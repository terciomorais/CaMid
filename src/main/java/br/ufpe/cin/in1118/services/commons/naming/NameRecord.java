package br.ufpe.cin.in1118.services.commons.naming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import br.ufpe.cin.in1118.distribution.stub.Stub;
import br.ufpe.cin.in1118.management.SchedullerRoundRobin;
import br.ufpe.cin.in1118.utils.EndPoint;

public class NameRecord implements Serializable {

	private static final long serialVersionUID 	= 1L;
	private Set<EndPoint>			endpoints	= Collections.synchronizedSet(new HashSet<EndPoint>());
	private Stub					stub		= null;
	private SchedullerRoundRobin	schduller	= null;
	
	public NameRecord(Stub stub){
		this.setStub(stub);
	}
	
	public Set<EndPoint> getEndPoints() {
		return endpoints;
	}
	
	public void setRemoteEndPoints(Set<EndPoint> endPoints) {
		this.endpoints = endPoints;
	}
	
	public void addRemoteEndPoint(String endpoint){
		EndPoint end = new EndPoint(endpoint);
		if(this.endpoints.isEmpty())
			this.endpoints.add(end);
		else{
		Iterator<EndPoint> it = this.endpoints.iterator();
		EndPoint ep = null;
		
		while (it.hasNext()){
			ep = it.next();
			if(end.equals(ep))
				break;	
		}
		if(!end.equals(ep))
			this.endpoints.add(end);
		}
	}
	
	public void removeRemoteEndPoint(String endpoint){
		Iterator<EndPoint> it = this.endpoints.iterator();
		while(it.hasNext())
			if(it.next().getEndpoint().equals(endpoint))
				it.remove();
	}

	public Stub getStub() {
		return this.stub;
	}
		
	public void setStub(Stub stub) {
		this.stub = stub;
	}

	public SchedullerRoundRobin getSchduller() {
		if (this.schduller == null)
			this.schduller = new SchedullerRoundRobin(new ArrayList<EndPoint>(this.endpoints));
		return schduller;
	}
}