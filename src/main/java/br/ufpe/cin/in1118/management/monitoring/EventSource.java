package br.ufpe.cin.in1118.management.monitoring;

import java.util.Observable;

public class EventSource extends Observable {

	protected Event event = null;
	
	public EventSource(){}
	
	public EventSource(Event event) {
		this.event = event;
	}
	
	protected void finalize() {
		this.deleteObservers();
	}

	public void startTimer() {
		long startTime = System.nanoTime();
		this.getEvent().setStartTime(startTime);
	}
	
	public void stopTimer() {
		long endTime = System.nanoTime();
		this.getEvent().setEndTime(endTime);
	}
	
	public void startForthTime(){
		this.getEvent().setInitForthTime(System.nanoTime());
	}
	
	public void stopBackTime(){
		this.getEvent().setEndForthTime(System.nanoTime());
	}
	
	public void startBackTime(){
		this.getEvent().setInitForthTime(System.nanoTime());
	}
	
	public void stopForthTime(){
		this.getEvent().setEndForthTime(System.nanoTime());
	}
	public void notifyObservers() {
		this.setChanged();
		this.notifyObservers(this.getEvent());
	}

	public void setException(Exception e)  {
		this.getEvent().setException(e);
	}
	
	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
