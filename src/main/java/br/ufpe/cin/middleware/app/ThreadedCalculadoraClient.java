package br.ufpe.cin.middleware.app;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufpe.cin.middleware.distribution.stub.CalculadoraStub;
import br.ufpe.cin.middleware.naming.Naming;
import br.ufpe.cin.middleware.naming.NamingService;
import br.ufpe.cin.middleware.naming.Service;
import br.ufpe.cin.middleware.naming.ServiceCreator;

public class ThreadedCalculadoraClient {

	public static void main(String[] args) {

		ExecutorService executor = Executors.newFixedThreadPool(8);

		System.out.println( "\033[H\033[2J" );

		Naming naming = new Naming("127.0.0.1", NamingService.PORT);

		
		List<Service> endpoints = null;
		final Random random = new Random();
		try {
			endpoints = naming.lookup("Calculadora");
			for(Service endpoint : endpoints)
			{
				System.out.println(endpoint.toString());
				final CalculadoraStub calc = (CalculadoraStub)new ServiceCreator(endpoint).createStub();

				for(int i = 0; i < 1000000; i++)
				{	
					Runnable runnable = new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								int a = random.nextInt(100);
								int b = random.nextInt(100);
								
								int soma = calc.soma(a, b);
								int sub = calc.sub(a, b);
								System.out.println("RESPOSTA FINAL " + soma + " ; Success? " + (soma == a+b));
								System.out.println("RESPOSTA FINAL " + sub + " ; Success? " + (sub == a-b));
							}
							catch(Exception e)
							{
								System.err.println(e.getMessage());

							}

						}
					};
					
					executor.execute(runnable);
					
					
				}
				
				
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		
	}
	
}
