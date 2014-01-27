package br.ufpe.cin.middleware.naming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NamingServiceResponse implements Serializable{

	private static final long serialVersionUID = -720243718476590349L;
	
	private List<Service> serviceEndpoints;
	
	private Map<String, Set<Service>> map;
	
	public NamingServiceResponse()	{
		this.serviceEndpoints = new ArrayList<Service>();
	}

	public List<Service> getServiceEndpoints() {
		return this.serviceEndpoints;
	}
	
	public Map<String, Set<Service>> getServiceMap(){
		return this.map;
	}
}
