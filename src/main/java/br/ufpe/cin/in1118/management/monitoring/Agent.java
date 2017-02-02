package br.ufpe.cin.in1118.management.monitoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Agent implements Observer{
	private List<Event> events = Collections.synchronizedList(new ArrayList<Event>());

	public List<Event> getEvents() {
		return events;
	}
	
	public void clearEvents(){
		this.events.clear();
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		this.events.add((Event) arg1);
	}
}
