package br.ufpe.cin.in1118.application.client;

import br.ufpe.cin.in1118.distribution.stub.DelayStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;

public class ThreadTest {

	public static void main(String[] args) {
		String 	host		= args[0];
		int		port		= Integer.parseInt(args[1]);	
		int		serviceTime	= Integer.parseInt(args[2]);
		long 	b			= 0;
		long	e			= 0;
/*		
		System.out.println("\n -----------------------------------------------------------");
		System.out.println("|      Begining of all experiment service time " + serviceTime + " ms       |");
		System.out.println(" -----------------------------------------------------------");
*/
		NamingStub	naming	= new NamingStub(host, port);
		DelayStub	delay	= (DelayStub) naming.lookup("delay");

		
		//ramp up
		for(int i = 0; i < 20; i++){
			b = System.currentTimeMillis();
			delay.delay(serviceTime); // this class creates a socket and sends a request
			e = System.currentTimeMillis();
			System.out.println("Elapsed time " + ((e - b)) + "ms");
		}
	}

}
