package br.ufpe.cin.in1118.management.analysing;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.distribution.stub.NodeManagerServiceStub;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.management.node.NodeManager;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.utils.Network;

public class Analyser {

	private float cpuLowerThreshold		= 0;
	private float cpuHigherThreshold	= 0;
	private float rtLowerThreshold		= 0;
	private float rtHigherThreshold		= 0;
	private boolean paused				= false;
	
	public Analyser(){
		this.cpuLowerThreshold =
				Broker.getSystemProps().getProperties().containsKey("lower_threshold")
				?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("lower_threshold"))
				:0;
		this.cpuHigherThreshold =
				Broker.getSystemProps().getProperties().containsKey("cpu_lower_threshold")
				?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("cpu_higher_threshold"))
				:0;
	}
	
	public Analyser(String resource){
		if(resource.equals("cpu")){
			this.cpuLowerThreshold = Broker.getSystemProps().getProperties().containsKey("cpu_lower_threshold")
				?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("cpu_lower_threshold"))
				:0;
			this.cpuHigherThreshold = Broker.getSystemProps().getProperties().containsKey("cpu_higher_threshold")
				?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("cpu_higher_threshold"))
				:0;
		}
	}

	public float getLowerThreshold(){
		return this.cpuLowerThreshold;
	}
	
	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

/*	public boolean analyse(double metric){
		//call the domain manager if necessary
		if (this.higherThreshold == 0 || this.paused)
			return true;
//		System.out.println("[Analyser:33] Analysis (" + this.paused +")" + this.threshold + " > " + metric + "?");
		return this.higherThreshold > metric;
	}*/
	
/*	public boolean analyse(double metric){
		if (this.higherThreshold == 0 || this.paused)
			return true;
		return this.higherThreshold > metric && this.lowerThreshold > metric;
	}
*/	
/* 	public short analyse(double metric){
		short alert = 0;
		if (this.higherThreshold == 0 || this.paused)
			return alert;
		else if (metric < this.lowerThreshold)
			alert = -1;
		else if (metric > this.higherThreshold)
			alert = 1;
		return alert;
	} */

	public Analysis analyse(String service, InvokingDataPoint dataPoint){
		Analysis analysis = new Analysis();
		analysis.setSystemMonitorEnabled(NodeManager.getInstance().isSysMonitorEnabled());
		analysis.setObjectMonitorEnabled(NodeManager.getInstance().isObjectMonitorEnabled());
		Set<EndPoint> otherNodes	= null;
		Set<EndPoint> replicaNodes	= FrontEnd.getInstance().getService(service).getEndPoints();

		if(dataPoint.getStatistics().getAverage() > this.rtHigherThreshold){
			analysis.setAlertMessage("reponse time overload");
			analysis.setServiceAlert(true);
			if(FrontEnd.getInstance().getService(service).hasReplicas()){
				otherNodes = FrontEnd.getInstance().getNodes();
				otherNodes.removeAll(replicaNodes);

				//Get system information from replica nodes
				String					namingHost	= Broker.getSystemProps().getProperties().getProperty("naming_host");
				int						namingPort	= Integer.parseInt(Broker.getSystemProps().getProperties().getProperty("naming_port"));
				NamingStub				naming		= new NamingStub(namingHost, namingPort);
				NodeManagerServiceStub	nodeManager = null;
				
				for(EndPoint ep : replicaNodes){
					nodeManager = (NodeManagerServiceStub) naming.lookup("NodeManagerService".toLowerCase() + '@' + ep.getHost());
					analysis.addReplicaSystemData(nodeManager.getSystemData());
				}

				if(otherNodes != null || !otherNodes.isEmpty()){
					for(EndPoint ep : otherNodes){
						if (Broker.getSystemProps().getProperties().get("role").equals("frontend")
								&& ep.getHost().equals(Network.recoverAddress("localhost")))
							otherNodes.remove(ep);

						nodeManager = (NodeManagerServiceStub) naming.lookup("NodeManagerService".toLowerCase() + '@' + ep.getHost());
						analysis.addNodeSystemData(nodeManager.getSystemData());
					}
				}
				analysis.setAvailableNodes(otherNodes);
			}
		}
		return analysis;
	}

	public Analysis analyse(SystemDataPoint dataPoint){
		Analysis analysis = new Analysis();
		analysis.setSystemMonitorEnabled(NodeManager.getInstance().isSysMonitorEnabled());
		analysis.setObjectMonitorEnabled(NodeManager.getInstance().isObjectMonitorEnabled());
		
		if (dataPoint.getLastSystemData().getCpuUsage() > this.cpuHigherThreshold){
			analysis.setAlertMessage("CPU overload");
			analysis.setResourceAlert(true);
		} else if (dataPoint.getLastSystemData().getCpuUsage() < this.cpuLowerThreshold){
			analysis.setAlertMessage("CPU underused");
			analysis.setResourceAlert(true);
		}
		analysis.setSystemDataPoint(dataPoint);
		if (NodeManager.getInstance().isObjectMonitorEnabled())
			analysis.setServicesMetering(NodeManager.getInstance().getObjectMonitor().getTimeSeries());
		return analysis;
	}
}
	