package br.ufpe.cin.in1118.management.analysing;

import java.util.Set;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;
import br.ufpe.cin.in1118.distribution.stub.NodeManagerServiceStub;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.management.node.NodeManager;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.utils.Network;

public class Analyser {

	private float 	cpuLowerThreshold	= 0;
	private float 	cpuHigherThreshold	= 0;
	private float 	rtLowerThreshold	= 0;
	private float	rtHigherThreshold	= 0;
	private boolean paused				= false;
	private double	lastServiceAlert	= 0;
	
	public Analyser(){
		this.cpuLowerThreshold =
				Broker.getSystemProps().getProperties().containsKey("cpu_lower_threshold")
				?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("cpu_lower_threshold"))
				:0;
		this.cpuHigherThreshold =
				Broker.getSystemProps().getProperties().containsKey("cpu_higher_threshold")
				?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("cpu_higher_threshold"))
				:0;
		this.rtLowerThreshold = Broker.getSystemProps().getProperties().containsKey("response_time_lower_threshold")
		?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("response_time_lower_threshold")) * 1000000
		:0;
		this.rtHigherThreshold = Broker.getSystemProps().getProperties().containsKey("response_time_higher_threshold")
		?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("response_time_higher_threshold")) * 1000000
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

	public double getLastServiceAlert() {
		return this.lastServiceAlert;
	}

	private void setLastServiceAlert(double lastServiceAlert) {
		this.lastServiceAlert = lastServiceAlert;
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
		analysis.setService(service);
		analysis.setSystemMonitorEnabled(NodeManager.getInstance().isSysMonitorEnabled());
		analysis.setObjectMonitorEnabled(NodeManager.getInstance().isObjectMonitorEnabled());
		analysis.setServiceEndPoints(FrontEnd.getInstance().getService(service).getEndPoints());
		
		NodeManagerServiceStub	nodeManager = null;
		
		/* System.out.println("\n[Analyser:97] Response time "  + service + " = " + dataPoint.getStatistics().getAverage()*0.000000001 + " s\n"
							+ "              Throughput = " + dataPoint.getStatistics().getThroughput());
 */
		if(dataPoint.getStatistics().getAverage() > this.rtHigherThreshold 
				&& (System.currentTimeMillis() - this.getLastServiceAlert()) > 60000){
			Set<EndPoint> otherNodes = FrontEnd.getInstance().getNodes();
			System.out.println("\n[Analyser:102] Response time overload "  + service + " = " + dataPoint.getStatistics().getAverage()*0.000000001 + " s\n"
								+ "                 Throughput = " + dataPoint.getStatistics().getThroughput());
			if (Broker.getROLE().equals("frontend")){
				EndPoint ep = new EndPoint(Network.recoverAddress("localhost"), Integer.parseInt((String)Broker.getSystemProps().getProperties().get("port_number")));
				otherNodes.remove(ep);
			}
			
			analysis.setAlertMessage("response time overload");
			analysis.setServiceAlert(true);
			
/* 			if(FrontEnd.getInstance().getService(service).hasReplicas()){
				//Get system information from replica nodes
				for(EndPoint e : analysis.getServiceEndPoints()){
					nodeManager = (NodeManagerServiceStub) Broker.getNaming().lookup("NodeManagerService".toLowerCase() + '@' + e.getHost());
					if(nodeManager != null){
						nodeManager.setForwarded(false);
						analysis.addReplicaSystemData(e.getHost(), nodeManager.getSystemData());
					}
				}
			} */
			
/* 			if(otherNodes != null && !otherNodes.isEmpty()){
				for(EndPoint ep : otherNodes){
					nodeManager = (NodeManagerServiceStub) Broker.getNaming().lookup("NodeManagerService".toLowerCase() + '@' + ep.getHost());
					if(nodeManager != null){
						nodeManager.setForwarded(false);
						analysis.addAvailableNodesSystemData(ep.getHost(), nodeManager.getSystemData());
					}
				}
			} */
			
			//analysis.setAvailableNodes(otherNodes);
			this.setLastServiceAlert(System.currentTimeMillis());
			
		} else if(dataPoint.getStatistics().getAverage() < this.rtLowerThreshold
					&& (System.currentTimeMillis() - this.getLastServiceAlert()) > 60000){
			
			System.out.println("\n[Analyser:149] Response time underload "  + service + " = " + dataPoint.getStatistics().getAverage()*0.000000001 + " s\n"
							+ "                 Throughput = " + dataPoint.getStatistics().getThroughput());

			Set<EndPoint> otherNodes = FrontEnd.getInstance().getNodes();
			otherNodes.removeAll(analysis.getServiceEndPoints());
			
			if (Broker.getSystemProps().getProperties().get("role").equals("frontend")){
				EndPoint ep = new EndPoint(Network.recoverAddress("localhost"), Integer.parseInt((String)Broker.getSystemProps().getProperties().get("port_number")));
				otherNodes.remove(ep);
			}

			analysis.setAlertMessage("response time underload");
			analysis.setServiceAlert(true);
/* 			if(FrontEnd.getInstance().getService(service).hasReplicas()){
				for(EndPoint ep : analysis.getServiceEndPoints()){
					nodeManager = (NodeManagerServiceStub) Broker.getNaming().lookup("NodeManagerService".toLowerCase() + '@' + ep.getHost());
					if(nodeManager != null){
						nodeManager.setForwarded(false);
						analysis.addReplicaSystemData(ep.getHost(), nodeManager.getSystemData());
					}
				}
			} */

			this.setLastServiceAlert(System.currentTimeMillis());
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
	