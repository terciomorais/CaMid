package br.ufpe.cin.middleware.app;

import java.util.List;
import java.util.Random;

import br.ufpe.cin.middleware.distribution.stub.CalculadoraStub;
import br.ufpe.cin.middleware.naming.Naming;
import br.ufpe.cin.middleware.naming.Service;
import br.ufpe.cin.middleware.naming.ServiceCreator;

public class SingleClientCalc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Naming naming = new Naming("localhost", 5000);
		
		CalculadoraStub calc = null;
		List<Service> endpoints = null;
		Random random = new Random();
		
		try {
			endpoints = naming.lookup("Calculadora");
			for(Service endpoint : endpoints){
				System.out.println("Invoking to " + endpoint.toString());
				calc = (CalculadoraStub)new ServiceCreator(endpoint).createStub();
				System.out.println("Result: " + calc.soma(random.nextInt(100), random.nextInt(100)));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			//System.exit(-1);
		}
	}
}
