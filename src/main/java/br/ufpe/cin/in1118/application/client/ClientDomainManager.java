package br.ufpe.cin.in1118.application.client;

import br.ufpe.cin.in1118.distribution.stub.CloudManagerServiceStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.management.analysing.Analysis;

public class ClientDomainManager {

	public static void main(String[] args) {
		NamingStub naming = new NamingStub(args[0], Integer.parseInt(args[1]));
		
		CloudManagerServiceStub domainManager = (CloudManagerServiceStub) naming.lookup("management");
		domainManager.alert(new Analysis("CPU overload"));
		//System.out.println("Result " + result);
		System.exit(0);
	}
}
