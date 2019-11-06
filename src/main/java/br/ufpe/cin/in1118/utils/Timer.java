package br.ufpe.cin.in1118.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

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
		if (endTime < initTime || initTime == 0)
			return -1;
		else
			return endTime - initTime;
	}

	public void printTime(){
			Date d = GregorianCalendar.getInstance().getTime();
			SimpleDateFormat format = new SimpleDateFormat();
			System.out.println(format.format(d));	
	}
}
