package br.ufpe.cin.in1118.management.monitoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class InvokingDataPoint {
	private DataPoint 	statistics	= null;
	private List<Event>	events		= null;	
	private long		interval	= 0;
	private boolean		afterAdapt	= false;
	
	public InvokingDataPoint(){
		this.events = Collections.synchronizedList(new ArrayList<Event>());
	}
	
	public InvokingDataPoint(List<Event> events){
		this.events = Collections.synchronizedList(new ArrayList<Event>(events));
		this.statistics = new DataPoint();
		this.setDataPoint();
	}
	
	public List<Event> getEvents() {
		return this.events;
	}

	public synchronized void setDataPoint(){
		if(!events.isEmpty()){
			long max		= events.get(0).getElapsedTime();
			long min		= events.get(0).getElapsedTime();
			
			long firstTime	= events.get(0).getStartTime();
			long lastTime	= events.get(0).getStartTime();
			
			this.statistics.setBeginTimeStamp(this.events.get(0).getTimeStamp());
			this.statistics.setEndTimeStamp(this.events.get(events.size() - 1).getTimeStamp());
			this.statistics.setCount(events.size());
			for(Iterator<Event> it = events.iterator(); it.hasNext();){
				Event ev = it.next();
				if(ev.isSuccess()){
					this.statistics.setSum(this.statistics.getSum() + ev.getElapsedTime());
				} else
					this.statistics.setFailCount(this.statistics.getFailCount() + 1);

				max = ev.getElapsedTime() > max ? ev.getElapsedTime() : max;
				min = ev.getElapsedTime() < min ? ev.getElapsedTime() : min;
				
				firstTime	= ev.getStartTime() < firstTime ? ev.getStartTime() : firstTime;
				lastTime	= ev.getEndTime() > lastTime ? ev.getEndTime() : lastTime;
			}
			
			this.setInterval(lastTime - firstTime);
			this.statistics.setLowerValue(min);
			this.statistics.setHighValue(max);
			
			this.statistics.setStatistics();
		}
	}

	public long getInterval() {
		return interval;
	}

	public boolean isAfterAdapt() {
		return this.afterAdapt;
	}

	public boolean getAfterAdapt() {
		return this.afterAdapt;
	}

	public void setAfterAdapt(boolean afterAdapt) {
		this.afterAdapt = afterAdapt;
	}
	
	private void setInterval(long interval) {
		this.interval = interval;
	}
	
	public Event getLastEvent(){
		return this.events.get(this.events.size() - 1);
	}

	public DataPoint getStatistics(){
		return this.statistics;
	}

}
