package br.ufpe.cin.in1118.application.client;

import br.ufpe.cin.in1118.distribution.stub.DomainManagerStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;

public class ClientDelay {

	public static void main(String[] args) {
		NamingStub	naming	= new NamingStub(args[0], Integer.parseInt(args[1]));
/*		
		for(EndPoint ep : naming.getRegistry().get("management").getEndPoints())
			System.out.println("Management #1 " + ep.getEndpoint());
		DomainManagerStub dm = (DomainManagerStub) naming.getRegistry().get("management").getStub();
		dm.scaleOut("vm");
		System.out.println("Management #2 " + dm.getFeHost());
		*/
		//DelayStub	delay	= (DelayStub) naming.lookup("delay");
/*		NameRecord nr = new NameRecord(naming);
		nr.addRemoteEndPoint("localhost:8080");
		nr.addRemoteEndPoint("192.168.1.254:80");
		nr.setStub(new DomainManagerStub());
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream("filename"));
			oos.writeObject(nr);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
*/		
		long ini = System.currentTimeMillis();
	//	delay.delay(500);
	//	delay.delay(500);
		System.out.println("Time for request service of 500ms: "
				+ (System.currentTimeMillis() - ini) + " ms");
		DomainManagerStub manager	= (DomainManagerStub) naming.lookup("management");
		manager.scaleOut("vm");
		System.exit(0);
	}
}
