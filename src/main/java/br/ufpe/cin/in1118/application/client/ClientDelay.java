package br.ufpe.cin.in1118.application.client;

import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.distribution.stub.DelayStub;

public class ClientDelay {
	public static void main(String[] args) {
		NamingStub	naming	= new NamingStub(args[0], Integer.parseInt(args[1]));
		DelayStub delay = (DelayStub) naming.lookup("delay");
		System.out.println("\n[ClientDelay:10] Delay stub"
							+ "\n                   Frontend host	: " + delay.getFeHost()
							+ "\n                   Frontend port	: " + delay.getFePort()
							+ "\n                   Target host		: " + delay.getHost()
							+ "\n                   Tagret port		: " + delay.getPort()
							+ "\n                   Frontend enabled	: " + delay.isForwarded());
		delay.delay(Integer.parseInt(args[2]));
		System.out.println("FUNCIONOU");
		System.exit(0);
	}
}
