package br.ufpe.cin.in1118.management.node.monitoring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.utils.Network;

public class SystemMonitor implements Runnable{
	
	private String 				osName		= "";
	private String 				arch		= "";
	private String 				osVersion	= "";
	private SystemAgent 		agent		= null;
	private long 				interval	= 1000;
	private String 				fileLog		= "logs/node/systemLog";
	
	private List<SystemDataPoint> timeSeries = null;
	
	public SystemMonitor(){
		this.setArch();
		this.setOsName();
		this.setOsVersion();
		
		this.timeSeries = Collections.synchronizedList(new ArrayList<SystemDataPoint>());
		this.agent = new SystemAgent(this.interval);
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
	}
	
	public SystemMonitor(long interval){
		this.setArch();
		this.setOsName();
		this.setOsVersion();
		
		this.timeSeries = Collections.synchronizedList(new ArrayList<SystemDataPoint>());
		this.agent = new SystemAgent(interval);
		this.interval = interval;
	}
	
	public SystemMonitor(long interval, String fileLog){
		this.setArch();
		this.setOsName();
		this.setOsVersion();
		
		this.timeSeries = Collections.synchronizedList(new ArrayList<SystemDataPoint>());
		this.agent = new SystemAgent(interval);
		this.interval		= interval;
		this.fileLog		= fileLog;
	}
	
	public String getOsName() {
		return osName;
	}
	private void setOsName() {
		this.osName = System.getProperty("os.name");
	}
	public String getArch() {
		return arch;
	}
	private void setArch() {
		this.arch = 
				this.osName = System.getProperty("os.arch");
	}
	public String getOsVersion() {
		return osVersion;
	}
	
	private void setOsVersion() {
		this.osVersion = System.getProperty("os.version");
	}
	
	public long getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public String getFileLog() {
		return fileLog;
	}
	public void setFileLog(String fileLog) {
		this.fileLog = fileLog;
	}
	
	@Override
	public void run() {
		Broker.getExecutorInstance().execute(agent);
		System.out.println("[SystemMonitor] Starting system monitor...\n");
		System.out.println(" ----------------------------------------------------");
		System.out.println("|  [SystemMonitor] System info \n|    Arch: "
			+ this.getArch()
			+ "\n|    OS name: " + this.getOsName()
			+ "\n|    OS version: " + this.getOsVersion());
		System.out.println(" ----------------------------------------------------");
		
		while(true)
			try {
				Thread.currentThread();
				Thread.sleep(interval);
				
				if(!SystemAgent.getSystemDataList().isEmpty()){
					
					this.timeSeries.add(new SystemDataPoint(
							new ArrayList<SystemData>(SystemAgent.getSystemDataList())));
					SystemAgent.getSystemDataList().clear();
					long yesterday = (this.timeSeries== null || this.timeSeries.isEmpty()) ? 
							this.timeSeries.get(0).getSystemDataList().get(0).getTimeStamp():
								System.currentTimeMillis();
							
					long day = System.currentTimeMillis() - yesterday;
					if(day >= interval) //86400)
						this.saveToFile();	
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	private void saveToFile(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		calendar.setTimeInMillis(System.currentTimeMillis());
		fileLog.concat(Network.recoverAddress("localhost")
						+ sdf.format(calendar.getTime()) 
						+ ".log");
		
		Gson gson = new Gson();
		String tsString = gson.toJson(this.timeSeries);
		BufferedWriter bw = null;
		File log = new File(fileLog);
		if(!log.exists())
			try {
				log.createNewFile();
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
		this.timeSeries.clear();
	}
}
