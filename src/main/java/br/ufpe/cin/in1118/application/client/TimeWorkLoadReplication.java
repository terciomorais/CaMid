package br.ufpe.cin.in1118.application.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufpe.cin.in1118.distribution.stub.DelayStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.management.monitoring.DataPoint;
import br.ufpe.cin.in1118.management.monitoring.Event;

public class TimeWorkLoadReplication {

	public static void main(String[] args) {
		String 	host		= args[0];
		int		port		= Integer.parseInt(args[1]);
		int 	serviceTime	= Integer.parseInt(args[2]);
		int		lowUsers	= Integer.parseInt(args[3]);
		int		highUsers	= Integer.parseInt(args[4]);
		int		reqTax		= Integer.parseInt(args[5]);
		String	scenario	= args[6];

		NamingStub	naming	= new NamingStub(host, port);
		DelayStub	delay	= (DelayStub) naming.lookup("delay");

		//ramp up
		System.out.println("\n\n[TimeWorkLoadReplication] stating ramp up");
		for(int i = 0; i < 50; i++)
			delay.delay(1);

		ExecutorService	es = Executors.newFixedThreadPool(512);
		System.out.println("[TimeWorkLoadReplication] starting " + lowUsers + " users.");
		
		for(int i = 0; i < lowUsers; i++){
			es.execute(new SenderRunner(reqTax, serviceTime, 600000, host, port, false));
		}
		
		Thread.currentThread();
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("[TimeWorkLoadReplication] starting " + (highUsers - lowUsers) + " users.");
		
		for(int i = 0; i < (highUsers - lowUsers); i++){
			es.execute(new SenderRunner(reqTax, serviceTime, 480000, host, port, false));
		}
		//finalizing experiment
		es.shutdown();
		try {
			es.awaitTermination(1L, java.util.concurrent.TimeUnit.DAYS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Collections.sort(SenderRunner.elapsedTimes, new Comparator<Event>() {
			@Override
			public int compare(Event o1, Event o2) {
				return o1.compareTo(o2);
			}
		});
		
		List<DataPoint> datapoints = new ArrayList<DataPoint>();
		List<Double> times = new ArrayList<>();
		int firstPos = 0;
		times.add((double) SenderRunner.elapsedTimes.get(0).getElapsedTime());
		
		for (int i = 1; i < SenderRunner.elapsedTimes.size(); i++){
			times.add((double) SenderRunner.elapsedTimes.get(i).getElapsedTime());
			
			if ((SenderRunner.elapsedTimes.get(i).getTimeStamp() -
					SenderRunner.elapsedTimes.get(firstPos).getTimeStamp()) >= 10000){
				DataPoint dp = new DataPoint(times);
				dp.setData();
				dp.setStatistics();
				
				datapoints.add(dp);
				times.clear();
				firstPos = i + 1;
			}
		}
		
		System.out.println("[TimeWorkLoadReplication] Populating file...");
		
		BufferedWriter bw = null;
		File log = new File("logs/node/" + scenario + "-" + serviceTime + "ms-" + reqTax + "ReqsPerSec-" +  lowUsers + "-" + highUsers + "users.csv");
		if (!log.exists()){
			System.out.println("[TimeWorkLoadReplication] File does not exist. Creating file...");
			try {
				log.createNewFile();
				bw = new BufferedWriter(new FileWriter(log, true));
				bw.write("time;response time (ms)");
				bw.newLine();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
				bw = new BufferedWriter(new FileWriter(log, true));
				
				for(int i = 0; i < datapoints.size(); i++){
					System.out.println("[TimeWorkLoadReplication] elapsed time "
							+ (i + 1)*10 + " min = "
							+ datapoints.get(i).getAverage());

					bw.write((i + 1)*10 + ";" + datapoints.get(i).getAverage());
					bw.newLine();
				}
				bw.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		System.exit(0);
	}
}
