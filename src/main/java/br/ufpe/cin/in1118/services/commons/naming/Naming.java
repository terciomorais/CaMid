package br.ufpe.cin.in1118.services.commons.naming;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.stub.CloudManagerServiceStub;
import br.ufpe.cin.in1118.distribution.stub.Stub;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.utils.Network;

public class Naming implements INaming {

	private static	Naming			INSTANCE;	
	private Map<String, NameRecord>	repository			= new ConcurrentHashMap<String, NameRecord>();
	private String					feHost				= "localhost";
	private boolean					isFrontEndEnabled	= false;
	private CloudManagerServiceStub	domainManager		= null;
	
	private Naming(){
		if(Broker.getSystemProps().getProperties().containsKey("frontend")
				&& ((String)Broker.getSystemProps().getProperties().get("frontend")).equals("enabled")){
			this.isFrontEndEnabled = true;
			this.feHost = (String) Broker.getSystemProps().getProperties().get("frontend_host");
			Integer.parseInt((String)Broker.getSystemProps().getProperties().get("frontend_port"));
		}
	}
	
	public static Naming getInstance(){
		if (INSTANCE == null)
			INSTANCE = new Naming();
		return INSTANCE;
	}
	
	private void add(String serviceName, NameRecord namingRecord){
		if(!this.repository.containsKey(serviceName))
			this.repository.put(serviceName, namingRecord);
		this.repository.get(serviceName).addRemoteEndPoint(namingRecord.getStub().getEndpoint());
		System.out.println(" ------------------------------------");
		System.out.println("| [Naming] registering service: " + serviceName);
		System.out.println("|         " + this.repository.get(serviceName).getEndPoints().size() + " instances");
		for(EndPoint endpoint : this.repository.get(serviceName).getEndPoints())
			System.out.println("|         endpoint: " + endpoint.getEndpoint());
		System.out.println(" ------------------------------------");
	}
	
	public void removeEndPoint(String serviceName, EndPoint endPoint){
		//this.repository.remove(serviceName);
	}
	
	@Override
	public void bind(String serviceName, Stub stub)
			throws UnknownHostException, IOException, Throwable {
		NameRecord nameRecord = new NameRecord(stub);
		this.add(serviceName, nameRecord);
		if (!(serviceName.equals("management") || serviceName.equals("forward"))
				&& this.isFrontEndEnabled
				&& Network.isReachable(this.feHost, 5000)){
			if(this.domainManager == null)
				this.domainManager = (CloudManagerServiceStub) this.lookup("management");
			
			DispatcherThread dispatcher = new DispatcherThread(this.domainManager, serviceName, this.repository);
			Broker.getExecutorInstance().execute(dispatcher);
		}
	}
	
	public synchronized void unbind(String serviceName){
		this.repository.remove(serviceName);
	}
	
	@Override
	public Stub lookup(String serviceName) throws UnknownHostException,
			IOException, Throwable {
		NameRecord nr = this.repository.get(serviceName);
		return nr.getStub();	
	}

	@Override
	public ArrayList<String> listServiceNames() throws UnknownHostException,
	IOException, Throwable{
		return new ArrayList<String>(this.repository.keySet());
	}

	@Override
	public ArrayList<NameRecord> listRecords() throws UnknownHostException,
			IOException, Throwable {
		return new ArrayList<NameRecord>(this.repository.values());	
	}

	@Override
	public Map<String, NameRecord> getRegistry() throws UnknownHostException,
			IOException, Throwable {
		return this.repository;
	}
}
