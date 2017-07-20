package br.ufpe.cin.in1118.application.client;

import br.ufpe.cin.in1118.application.remoteObject.Calculator;
import br.ufpe.cin.in1118.application.remoteObject.Delay;
import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.stub.CalculatorStub;
import br.ufpe.cin.in1118.distribution.stub.DelayStub;
import br.ufpe.cin.in1118.distribution.stub.DomainManagerStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.infrastructure.client.ClientRequestHandler;
import br.ufpe.cin.in1118.infrastructure.client.ClientSender;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.protocols.communication.MessageBody;
import br.ufpe.cin.in1118.protocols.communication.MessageHeader;
import br.ufpe.cin.in1118.protocols.communication.Parameter;
import br.ufpe.cin.in1118.protocols.communication.RequestBody;
import br.ufpe.cin.in1118.protocols.communication.RequestHeader;

public class ClientDelay {

	public static void main(String[] args) {
		
/*		MessageHeader header	= new MessageHeader("request", 0, false, 1, 0);
		RequestHeader reqHeader	=
				new RequestHeader("br.ufpe.cin.in1118.management.node.NodeManagerService", 0, false, 0, "NodeManagerService", "addService");
		Parameter[] params		= {new Parameter("serviceName", String.class, "delay"),
				new Parameter("className", Class.class, Delay.class)};
		RequestBody reqBody		= new RequestBody(params);
		MessageBody body		= new MessageBody(reqHeader, reqBody, null, null);
		Message message			= new Message(header, body);
		
		ClientSender sender		= new ClientSender("localhost", 1313, message);
		ClientRequestHandler.getInstance().submit(sender);*/

		//Broker.getExecutorInstance().submit(sender);
/*		NamingStub	naming	= new NamingStub(args[0], Integer.parseInt(args[1]));
		
		for(EndPoint ep : naming.getRegistry().get("management").getEndPoints())
			System.out.println("Management #1 " + ep.getEndpoint());
		DomainManagerStub dm = (DomainManagerStub) naming.getRegistry().get("management").getStub();
		dm.scaleOut("vm");
		System.out.println("Management #2 " + dm.getFeHost());
		
		//DelayStub	delay	= (DelayStub) naming.lookup("delay");
		NameRecord nr = new NameRecord(naming);
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
		
		long ini = System.currentTimeMillis();
		delay.delay(500);
		delay.delay(500);
		System.out.println("Time for request service of 500ms: "
				+ (System.currentTimeMillis() - ini) + " ms");
		DomainManagerStub manager	= (DomainManagerStub) naming.lookup("management");
		manager.scaleOut("app");*/
		NamingStub	naming	= new NamingStub("10.66.66.41", 1111);
		CalculatorStub delay = (CalculatorStub) naming.lookup("calculator");
		System.out.println("9 + 13 = " + delay.add(9, 13));
		//delay.delay(Integer.parseInt(args[0]));
		System.exit(0);
	}
}
