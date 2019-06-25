package br.ufpe.cin.in1118.management.node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;
import br.ufpe.cin.in1118.distribution.stub.CloudManagerServiceStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.utils.EndPoint;

public class ClientCloudManager {

	private EndPoint endpoint = null;
	private String host = null;
	private int port = -1;
	private Set<EndPoint> endpoints = FrontEnd.getInstance().getClouds();

	public ClientCloudManager() {
	}

	public ClientCloudManager(EndPoint endpoint) {
		this.host = endpoint.getHost();
		this.port = endpoint.getPort();
	}

	public ClientCloudManager(String host, int port) {
		this.host = host;
		this.port = port;
		this.endpoint = new EndPoint(host, port);
	}

	public ClientCloudManager(Set<EndPoint> endpoints) {
		this.endpoints = endpoints;
	}

	public void sendAlert(Analysis analysis) {
		AlertSenderRunner alert = new AlertSenderRunner(analysis);
		Broker.getExecutorInstance().execute(alert);
	}
	
	public Map<EndPoint, List<SystemDataPoint>> requestCloudResources() {
		if (this.endpoints != null && this.endpoints.isEmpty())
			return null;
		else {
			Map<EndPoint, List<SystemDataPoint>> nodesSystemData = new HashMap<EndPoint, List<SystemDataPoint>>();
			CloudManagerServiceStub cmss = new CloudManagerServiceStub();
			cmss.setClassName("br.ufpe.cin.in1118.management.domain.CloudManagerService");
			cmss.setForwarded(false);
			cmss.getInvocation().setHasReturn(true);
			cmss.setServiceName("CloudManagerService".toLowerCase());
			for (EndPoint ep : this.endpoints) {
				cmss.setHost(ep.getHost());
				cmss.setPort(ep.getPort());
				cmss.setFeHost(ep.getHost());
				cmss.setFePort(ep.getPort());
				nodesSystemData.put(ep, cmss.requestCloudResources());
			}
			return nodesSystemData;
		}
	}

	public void scaleCloud(Analysis analysis, EndPoint targetCloud) {
		CloudScalerRunner scalerRunner = new CloudScalerRunner(analysis, targetCloud);
		Broker.getExecutorInstance().execute(scalerRunner);
	}

	private class AlertSenderRunner implements Runnable {

		private String action;
		private Analysis analysis;

		AlertSenderRunner(Analysis analysis) {
			this.analysis = analysis;
			this.action = "alert";
		}

		@Override
		public void run() {
			String host = Broker.getSystemProps().getProperties().getProperty("naming_host");
			int port = Integer.parseInt(Broker.getSystemProps().getProperties().getProperty("naming_port"));

			NamingStub naming = new NamingStub(host, port);
			CloudManagerServiceStub domainManager = (CloudManagerServiceStub) naming.lookup("management");
			domainManager.setForwarded(false);
			if (this.action.equals("alert")) {
				System.out.println("[ClientCloudManager:45] Analysis data: " + analysis.getAlertMessage()
						+ "\n                     CPU usage: "
						+ analysis.getSystemDataPoint().getCpuUsage().getAverage()
						+ "\n                     Service size: "
						+ analysis.getSystemDataPoint().getSystemDataList().size());
				domainManager.alert(this.analysis);
			}
		}
	}

	private class CloudScalerRunner implements Runnable {

		Analysis analysis		= null;
		EndPoint targetCloud	= null;

		CloudScalerRunner(Analysis analysis, EndPoint targetCloud) {
			this.analysis = analysis;
			this.targetCloud = targetCloud;
		}

		@Override
		public void run() {
			String className = Broker.getRegistry().getRemoteObjectClass(this.analysis.getService()).getSimpleName();
			Class<?> clazz = null;
			try {
				clazz = Broker.getStub(className);
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}

			CloudManagerServiceStub cloudManager = new CloudManagerServiceStub();
			cloudManager.setHost(this.targetCloud.getHost());
			cloudManager.setPort(this.targetCloud.getPort());
			boolean flag = cloudManager.allocateResource(analysis);
			cloudManager.setForwarded(false);
			Broker.publishService(analysis.getService(),
					clazz,
					true,
					FrontEnd.getInstance().getEndpoint().getHost(),
					FrontEnd.getInstance().getEndpoint().getPort(),
					targetCloud.getHost(),
					targetCloud.getPort());
		}


	}
}