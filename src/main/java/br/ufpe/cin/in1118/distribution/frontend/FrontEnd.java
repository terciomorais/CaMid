package br.ufpe.cin.in1118.distribution.frontend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.distribution.stub.Stub;
import br.ufpe.cin.in1118.services.commons.naming.LocalServiceRegistry;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.utils.Network;
import br.ufpe.cin.in1118.utils.PropertiesSetup;

public class FrontEnd {

	private static FrontEnd			INSTANCE	= null;
	private Broker 					broker		= null;
	private PropertiesSetup			properties	= new PropertiesSetup("config/frontend.config");
	private String					host		= Network.recoverAddress("localhost");
	private int						port		= 1212;
	private Map<String, NameRecord>	serviceList	= null;
	private LocalServiceRegistry	registry	= LocalServiceRegistry
			.getINSTANCE("config/remote_front.properties");
	
	private FrontEnd(){
		this.registry.create();
	}
	
	private FrontEnd(String props){
		this.properties = new PropertiesSetup(props);
		this.registry.create();
	}
	
	public static FrontEnd getInstance(){
		if(INSTANCE == null){
			INSTANCE = new FrontEnd();
		}
		return INSTANCE;
	}
	
	public static FrontEnd getInstance(String props){

		if(INSTANCE == null){
			INSTANCE = new FrontEnd(props);
		}
		return INSTANCE;
	}
	
	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

	public PropertiesSetup getProperties() {
		return properties;
	}

	public void setProperties(PropertiesSetup properties) {
		this.properties = properties;
	}

	public Map<String, NameRecord> getServices(){
		if (this.serviceList == null)
			this.updateServices();
		return this.serviceList;
	}
	
	public NameRecord getService(String serviceName){
		return this.getServices().get(serviceName);
	}
	
	public void addService(String service, NameRecord record){
		this.serviceList.put(service, record);
		System.out.println("[FrontEnd] Service list updated:");
		for(Map.Entry<String, NameRecord> rec: this.serviceList.entrySet()){
			System.out.println("[Frontend:95] service " + rec.getValue().getStub().getServiceName());
			for(EndPoint ep : rec.getValue().getEndPoints())
				System.out.println("            endpoint " + ep.getEndpoint());
		}
	}
	
	public void updateServices(){
		String hostNS 	= Network.recoverAddress(this.getProperties().getProperties().getProperty("naming_host"));
		int portNS		= Integer.parseInt(this.getProperties().getProperties().getProperty("naming_port"));
		NamingStub name = null;
		
		System.out.println("[FrontEnd] Building service list ...");
		
		try {
			name = new NamingStub(hostNS, portNS);
			this.serviceList = name.getRegistry();
//			System.out.println(this.serviceList.get("management").getStub().getClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("[FrontEnd] Service list is DONE:");
/*		for(Map.Entry<String, NameRecord> record: this.serviceList.entrySet()){
			System.out.println("[Frontend:95] service " + record.getValue().getStub().getServiceName());
			for(EndPoint ep : record.getValue().getEndPoints())
				System.out.println("[Frontend:99] endpoint " + ep.getEndpoint());
		}
*/	}

	public void start(){
		this.publishServices();
		this.updateServices();
		this.setBroker(new Broker("config/frontend.config"));
		this.getBroker().start();
		System.exit(0);
	}

	private void publishServices() {
		String temp 	= this.getProperties().getProperties().getProperty("naming_host");
		String hostNS 	= Network.recoverAddress(temp);
		int portNS		= Integer.parseInt(this.getProperties().getProperties().getProperty("naming_port"));
		NamingStub name = new NamingStub(hostNS, portNS);
		
		System.out.println("[FrontEnd] Binding service on NameService at " + hostNS);
		
		Set<String> serviceNames = this.registry.getAllServiceNames();
		Stub stub = null;
		
		for(String serviceName : serviceNames){
			String className = "br.ufpe.cin.in1118.distribution.stub."
					+ this.registry.getRemoteObjectClass(serviceName).getSimpleName()
					+ "Stub";
			try {
				System.out.println("[Broker] Registering service " + serviceName + ": " + className);
				Class<?> clazz = Class.forName(className);
				stub = (Stub) clazz.getDeclaredConstructor().newInstance();
				
				Method method = clazz.getMethod("setServiceName", new Class[]{String.class});
				method.invoke(stub, serviceName);
				
				method = clazz.getMethod("setHost", new Class[]{String.class});
				method.invoke(stub, this.host);
				
				method = clazz.getMethod("setPort", new Class[]{Integer.TYPE});
				method.invoke(stub, new Integer(this.port));
				
				method = clazz.getMethod("setObjectId", new Class[]{Integer.TYPE});
				method.invoke(stub, new Integer(1));
				
				method = clazz.getMethod("setClassName", new Class[]{String.class});
				method.invoke(stub, this.registry.getRemoteObjectClass(serviceName).getCanonicalName());
				
		        name.bind(serviceName, stub);
		        
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}		
	}
}
