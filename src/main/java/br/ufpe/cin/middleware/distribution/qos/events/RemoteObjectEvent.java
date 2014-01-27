package br.ufpe.cin.middleware.distribution.qos.events;

import br.ufpe.cin.middleware.distribution.ObjectDescriptor;

public class RemoteObjectEvent {
	
	private ObjectDescriptor descriptor;
	
	private long startTime;
	
	private long endTime;
	
	private boolean success;
	
	private Exception exception;
	
	public ObjectDescriptor getDescriptor() {
		return descriptor;
	}
	
	public void setDescriptor(ObjectDescriptor descriptor) {
		this.descriptor = descriptor;
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

}
