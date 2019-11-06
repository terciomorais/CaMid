package br.ufpe.cin.in1118.management.analysing;

import java.util.Set;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.management.node.NodeManager;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.utils.Network;
import java.util.Queue;
import java.util.LinkedList;

public class Analyser {

	private float 	cpuLowerThreshold	= 0;
	private float 	cpuHigherThreshold	= 0;
	private float 	rtLowerThreshold	= 0;
	private float	rtHigherThreshold	= 0;
	private boolean paused				= false;
	private boolean	afterAdaptation		= false;
	private double	lastServiceAlert	= 0;
	private Queue<InvokingDataPoint> lastDataPoints = new LinkedList<InvokingDataPoint>();
	
	public Analyser(){

		this.cpuLowerThreshold =
				Broker.getSystemProps().getProperties().containsKey("cpu_lower_	threshold")
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
		return this.paused;
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

	public boolean isAfterAdaptation() {
		return this.afterAdaptation;
	}

	public boolean getAfterAdaptation() {
		return this.afterAdaptation;
	}

	private void setAfterAdaptation(boolean afterAdaptation) {
		this.afterAdaptation = afterAdaptation;
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

		if (this.lastDataPoints.size() < 3){
			this.lastDataPoints.add(dataPoint);
		} else {
			this.lastDataPoints.poll();
			this.lastDataPoints.add(dataPoint);
		}

		Analysis analysis = new Analysis();
		analysis.setService(service);
		analysis.setSystemMonitorEnabled(NodeManager.getInstance().isSysMonitorEnabled());
		analysis.setObjectMonitorEnabled(NodeManager.getInstance().isObjectMonitorEnabled());
		if(FrontEnd.getInstance().getService(service) != null){
			analysis.setServiceEndPoints(FrontEnd.getInstance().getService(service).getEndPoints());
		}
		
		//NodeManagerServiceStub nodeManager = null;

		System.out.println("\n[Analyser:133]                 Is adaption?          " + this.isAfterAdaptation());
		System.out.println("[Analyser:134]                 Response time         " + dataPoint.getStatistics().getAverage()*0.000001 + " ms");
		System.out.println("[Analyser:135]                 Is a peak?            " + this.isPeak(this.lastDataPoints));
		System.out.println("[Analyser:136]                 Last dataPoints size: " + this.lastDataPoints.size() + "\n");

		if(this.isAfterAdaptation() && !this.isPaused()){
			if(dataPoint.getStatistics().getAverage() > this.rtLowerThreshold && dataPoint.getStatistics().getAverage() < this.rtHigherThreshold){
				this.setAfterAdaptation(false);
			}
			//System.out.println("[Analyser:142]: Wainting for adaptaion");
		} else {
			if(!this.isPeak(this.lastDataPoints) && !this.isAfterAdaptation()){ // && dataPoint.getStatistics().getAverage() > this.rtHigherThreshold){
					
				FrontEnd.getInstance().getReactionTime().setInitTime(System.currentTimeMillis());
				
				System.out.println("\n-----------------------------------------------------------------------");
				System.out.println("[Analyser:136] Response time OVERLOAD "  + service + " = " + dataPoint.getStatistics().getAverage()*0.000001 + " ms"
					+  "\n               Throughput = " + dataPoint.getStatistics().getThroughput() + " requests/s");
				System.out.println("-----------------------------------------------------------------------\n");
				Set<EndPoint> otherNodes = FrontEnd.getInstance().getNodes();
					
				if (Broker.getROLE().equals("frontend")){
					EndPoint ep = new EndPoint(Network.recoverAddress("localhost"), Integer.parseInt((String)Broker.getSystemProps().getProperties().get("port_number")));
					otherNodes.remove(ep);
				}
				
				analysis.setAlertMessage("response time overload");
				analysis.setServiceAlert(true);
				analysis.setAvailableNodes(otherNodes);
				
				//analysis.setAvailableNodes(otherNodes);
				this.setLastServiceAlert(System.currentTimeMillis());

				//this.setPaused(true);
				this.setAfterAdaptation(true);
				
			} else if(!this.isValley(this.lastDataPoints) && !this.isAfterAdaptation() && dataPoint.getStatistics().getThroughput() > 20){
					//&& dataPoint.getStatistics().getAverage() < this.rtLowerThreshold && dataPoint.getStatistics().getThroughput() > 20){
				
				FrontEnd.getInstance().getReactionTime().setInitTime(System.currentTimeMillis());
				
				System.out.println("\n-----------------------------------------------------------------------");
				System.out.println("[Analyser:176] Response time UNDERLOAD "  + service + " = " + dataPoint.getStatistics().getAverage()*0.000001 + " ms"
					+  "\n               Throughput = " + dataPoint.getStatistics().getThroughput() + " requests/s");
				System.out.println("-----------------------------------------------------------------------\n");
				
				Set<EndPoint> otherNodes = FrontEnd.getInstance().getNodes();
				otherNodes.removeAll(analysis.getServiceEndPoints());
				
				if (Broker.getSystemProps().getProperties().get("role").equals("frontend")){
					EndPoint ep = new EndPoint(Network.recoverAddress("localhost"), Integer.parseInt((String)Broker.getSystemProps().getProperties().get("port_number")));
					otherNodes.remove(ep);
				}
				
				analysis.setAlertMessage("response time underload");
				analysis.setServiceAlert(true);
				analysis.setAvailableNodes(otherNodes);
				
				this.setLastServiceAlert(System.currentTimeMillis());
				//this.setPaused(true);
				this.setAfterAdaptation(true);

			} else {
				this.setPaused(false);
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

	private boolean isPeak(Queue<InvokingDataPoint> queue){
		Queue<InvokingDataPoint> q = new LinkedList<InvokingDataPoint>(queue);
		boolean flag = false;
		if(q.size() == 3){
			while(!q.isEmpty()){
				if(q.poll().getStatistics().getAverage() <= this.rtHigherThreshold){
					flag = true;
					break;
				}
			}
			
			if(flag)
				return true;
			else
				return false;
		} else{
			return true;
		}
	}

	private boolean isValley(Queue<InvokingDataPoint> queue){
		Queue<InvokingDataPoint> q = new LinkedList<InvokingDataPoint>(queue);
		boolean flag = false;
		if(q.size() == 3){
			while(!q.isEmpty()){
				if(q.poll().getStatistics().getAverage() >= this.rtLowerThreshold){
					flag = true;
					break;
				}
			}
			
			if(flag)
				return true;
			else
				return false;
		} else{
			return true;
		}
	}
}
	