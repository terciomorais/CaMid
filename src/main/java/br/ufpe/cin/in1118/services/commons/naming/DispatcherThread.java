package br.ufpe.cin.in1118.services.commons.naming;

import java.util.Map;

import br.ufpe.cin.in1118.distribution.stub.DomainManagerStub;

public class DispatcherThread implements Runnable {
	private DomainManagerStub 		domainManager	=	null;
	private String 					serviceName 	= "";
	private Map<String, NameRecord> repository		= null;
	
	public DispatcherThread(DomainManagerStub domainManager, String serviceName, Map<String, NameRecord> repository){
		this.domainManager	= domainManager;
		this.serviceName 	= serviceName;
		this.repository		= repository;
	}
	@Override
	public void run() {
		this.domainManager.addService(serviceName, repository.get(serviceName));
	}
}
