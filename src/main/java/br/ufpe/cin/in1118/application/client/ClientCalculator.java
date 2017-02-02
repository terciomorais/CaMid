package br.ufpe.cin.in1118.application.client;

import java.io.IOException;
import java.net.UnknownHostException;

import br.ufpe.cin.in1118.distribution.stub.CalculatorStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;

public class ClientCalculator {

	public static void main(String[] args) throws UnknownHostException, IOException, Throwable {

		NamingStub naming = new NamingStub(args[0], Integer.parseInt(args[1]));
		
		CalculatorStub calc = (CalculatorStub) naming.lookup("calculator");

		System.out.println("4 + 5 = " + calc.add(4, 5));
		
		System.exit(0);
	}

}
