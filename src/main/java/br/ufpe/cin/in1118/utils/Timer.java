package br.ufpe.cin.in1118.utils;

public class Timer {
	private long initTime	= 0;
	private long endTime	= 0;
	
	public Timer(){}
	
	public Timer(long currentTimeMillis) {
		this.setInitTime(currentTimeMillis);
	}
	public long getInitTime() {
		return initTime;
	}
	public void setInitTime(long initTime) {
		this.initTime = initTime;
	}

	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public long getElapsedTime(){
		if (initTime == 0)
			return 0;
		else
			return endTime - initTime;
	}
}
