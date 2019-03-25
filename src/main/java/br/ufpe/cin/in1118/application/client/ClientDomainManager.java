package br.ufpe.cin.in1118.application.client;

import br.ufpe.cin.in1118.distribution.stub.ResourceControllerStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;

public class ClientDomainManager {

	public static void main(String[] args) {
		NamingStub naming = new NamingStub(args[0], Integer.parseInt(args[1]));
		
		ResourceControllerStub domainManager = (ResourceControllerStub) naming.lookup("management");
		boolean result = domainManager.scaleOut("vm");
		System.out.println("Result " + result);
		System.exit(0);
	}
}
