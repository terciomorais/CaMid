package br.ufpe.cin.in1118.management.monitoring;

import java.util.Iterator;
import java.util.Map;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventSourceFactory {

	private static EventSourceFactory INSTANCE;
	
	protected Map<String,Observer> observers;
	
	protected boolean enabled;
	
	public EventSourceFactory() {
		this.observers = new ConcurrentHashMap<String,Observer>();
	}
	
	public void registerObserver(String key, Observer observer) {
		
		System.out.println("| [EventSourceFactory:22] Registering Monitor Observer for " + key.substring(key.lastIndexOf('.') + 1) + " service.");
		
		this.observers.put(key, observer);
	}
	
	public void unregisterObserver(String key) {
		this.observers.remove(key);
	}
	
	public void addObservers(EventSource eventSource) {
		Set<String> keys = this.observers.keySet();
		for(Iterator<String> it = keys.iterator(); it.hasNext();) {
			eventSource.addObserver(this.observers.get(it.next()));;
		}
	}
	
	public Map<String, Observer> getObservers(){
		return this.observers;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public synchronized static EventSourceFactory getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new EventSourceFactory();
		}
		return INSTANCE;
	}
	
	public EventSource createEventSource() {
		EventSource es = new EventSource();
		this.addObservers(es);
		return es;
	}
}
