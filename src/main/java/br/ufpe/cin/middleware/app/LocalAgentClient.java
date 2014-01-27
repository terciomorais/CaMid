package br.ufpe.cin.middleware.app;

import java.util.List;

import br.ufpe.cin.middleware.distribution.stub.LocalAgentStub;
import br.ufpe.cin.middleware.naming.Naming;
import br.ufpe.cin.middleware.naming.NamingService;
import br.ufpe.cin.middleware.naming.Service;
import br.ufpe.cin.middleware.naming.ServiceCreator;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.ClientInvocationContext;



public class LocalAgentClient {

	public static void main(String[] args) {
		LocalAgentStub las = null;
		
//		Naming naming = new Naming("localhost", NamingService.PORT);
		Naming naming = new Naming("10.0.0.1", 5000);
		
		List<Service> endpoints = null;
//		try {
		try {
			endpoints = naming.lookup("LocalAgent");
			Service endpoint = endpoints.get(0);
			while(true)
			{
				System.out.println(endpoint);
				las = (LocalAgentStub) new ServiceCreator(endpoint).createStub();
				
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Requests per Second (Invoker): " + las.getRequestsPerSecondMetricForInvoker());
			
				
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Response time (ms) (Invoker): " + las.getResponseTimeMetricForInvoker());
				
			
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Response time (ms) (Calculadora): " + las.getResponseTimeMetricForRemoteObject(
					new ClientInvocationContext("Calculadora",null).toString()));
				
			
				
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Requests per second (Calculadora): " + las.getRequestsPerSecondMetricForRemoteObject(
					new ClientInvocationContext("Calculadora",null).toString()));
				
						
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Response time (ms) (Calculadora::soma): " + las.getResponseTimeMetricForRemoteObject(
					new ClientInvocationContext("Calculadora","soma").toString()));
				
			
				
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Requests per second (Calculadora::soma): " + las.getRequestsPerSecondMetricForRemoteObject(
					new ClientInvocationContext("Calculadora","soma").toString()));
				
				
			
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Response time (ms) (Calculadora::sub): " + las.getResponseTimeMetricForRemoteObject(
					new ClientInvocationContext("Calculadora","sub").toString()));
				
			
				
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Requests per second (Calculadora::soma): " + las.getRequestsPerSecondMetricForRemoteObject(
					new ClientInvocationContext("Calculadora","sub").toString()));
			
		
//		try 
//		{
//			System.out.println( "NULL ? " + (las == null));
//			System.out.println("Failure Ratio (Invoker): " + las.getFailureRatioForInvoker());	
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			System.exit(-1);
//		}
		
		
		
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Failure Ratio (Calculadora::soma): " + las.getFailureRatioForRemoteObject(
					new ClientInvocationContext("Calculadora","soma").toString()));	
				
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Failure Ratio (Calculadora::sub): " + las.getFailureRatioForRemoteObject(
					new ClientInvocationContext("Calculadora","sub").toString()));
			
				
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("CPU Usage: " + las.getCPUUsage()*100);	
			
//				System.out.println( "NULL ? " + (las == null));
				System.out.println("Mem Usage: " + las.getMemUsage());
				
				Thread.sleep(1000);
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
			
		}	
		
		System.out.println("Sucesso!");
	}
	
}
