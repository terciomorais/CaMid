package br.ufpe.cin.middleware.app.statistics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import br.ufpe.cin.camid.distribution.skeleton.Delay;
import br.ufpe.cin.camid.distribution.stub.DelayStub;
import br.ufpe.cin.camid.facade.Middleware;
import br.ufpe.cin.camid.facade.MiddlewareConfig;
import br.ufpe.cin.camid.service.naming.NamingService;
import br.ufpe.cin.camid.service.naming.Service;
import br.ufpe.cin.camid.service.naming.ServiceCreator;

public class StatisticsClient 
{
	
	public static long minToMilis(long minutes)
	{
		long milis = minutes*60*1000; 
		return milis;
	}
	
	private static List<ClientTask> clientTasks;
	
	public static List<ClientTask> createWorkloadList(DelayStub delay, StatFileWriter statWriter, AtomicLong successes, int workload, long wait)
	{
		if(clientTasks == null || (clientTasks.size() != workload) )
		{
			clientTasks = new ArrayList<ClientTask>(workload);
			for(int i = 0; i < workload ; i++)
				clientTasks.add(new ClientTask(delay, statWriter, successes, workload, wait));
		}
		
		return clientTasks;
	}
	
	public static void main(String[] args) throws IOException 
	{
		String host = null;
		Integer port = null;
		Integer load = null;
		Integer time = null;
		Long wait = null;
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("-host"))
			{
				host = args[i+1];
			}
			if(args[i].equals("-port"))
			{
				try
				{
					port = Integer.parseInt(args[i+1]);
				} 
				catch(NumberFormatException e)
				{
					e.printStackTrace();
				}
			}
			if(args[i].equals("-load"))
			{
				try
				{
					load = Integer.parseInt(args[i+1]);
				} 
				catch(NumberFormatException e)
				{
					e.printStackTrace();
				}
			}
			if(args[i].equals("-time"))
			{
				try
				{
					time = Integer.parseInt(args[i+1]);
				} 
				catch(NumberFormatException e)
				{
					e.printStackTrace();
			}
			}
			if(args[i].equals("-service-delay"))
			{
				try
				{
					wait = Long.parseLong(args[i+1]);
				} 
				catch(NumberFormatException e)
				{
					e.printStackTrace();
				}
			}
		}
		System.out.println("Configuring parameters...");
		//Default values 
		host = host == null ? "127.0.0.1" : host;
		System.out.println("Host: " + host);
//		port = port == null ? NamingService.PORT : port;
		System.out.println("Port: " + port);
		load = load == null ? 1000 : load;
		System.out.println("Load: " + load);
		time = time == null ? 1 : time;
		System.out.println("Time: " + time + " minutes");
		wait = wait == null ? 1 : wait;
		System.out.println("Service Delay: " + wait + " miliseconds");
		
		
		MiddlewareConfig config = new MiddlewareConfig();
		config.setProperty(MiddlewareConfig.SERVER_ENABLED, "false");
		config.setProperty(MiddlewareConfig.NAMING_ENABLED, "false");
		config.setProperty(MiddlewareConfig.NAMING_SERVICE_HOST, host);
		config.setProperty(MiddlewareConfig.NAMING_SERVICE_PORT, ""+port);
		
		System.out.println("Setting configs...");
		
		Middleware m = Middleware.createInstance(config);
		
		System.out.println("Initiating...");
		
		DelayStub delay = null;
		
		try {
			Service endpoint = (Service) m.getNaming().lookup(Delay.SERVICE_NAME).get(0);
			System.out.println("Service "+ Delay.SERVICE_NAME +" found. Creating client...");
			delay = (DelayStub) new ServiceCreator(endpoint).createStub();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final int workload = load;
		
		StatFileWriter statWriter = new StatFileWriter(new File(
				String.format("client-response-times-%04d-%04d.dat",workload,wait)));
		
		AtomicLong successes = new AtomicLong(0);
		long total = 0;
		
		long initial = 0;
		long end = 0;
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		long testDuration = minToMilis(time);
		
		initial = System.currentTimeMillis();
		System.out.println("Starting");
		
		while( true )
		{
			end = System.currentTimeMillis();
			long elapsedTime = end - initial;
			long remainingTime = testDuration - elapsedTime;
			
			if(remainingTime < 0)
			{
				break;
			}

			
			System.out.println(remainingTime + " ms remaining");
			List<ClientTask> tasklist = createWorkloadList(delay, statWriter, successes, workload, wait);
			try {
				executor.invokeAll(tasklist, remainingTime, TimeUnit.MILLISECONDS);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			total += workload;
			System.out.println(total + " jobs completed.");
			
		}
		
		System.out.println("Ended. Now shutting down...");
		
		executor.shutdownNow();
		end = System.currentTimeMillis();
		double durationSec = new Long(end - initial).doubleValue() / 1000;
		System.out.println("Total test duration: " + durationSec + " seconds");
		
		double successesValue = successes.doubleValue();
		System.out.println("Number of completed operations: " + successesValue);
		
		double throughput = successesValue / durationSec ;
		File file = new File(String.format("client-throughtput-%04d.dat", wait));
		
		PrintStream ps = null;
		try
		{
			ps = new PrintStream(new FileOutputStream(file,true));
			ps.println("#############################");
			ps.println("Workload: " + workload);
			ps.println("Throughput: " + throughput);
			ps.println("Successes: " + successesValue);
			ps.println("Total: " + total);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(ps != null)
				ps.close();
		}
		
		statWriter.close();
		m.closeExecutors();
		
	}
}
