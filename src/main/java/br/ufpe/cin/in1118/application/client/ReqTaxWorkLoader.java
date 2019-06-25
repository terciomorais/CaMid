package br.ufpe.cin.in1118.application.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufpe.cin.in1118.distribution.stub.DelayStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.management.monitoring.Event;

public class ReqTaxWorkLoader {

	public static void main(String[] args) {

		String 	host			= args[0];
		int		port			= Integer.parseInt(args[1]);	
		int		serviceTime		= Integer.parseInt(args[2]);
		int		ini_users		= Integer.parseInt(args[3]);
		int		end_users		= Integer.parseInt(args[4]);
		int		step			= Integer.parseInt(args[5]);
		int		reqTax			= Integer.parseInt(args[6]);
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
		int sumT = 0;
		for(int i = 0; i < 200; i++){
			long b = System.currentTimeMillis();
			delay.delay(1);
			sumT += System.currentTimeMillis() - b;
		}
		
		System.out.println("[ReqTaxWorkLoader:47] Average time during ramp up: " + sumT/100 + " ms");
		for(int tax = ini_users; tax <= end_users; tax += step){
			System.out.println(tax
					+ " simultaneous users at a request rate of "
					+ reqTax
					+ " requests per second.");
			requestCount	= 0;
			sum				= 0L;
			average			= 0;
			stdDeviation	= 0;
			squareSum		= 0L;
			maxValue		= 0L;
			minValue		= 0L;
			totalTime		= 0L;
			SenderRunner.createMetricList();
			SenderRunner.elapsedTimes.clear();
			SenderRunner.success.set(0);
			SenderRunner.fails.set(0);
			SenderRunner.systemFails.set(0);
			
			long ini = System.currentTimeMillis();			
			ExecutorService	es = Executors.newFixedThreadPool(tax);
			
			for(int i = 0; i < tax; i++)
				es.execute(new SenderRunner(reqTax,serviceTime,experimentTime, host, port));

			es.shutdown();

			try {
				es.awaitTermination(1L, java.util.concurrent.TimeUnit.DAYS);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			totalTime += System.currentTimeMillis() - ini;

			//	System.out.println(" Starting statistic computation\n");
			//Statistic process
			if(SenderRunner.success.intValue() > 0){
				maxValue = minValue = SenderRunner.elapsedTimes.get(0).getElapsedTime();
				requestCount = SenderRunner.elapsedTimes.size();

				for(Event ev : SenderRunner.elapsedTimes){
					if(ev.getElapsedTime() >= serviceTime){
						minValue = ev.getElapsedTime() < minValue
									? ev.getElapsedTime()
									: minValue;
						maxValue = ev.getElapsedTime() > maxValue
									? ev.getElapsedTime()
									: maxValue;

						sum			+= ev.getElapsedTime();
						squareSum	+= ev.getElapsedTime() * ev.getElapsedTime();
					} else {
						SenderRunner.success.decrementAndGet();
						SenderRunner.systemFails.incrementAndGet();
					}
				}
				average = (double)sum/(double)SenderRunner.success.get();
				stdDeviation = Math.sqrt(((double)squareSum - ((double)sum * sum /SenderRunner.success.get()))/((double)SenderRunner.success.get() - 1));
			}

			BufferedWriter bw = null;
			BufferedWriter bwTime = null;
			File log = new File("logs/" + scenario + "-" + serviceTime + "ms-" + reqTax + "ReqsPerSec-" +  ini_users + "-" + end_users + "users.csv");
			File logTime = new File("logs/" + scenario + "-" + serviceTime + "ms-" + reqTax + "ReqsPerSec-" +  ini_users + "-" + end_users + "users-elapsedTimes.csv");
			if (!log.exists())
				try {
					log.createNewFile();
					bw = new BufferedWriter(new FileWriter(log,true));
					bw.write("simultaneous users;total_time;total_invocations;success;mw_failed;vm_fails;response time average (ms);standard_deviation;percent;throughput;fail_tax;hi_value;low_value");
					bw.newLine();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			if (!logTime.exists())
				try {
					logTime.createNewFile();
					bwTime = new BufferedWriter(new FileWriter(logTime,true));
					bwTime.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			try{
				bwTime = new BufferedWriter(new FileWriter(logTime,true));
				int cont = 0;
				for(Event ev : SenderRunner.elapsedTimes){
					bwTime.write(ev.getElapsedTime() + ";");
					cont++;
					if(cont == 20){
						bwTime.newLine();
						cont = 0;
					}
				}
				bwTime.write("\b \b");
			} catch (IOException e) {
				e.printStackTrace();
			}

			try{
				bw = new BufferedWriter(new FileWriter(log,true));
				bw.write(tax
						+ ";" + totalTime
						+ ";" + requestCount
						+ ";" + SenderRunner.success.get()
						+ ";" + SenderRunner.fails.get()
						+ ";" + SenderRunner.systemFails.get()
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
				System.out.println("|   Total invocations: " + requestCount);
				System.out.println("|   Response time average: " + average + " ms");
				System.out.println("|   Standard deviation: " + stdDeviation);
				System.out.println("|   Standard deviation percentage: " + stdDeviation/average*100 + " %");
				System.out.println("|   Throughput: " + (SenderRunner.success.get()*1000)/(double)totalTime + " request per second");
				System.out.println("|   Total successes: " + SenderRunner.success.get());
				System.out.println("|   Middleware fails: " + SenderRunner.fails.get());
				System.out.println("|   OS fails: " + SenderRunner.systemFails.get());
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
