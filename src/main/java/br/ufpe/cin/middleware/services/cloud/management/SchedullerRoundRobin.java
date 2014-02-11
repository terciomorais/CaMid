package br.ufpe.cin.middleware.services.cloud.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import br.ufpe.cin.middleware.naming.Service;

public class SchedullerRoundRobin {

	private String currentService					= null;
	private Map<String, List<Service>> endpoints	= null;
	private int next								= 0;
	
	public SchedullerRoundRobin (String serviceName, List<Service> services){
		this.setCurrentService(serviceName);
		endpoints = new HashMap<String, List<Service>>();
		this.updateEndpoint(serviceName, services);
	}

	public String getCurrentService() {
		return this.currentService;
	}

	public void setCurrentService(String serviceName) {
		this.currentService = serviceName;
	}

	public Map<String, List<Service>> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(Map<String, List<Service>> endpoints) {
		this.endpoints = endpoints;
	}

	public int getNext() {
		return next;
	}

	private void setNext(int next) {
		this.next = next;
	}
	
	public Service getNextEndPoint(){
		if ((this.getEndpoints() != null) && !this.getEndpoints().isEmpty()){
			this.setNext((this.getNext() + 1) %
					this.getEndpoints().get(this.getCurrentService()).size());
			return this.getEndpoints().get(this.currentService).get(this.getNext());
		} else{
			return null;
		}
	}
	
	public synchronized void updateEndpoint(String serviceName, List<Service> services){
		if(this.getEndpoints().get(serviceName) == null){
			this.getEndpoints().put(serviceName, services);
		} else{
			List<Service> serviceAuxList = new ArrayList<Service>(services);
			this.getEndpoints().get(serviceName).clear();
			this.getEndpoints().get(serviceName).addAll(serviceAuxList);
		}
	}

	public synchronized void addService(String servicename, List<Service> endpointlist){
		
	}
	public synchronized void remService(String servicename, List<Service> endpointlist){
		
	}
	public synchronized void addEndpoint(Service endpoint){
		
	}
	
	public synchronized void remEndpoint(Service endpoint){
		
	}
}
