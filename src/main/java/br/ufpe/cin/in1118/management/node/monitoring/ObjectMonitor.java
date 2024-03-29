package br.ufpe.cin.in1118.management.node.monitoring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;

import br.ufpe.cin.in1118.management.monitoring.Agent;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.node.NodeManager;

public class ObjectMonitor implements Runnable{

	private int 					interval		= 60000;
	private Map<String, Observer>	objectAgents	= new HashMap<String, Observer>();
	private String 					fileLog			= "logs/app/objectLog";

	private Map<String, List<InvokingDataPoint>> timeseries = new ConcurrentHashMap<String, List<InvokingDataPoint>>();

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
		this.objectAgents.put(className, new Agent());
		this.timeseries.put(className, new ArrayList<InvokingDataPoint>());
	}

	public void setAgents(Map<String, Observer> objectAgents) {
		this.objectAgents = objectAgents;
		this.setTimeSeries(objectAgents.keySet());
	}

	private void setTimeSeries(Set<String> keys){
		for(Iterator<String> it = keys.iterator(); it.hasNext();)
			this.timeseries.put(it.next(), new ArrayList<InvokingDataPoint>());
	}

	public void addTimeSeries(String key, List<InvokingDataPoint> idp){
		this.timeseries.put(key, idp);
	}

	public InvokingDataPoint getLastDataPoint(String className){
		return this.timeseries.get(className).get(this.timeseries.get(className).size() - 1);
	}

	@Override
	public void run() {
		System.out.println("[ObjectMonitor] Starting object monitoring...");
		while(true){
			try {
				Thread.sleep(this.interval);
				for(String className : this.objectAgents.keySet()){
					Agent ag = this.getAgent(className);
					if(ag.getEvents() != null && !ag.getEvents().isEmpty()){					
						InvokingDataPoint dataPoint = new InvokingDataPoint(ag.getEvents());
						this.timeseries.get(className).add(dataPoint);
						ag.clearEvents();
						if(!NodeManager.getInstance().getAnalyser().analyse(this.getLastDataPoint(className)
								.getStatistics().getAverage())){
							System.out.println("[ObjectMonitor:81] Average "
								+ this.getLastDataPoint(className)
									.getStatistics().getAverage());
							System.out.println("[ObjectMonitor:84] Threshold "
									+ NodeManager.getInstance().getAnalyser().getThreshold());
							NodeManager.getInstance().alert();
						}
					}

					if(this.timeseries.get(className).size() >= 10000)
						this.saveToFile(className);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveToFile(String className){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("-yyyy-MMM-dd");
		calendar.setTimeInMillis(System.currentTimeMillis());
		fileLog.concat(className + sdf.format(calendar.getTime()) + ".log");

		Gson gson = new Gson();
		String tsString = gson.toJson(this.timeseries.get(className));
		BufferedWriter bw = null;
		File log = new File(fileLog);
		if(!log.exists())
			try {
				log.createNewFile();
				bw = new BufferedWriter(new FileWriter(log,true));
				bw.write(tsString);
				bw.newLine();
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		try{
			bw = new BufferedWriter(new FileWriter(log,true));
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
