package br.ufpe.cin.in1118.management.monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

public class Agent implements Observer{
	private String name;
	private String context;
	private Map<String, List<Event>> servicesEvents	= new ConcurrentHashMap<String, List<Event>>();
	
	public Agent(){}
	
	/* The usage of the attribute context only work for the context forward because */
	public Agent(String name){
		this.setName(name.toLowerCase());
		if(this.name.indexOf("manag") != -1){
			this.context = "management";
		} else if (this.name.equals("forward")){
			this.context = "forward";
		} else{
			this.context = "request";
		}
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getContext(){
		return this.context;
	}

/* 	public void setContext(String context){
		this.context = context;
	} */

	public Map<String, List<Event>> getServiceEvents() {
		return this.servicesEvents;
	}
	
	// public List<Event> getEvents() {
		// 	return events;
		// }
		
		public List<Event> getEvents(String service){
			return this.getServiceEvents().get(service);
		}
		
		public void clearEvents(){
			for(String st : this.servicesEvents.keySet()){
				this.servicesEvents.get(st).clear();
			}
		}
		
		@Override
		public void update(Observable arg0, Object arg1) {			
			if (((Event)arg1).getContext().equals(this.getContext())){
				if (this.servicesEvents.containsKey(((Event)arg1).getService())){
					this.servicesEvents.get(((Event)arg1).getService()).add((Event) arg1); //Add a new event to a service
			} else {
					List<Event> events = new ArrayList<Event>();
					events.add((Event) arg1);
					this.servicesEvents.put(((Event)arg1).getService(), events);
				}

			}
		}
	}
