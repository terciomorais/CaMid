package br.ufpe.cin.in1118.distribution.frontend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.distribution.stub.Stub;
import br.ufpe.cin.in1118.management.Adaptor;
import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.management.node.NodeManager;
import br.ufpe.cin.in1118.services.commons.naming.LocalServiceRegistry;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.utils.Network;
import br.ufpe.cin.in1118.utils.PropertiesSetup;
import br.ufpe.cin.in1118.utils.Timer;

public class FrontEnd {

	private static FrontEnd			INSTANCE		= null;
	private Broker 					broker			= null;
	private PropertiesSetup			properties		= new PropertiesSetup("config/frontend.config");
	private String					host			= Network.recoverAddress("localhost");
	private int						port			= 1212;
	private EndPoint				endpoint		= null;
	private Map<String, NameRecord>	serviceList		= null;
	private Set<EndPoint>			nodes			= null;
	private Set<EndPoint>			clouds			= null;
	private int						maxNodes		= 0;
	private Adaptor					adaptor			= null;
	private Timer					reactionTime	= null;

	private LocalServiceRegistry	registry	= LocalServiceRegistry.getINSTANCE("config/remote_front.properties");
	
	private FrontEnd(){
		this.endpoint = new EndPoint(this.host, this.port);
		this.registry.create();
	}
	
	private FrontEnd(String props){
		this.properties = new PropertiesSetup(props);
		this.endpoint = new EndPoint(this.host, this.port);
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
		System.out.println("\n--------------------------------------\n[FrontEnd:90] Service list updated:");
		for(Map.Entry<String, NameRecord> rec: this.serviceList.entrySet()){
			System.out.println("        Service " + rec.getValue().getStub().getServiceName());
			for(EndPoint ep : rec.getValue().getEndPoints())
				System.out.println("            endpoint " + ep.getEndpoint());
		}
		System.out.println("--------------------------------------\n");

		this.addAllNodes(record.getEndPoints());

		if(NodeManager.getInstance().getObjectAnalyser().isPaused() || NodeManager.getInstance().getObjectAnalyser().isAfterAdaptation()){
			//Calculating reaction time for VM level elasticity
			FrontEnd.getInstance().getReactionTime().setEndTime(System.currentTimeMillis());
			System.out.println("\n-----------------------------------------------------------------------------");
			System.out.println("[FrontEnd:107] Reaction time for application level elasticity: " + FrontEnd.getInstance().getReactionTime().getElapsedTime());
			System.out.println("-------------------------------------------------------------------------------\n");
			NodeManager.getInstance().getObjectAnalyser().setPaused(false);
		}
	}
	
	public void removeService(String service){
		this.serviceList.remove(service);
		System.out.println("\n--------------------------------------\n  [FrontEnd:114] Service list updated:");
		for(Map.Entry<String, NameRecord> rec: this.serviceList.entrySet()){
			System.out.println("[Frontend:120] service " + rec.getValue().getStub().getServiceName());
			for(EndPoint ep : rec.getValue().getEndPoints())
				System.out.println("            endpoint " + ep.getEndpoint());
		}
		System.out.println("--------------------------------------\n");

		if(NodeManager.getInstance().getObjectAnalyser() != null && (NodeManager.getInstance().getObjectAnalyser().isPaused()
				|| NodeManager.getInstance().getObjectAnalyser().isAfterAdaptation())){
			//Calculating reaction time for VM level elasticity
			FrontEnd.getInstance().getReactionTime().setEndTime(System.currentTimeMillis());
			System.out.println("\n-----------------------------------------------------------------------------");
			System.out.println("[FrontEnd:128] Reaction time for application level elasticity: " + FrontEnd.getInstance().getReactionTime().getElapsedTime());
			System.out.println("-------------------------------------------------------------------------------\n");
			NodeManager.getInstance().getObjectAnalyser().setPaused(false);
		}
	}

	public void updateServices(){
		String hostNS 	= Network.recoverAddress(this.getProperties().getProperties().getProperty("naming_host"));
		int portNS		= Integer.parseInt(this.getProperties().getProperties().getProperty("naming_port"));
		NamingStub name = null;
		
		try {
			name = new NamingStub(hostNS, portNS);
			this.serviceList = name.getRegistry();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\n ------------------------------------------------------------------------------");
		System.out.println("| [FrontEnd:135] Service list is DONE:");
		for(Map.Entry<String, NameRecord> record : this.serviceList.entrySet()){

			System.out.println("                service " + record.getValue().getStub().getServiceName());
			for(EndPoint ep : record.getValue().getEndPoints())
				System.out.println("                      endpoint " + ep.getEndpoint());
		}
		System.out.println(" ------------------------------------------------------------------------------\n");
		
/* 		if(NodeManager.getInstance() != null && NodeManager.getInstance().getObjectAnalyser() != null && NodeManager.getInstance().getObjectAnalyser().isPaused()){
			//Calculating reaction time for VM level elasticity
			FrontEnd.getInstance().getReactionTime().getEndTime();
			System.out.println("\n-----------------------------------------------------------------------------");
			System.out.println("[FrontEnd:161] Reaction time for application level elasticity: " + FrontEnd.getInstance().getReactionTime());
			System.out.println("-------------------------------------------------------------------------------\n");
			NodeManager.getInstance().getObjectAnalyser().setPaused(false);
		} */
	}

	public void addAllNodes(Set<EndPoint> endpoints){
		if(this.nodes == null)
			this.nodes = new HashSet<EndPoint>();
		if(endpoints.contains(this.getEndpoint()))
			endpoints.remove(this.getEndpoint());
		if(!endpoints.isEmpty())
			this.nodes.addAll(endpoints);
	}

	public void addNode(EndPoint endpoint){
		if(this.nodes == null)
			this.nodes = new HashSet<EndPoint>();
		if(!endpoint.equals(this.getEndpoint()))
			this.nodes.add(endpoint);
	}

	public Set<EndPoint> getNodes(){
		return this.nodes;
	}

	public EndPoint getEndpoint() {
		return this.endpoint;
	}

	public void setEndpoint(EndPoint endpoint) {
		this.endpoint = endpoint;
	}


	public Set<EndPoint> getClouds() {
		return this.clouds;
	}

	public void setClouds(Set<EndPoint> clouds) {
		this.clouds = clouds;
	}

	public void setClouds(){
		if(this.properties.getProperties().contains("cloud_list")){
			String[] ep = ((String)this.properties.getProperties().get("cloud_list")).split(";");
			for(int idx = 0; idx < ep.length; idx++)
				this.addCloud(new EndPoint(ep[idx]));
		} else {
			this.setClouds(new HashSet<EndPoint>());
		}
	}

	public void addCloud(EndPoint cloud){
		if(this.clouds == null)
			this.clouds = new HashSet<EndPoint>();
		this.clouds.add(cloud);
	}

	public int getMaxNodes() {
		return this.maxNodes;
	}

	public void setMaxNodes() {
		if(FrontEnd.getInstance().getProperties().getProperties().contains("max_vms")){
			this.maxNodes = (Integer) FrontEnd.getInstance().getProperties().getProperties().get("max_vms");
		} else
			this.maxNodes = -1;
	}

	public Adaptor getAdaptor(Analysis analysis) {
		if(this.adaptor == null)
			this.adaptor = new Adaptor(analysis);
		else
			this.adaptor.setAnalysis(analysis);

		return this.adaptor;
	}

	public void setAdaptor(Adaptor adaptor) {
		this.adaptor = adaptor;
	}

	public Timer getReactionTime() {
		if(this.reactionTime == null)
			this.reactionTime = new Timer();
		return this.reactionTime;
	}

	public void setReactionTime(Timer reactionTime) {
		this.reactionTime = reactionTime;
	}

	public void start(){
		this.publishServices();
		this.updateServices();
		this.setClouds();
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
				method.invoke(stub, this.port);
				
				method = clazz.getMethod("setObjectId", new Class[]{Integer.TYPE});
				method.invoke(stub, 1);
				
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
