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
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		int serviceTime = Integer.parseInt(args[2]);
		int lowUsers = Integer.parseInt(args[3]);
		int highUsers = Integer.parseInt(args[4]);
		String scenario = args[5];
		int interval = Integer.parseInt(args[6]);

		NamingStub naming = new NamingStub(host, port);
		DelayStub delay = (DelayStub) naming.lookup("delay");

		// ramp up
		System.out.println("\n\n[TimeWorkLoadReplication:34] **** Stating ramp up ****");
		for (int i = 0; i < 100; i++)
			delay.delay(100);

		ExecutorService es = Executors.newFixedThreadPool(512);
		System.out.println("[TimeWorkLoadReplication:39] starting " + lowUsers + " users.");

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < lowUsers; i++) {
			es.execute(new SenderRunner(delay, serviceTime, 1800000, interval, false));
			Thread.currentThread();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.err.println("[TimeWorkloadReplication:49] Thread sleeping error");
			}
			//es.execute(new SenderRunner(delay, serviceTime, 840000, interval, false));
			//es.execute(new SenderRunner(reqTax, serviceTime, 600000, host, port, false));
		}
		
		do{
			System.out.println("[TimeWorkLoadReplication:50] sleeping for 60 s ");
			long rectionTime = System.currentTimeMillis();
			Thread.currentThread();
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("[TimeWorkLoadReplication:58] starting more " + (highUsers - lowUsers) + " users.");
			
			for(int i = 0; i < (highUsers - lowUsers); i++){
				es.execute(new SenderRunner(delay, serviceTime, 180000, interval, false));
				Thread.currentThread();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					System.err.println("[TimeWorkloadReplication:49] Thread sleeping error");
				}
				//es.execute(new SenderRunner(delay, serviceTime, 240000, interval, false));
			}
			
			Thread.currentThread();
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("[TimeWorkLoadReplication:63] finishing a reaction lap: " + (System.currentTimeMillis()-rectionTime)/1000.0 + " s");
		} while((System.currentTimeMillis() - startTime) <= 1800000);
		
		//finalizing experiment
		es.shutdown();
		try {
			es.awaitTermination(1L, java.util.concurrent.TimeUnit.DAYS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("\n[TimeWorkLoadReplication:78] Total time: " + (System.currentTimeMillis() - startTime)/1000.0 + "s\n");
		Collections.sort(SenderRunner.elapsedTimes, new Comparator<Event>() {
			@Override
			public int compare(Event o1, Event o2) {
				return o1.compareTo(o2);
			}
		});
		
		List<DataPoint>	datapoints	= new ArrayList<DataPoint>();
		List<Double> 	times		= new ArrayList<>();
		int 			firstPos	= 0;
		
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
		
		System.out.println("\n[TimeWorkLoadReplication:113] Populating file...");
		
		BufferedWriter bw = null;
		File log = new File("logs/" + scenario + "-" + serviceTime + "ms-" + lowUsers + "-" + highUsers + "users.csv");
		if (!log.exists()){
			System.out.println("\n[TimeWorkLoadReplication:118] File does not exist. Creating file...");
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
				System.out.println("\n[TimeWorkLoadReplication:134] elapsed time "
				+ (i + 1)*10 + " s = "
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
