package br.ufpe.cin.in1118.application.client;

import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.distribution.stub.DelayStub;

public class ClientDelay {
	public static void main(String[] args) {
		NamingStub	naming	= new NamingStub("localhost", 1111);
		DelayStub delay = (DelayStub) naming.lookup("delay");
		delay.delay(10);
		System.out.println("FUNCIONOU");
		System.exit(0);
	}
}
