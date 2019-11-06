package br.ufpe.cin.in1118.application.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufpe.cin.in1118.distribution.stub.DelayStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;

public class ClassicWorkLoader {

	public static void main(String[] args) {

		String 	host			= args[0];
		int		port			= Integer.parseInt(args[1]);	
		int		serviceTime		= Integer.parseInt(args[2]);
		int		ini_thread		= Integer.parseInt(args[3]);
		int		end_thread		= Integer.parseInt(args[4]);
		int		step			= Integer.parseInt(args[5]);
		int 	interval		= Integer.parseInt(args[6]);
		int		experimentTime	= Integer.parseInt(args[7]);
		String	scenario		= args[8];

		int		requestCount	= 0;
		long	sum				= 0L;
		double	average			= 0;
		double	stdDeviation	= 0;
		long	squareSum		= 0L;
		long	maxValue		= 0L;
		long	minValue		= 0L;
		long 	totalTime		= 0L;

		System.out.println("\n -----------------------------------------------------------");
		System.out.println("|      Begining of all experiment service time " + serviceTime + " ms       |");
		System.out.println(" -----------------------------------------------------------");

		NamingStub	naming	= new NamingStub(host, port);
		DelayStub	delay	= (DelayStub) naming.lookup("delay");

		//ramp up
		for(int i = 0; i < 300; i++){
			//long b = System.currentTimeMillis();
			delay.delay(10);
			//System.out.println("[ClassicWorkLoader:49] " + (System.currentTimeMillis() - b) + "ms");
		}

		for(int tax = ini_thread; tax <= end_thread; tax += step){
			System.out.println("     Starting sample with " + tax + " simultaneous users.");
			SenderRunner.createMetricList();
			SenderRunner.elapsedTimes.clear();
			SenderRunner.fails.set(0);
			SenderRunner.success.set(0);

			requestCount	= 0;
			sum				= 0L;
			average			= 0;
			stdDeviation	= 0;
			squareSum		= 0L;
			maxValue		= 0L;
			minValue		= 0L;
			totalTime		= 0L;
			ExecutorService	es	= Executors.newFixedThreadPool(tax);

			long begin	= System.currentTimeMillis();
			for(int i = 0; i < tax; i++)
				es.execute(new SenderRunner(delay, serviceTime, experimentTime, interval));

			//finalizing threads
			es.shutdown();
			try {
				es.awaitTermination(1L, java.util.concurrent.TimeUnit.DAYS);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			totalTime += System.currentTimeMillis() - begin;

			//	System.out.println(" Starting statistic computation\n");
			//Statistic process
			if(SenderRunner.success.get() > 0){
				maxValue = minValue = SenderRunner.elapsedTimes.get(0).getElapsedTime();
				requestCount = SenderRunner.elapsedTimes.size();
				/*
			int count = 0;
			while (count < requestCount && minValue == -1)
				minValue = SenderRunner.elapsedTimes.get(++count);*/

				for(int l = 0; l < requestCount; l++){

					if(SenderRunner.elapsedTimes.get(l).getElapsedTime() >= serviceTime){
						minValue = SenderRunner.elapsedTimes.get(l).getElapsedTime() < minValue
								? SenderRunner.elapsedTimes.get(l).getElapsedTime()
								: minValue;
						maxValue = SenderRunner.elapsedTimes.get(l).getElapsedTime() > maxValue
								? SenderRunner.elapsedTimes.get(l).getElapsedTime()
								: maxValue;

						sum			+= SenderRunner.elapsedTimes.get(l).getElapsedTime();
						squareSum	+= SenderRunner.elapsedTimes.get(l).getElapsedTime() * SenderRunner.elapsedTimes.get(l).getElapsedTime();
					} else{
						SenderRunner.success.decrementAndGet();
						SenderRunner.fails.incrementAndGet();
					}
				}
				average = (double)sum/(double)SenderRunner.success.get();
				stdDeviation = Math.sqrt(((double)squareSum - ((double)sum * sum /SenderRunner.success.get()))/((double)SenderRunner.success.get() - 1));
			}

			BufferedWriter bw = null;
			File log = new File("logs/" + scenario + "-Overhead" + "-" + serviceTime + "ms-" + ini_thread + "-" + end_thread + "-" + serviceTime + ".csv");
			if (!log.exists())
				try {
					log.createNewFile();
					bw = new BufferedWriter(new FileWriter(log,true));
					bw.write("simultaneous users;total_time;total_invocations;success;failed;response time average (ms);standard_deviation;percent;throughput;fail_tax;hi_value;low_value");
					bw.newLine();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			try{
				bw = new BufferedWriter(new FileWriter(log,true));
				bw.write(tax
						+ ";" + totalTime
						+ ";" + requestCount
						+ ";" + SenderRunner.success
						+ ";" + SenderRunner.fails
						+ ";" + average
						+ ";" + stdDeviation
						+ ";" + stdDeviation/average*100
						+ ";" + (SenderRunner.success.get()*1000)/(double)totalTime
						+ ";" + SenderRunner.fails.get()/(double)requestCount*100
						+ ";" + maxValue
						+ ";" + minValue);

				bw.newLine();
				bw.close();
				System.out.println(" --------------------   RESULTS   --------------------");
				System.out.println("|   Response time average: " + average + " ms");
				System.out.println("|   Standard deviation: " + stdDeviation);
				System.out.println("|   Standard deviation percentage: " + stdDeviation/average*100 + " %");
				System.out.println("|   Throughput: " + (SenderRunner.success.get()*1000)/(double)totalTime + " request per second");
				System.out.println("|   Total successes: " + SenderRunner.success);
				System.out.println("|   Total fails: " + SenderRunner.fails);
				System.out.println("|   Fail tax: " + SenderRunner.fails.get()/(double)requestCount*100 + " %");
				System.out.println(" -----------------------------------------------------");

			} catch (IOException e) {
				e.printStackTrace();
			}
			Thread.currentThread();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	

		System.out.println(" -----------------------------------------------------------");
		System.out.println("|                 Finish of all experiment                  |");
		System.out.println(" -----------------------------------------------------------");
		System.exit(0);
	}
}
