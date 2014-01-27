package br.ufpe.cin.middleware.app;

import java.util.List;
import java.util.Random;
import br.ufpe.cin.middleware.distribution.stub.CalculadoraStub;
import br.ufpe.cin.middleware.distribution.stub.LocalAgentStub;
import br.ufpe.cin.middleware.naming.Naming;
import br.ufpe.cin.middleware.naming.Service;
import br.ufpe.cin.middleware.naming.ServiceCreator;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.ClientInvocationContext;

public class CalculadoraClient {

	public static void main(String[] args) {
		//System.out.println( "\033[H\033[2J" );

		Naming naming = new Naming("localhost", 5000);
		
		CalculadoraStub calc = null;
		List<Service> endpoints = null;
		Random random = new Random();
		try {
			endpoints = naming.lookup("Calculadora");
			for(Service endpoint : endpoints){
				System.out.println(endpoint.toString());
				calc = (CalculadoraStub)new ServiceCreator(endpoint).createStub();
				
				for(int i = 0; i < 100000; i++)
				{	
					
					try {
						System.out.println("RESPOSTA FINAL " + calc.soma(random.nextInt(100), random.nextInt(100)));
						System.out.println("RESPOSTA FINAL " + calc.sub(random.nextInt(100), random.nextInt(100)));
					}
					catch(Exception e)
					{
						System.err.println(e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
		
		LocalAgentStub las = null;
		
//		try {
		try {
			endpoints = naming.lookup("LocalAgent");
			for(Service endpoint : endpoints)
			{
				System.out.println(endpoint);
				las = (LocalAgentStub) new ServiceCreator(endpoint).createStub();
			
				
				System.out.println( "NULL ? " + (las == null));
				System.out.println("Requests per Second (Invoker): " + las.getRequestsPerSecondMetricForInvoker());
				
			
				
				System.out.println( "NULL ? " + (las == null));
				System.out.println("Response time (ms) (Invoker): " + las.getResponseTimeMetricForInvoker());
				
			
						
				System.out.println( "NULL ? " + (las == null));
				System.out.println("Response time (ms) (Calculadora::soma): " + las.getResponseTimeMetricForRemoteObject(
					new ClientInvocationContext("Calculadora","soma").toString()));
				
			
				
				System.out.println( "NULL ? " + (las == null));
				System.out.println("Requests per second (Calculadora::soma): " + las.getRequestsPerSecondMetricForRemoteObject(
					new ClientInvocationContext("Calculadora","soma").toString()));
			
		
//		try 
//		{
//			System.out.println( "NULL ? " + (las == null));
//			System.out.println("Failure Ratio (Invoker): " + las.getFailureRatioForInvoker());	
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			System.exit(-1);
//		}
		
		
		
				System.out.println( "NULL ? " + (las == null));
				System.out.println("Failure Ratio (Calculadora::soma): " + las.getFailureRatioForRemoteObject(
					new ClientInvocationContext("Calculadora","soma").toString()));	
			
				System.out.println( "NULL ? " + (las == null));
				System.out.println("CPU Usage: " + las.getCPUUsage());	
			
				System.out.println( "NULL ? " + (las == null));
				System.out.println("Mem Usage: " + las.getCPUUsage());
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
			
		}	
		
		System.out.println("Sucesso!");
	}
}