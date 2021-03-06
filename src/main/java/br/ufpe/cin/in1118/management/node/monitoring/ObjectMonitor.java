package br.ufpe.cin.in1118.management.node.monitoring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;

import com.google.gson.Gson;

import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.management.monitoring.Agent;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.node.NodeManager;

public class ObjectMonitor implements Runnable{
	
	private int 					interval		= 30000;
	private Map<String, Observer>	objectAgents	= new HashMap<String, Observer>();
	private String 					fileLog			= "logs/app/";
	
	//Stats list by service
	private Map<String, List<InvokingDataPoint>> timeseries = Collections.synchronizedMap(new HashMap<String, List<InvokingDataPoint>>());
	
	public ObjectMonitor(){
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
	}
	
	public Map<String, Observer> getObjectAgents() {
		return objectAgents;
	}
	
	public Agent getAgent(String className) {
		return (Agent)this.objectAgents.get(className);
	}
	
	public void addAgent(String className){
		this.objectAgents.put(className, new Agent(className));
		this.timeseries.put(className, new ArrayList<InvokingDataPoint>());
	}
	
	public void setAgents(Map<String, Observer> objectAgents) {
		this.objectAgents = objectAgents;
		//this.setTimeSeries(FrontEnd.getInstance().getServices().keySet());
		this.setTimeSeries();
	}
	
	private void setTimeSeries(){
		for(String className : this.objectAgents.keySet()){
			Set<String> services = ((Agent)this.objectAgents.get(className)).getServiceEvents().keySet();
			for(String str : services)
			this.timeseries.put(str, new ArrayList<InvokingDataPoint>());
		}
	}
	
	public Map<String, List<InvokingDataPoint>> getTimeSeries(){
		return this.timeseries;
	}

	public void addTimeSeries(String service, List<InvokingDataPoint> idp){
		this.timeseries.put(service, idp);
	}
	
	public InvokingDataPoint getLastDataPoint(String service){
		return this.timeseries.get(service).get(this.timeseries.get(service).size() - 1);
	}
	
	@Override
	public void run() {
		System.out.println("[ObjectMonitor] Starting object monitoring...");
		while(true){
			try {
				Thread.sleep(this.interval);
				for(String className : this.objectAgents.keySet()){
					Agent ag = this.getAgent(className);
					//System.out.println("[ObjectMonitor-77] monitored object: " + className);
					if(ag.getServiceEvents() != null && !ag.getServiceEvents().isEmpty()){
						for(String st : ag.getServiceEvents().keySet()){
							//System.out.println("                  monitored service: " + st);
							if(ag.getEvents(st) != null && !ag.getEvents(st).isEmpty()){
								InvokingDataPoint dataPoint = new InvokingDataPoint(ag.getEvents(st));
								if(!this.timeseries.containsKey(st))
									this.timeseries.put(st, new ArrayList<>());
								this.timeseries.get(st).add(dataPoint);

								Analysis analysis = NodeManager.getInstance().getObjectAnalyser().analyse(st, dataPoint);
								if(analysis.getAlertMessage().equals("anObject"));

								//System.out.println("[ObjectMonitor:92] Response time average: " + new BigDecimal(dataPoint.getStatistics().getAverage()).toPlainString());
								ag.clearEvents();
							}
							
/* 							if(NodeManager.getInstance().getObjectAnalyser().analyse(this.getLastDataPoint(st).getStatistics().getAverage()) != 0){
								System.out.println("[ObjectMonitor:81] Average " + this.getLastDataPoint(st).getStatistics().getAverage());
								//System.out.println("[ObjectMonitor:84] Threshold " + NodeManager.getInstance().getAnalyser().get;
								NodeManager.getInstance().alert(NodeManager.getInstance().getObjectAnalyser().analyse(this.getLastDataPoint(st).getStatistics().getAverage()));
							} */
							
							if(this.timeseries.get(st).size() >= 10000){
								this.saveToFile(st);
							}	
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void saveToFile(String service){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("-yyyy-MMM-dd");
		calendar.setTimeInMillis(System.currentTimeMillis());

		File folder = new File(fileLog);
		if(!folder.exists())
			folder.mkdirs();
		
		fileLog = fileLog.concat(service + sdf.format(calendar.getTime()) + ".log");
		Gson gson = new Gson();
		String tsString = gson.toJson(this.timeseries.get(service));
		BufferedWriter bw = null;
		File log = new File(fileLog);
		if(!log.exists()){
			try {
				log.createNewFile();
				bw = new BufferedWriter(new FileWriter(log,true));
				bw.write(tsString);
				bw.newLine();
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try{
			System.out.println("[objectMonitor-137] nome do arquivo: " + log.getPath());
			bw = new BufferedWriter(new FileWriter(log, true));
			bw.write(tsString);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(String key : this.timeseries.keySet())
			this.timeseries.get(key).clear();
	}
}
