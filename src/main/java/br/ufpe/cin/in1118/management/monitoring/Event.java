package br.ufpe.cin.in1118.management.monitoring;


public class Event implements Comparable<Event>{
	
	private String		name		= "";
	private long 		timeStamp;
	private String 		source		= "";
	private String 		service		= "";
	private long 		initForthTime;
	private long 		initBackTime;
	private long 		endForthTime;
	private long 		endBackTime;
	private long 		startTime;
	private long 		endTime;
	private boolean 	success;
	private Exception	exception;
	
	public Event() {
		this.setTimeStamp(System.currentTimeMillis());
	}
	
	public Event(String source){
		this.setTimeStamp(System.currentTimeMillis());
		this.setSource(source);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	private void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	public long getInitForthTime() {
		return initForthTime;
	}

	public void setInitForthTime(long forthTime) {
		this.initForthTime = forthTime;
	}

	public long getInitBackTime() {
		return initBackTime;
	}

	public void setInitBackTime(long backTime) {
		this.initBackTime = backTime;
	}

	public long getEndForthTime() {
		return endForthTime;
	}

	public void setEndForthTime(long endForthTime) {
		this.endForthTime = endForthTime;
	}

	public long getStartTime() {
		return startTime;
	}
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public long getEndTime() {
		return endTime;
	}
	
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public long getElapsedTime(){
		return endTime - startTime;
	}
	
	public long getForthElapsedTime(){
		return this.endForthTime - this.initForthTime;
	}
	
	public long getBackElapsedTime(){
		return this.endBackTime - this.initBackTime;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	@Override
	public int compareTo(Event e) {
		
		return Long.compare(this.timeStamp, e.getTimeStamp());
	}	
}
