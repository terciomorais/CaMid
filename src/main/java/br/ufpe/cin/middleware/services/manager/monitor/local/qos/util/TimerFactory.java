package br.ufpe.cin.middleware.services.manager.monitor.local.qos.util;

import java.util.Map;
import java.util.TreeMap;

public class TimerFactory {

	public static final long ONE_SECOND_INTERVAL = 1000;
	
	private Map<Long, Timer> timerMap;
	
	private TimerFactory()
	{
		timerMap = new TreeMap<Long,Timer>();
	}
	
	private static final TimerFactory INSTANCE = new TimerFactory(); 
	
	public static TimerFactory getInstance()
	{
		return INSTANCE;
	}
	
	public synchronized Timer createTimer(long intervalLength)
	{
		Timer timer = timerMap.get(intervalLength);
		if(timer == null)
		{
			timer = new Timer(intervalLength);
			timerMap.put(intervalLength, timer);
		}
		return timer;
	}
	
}
