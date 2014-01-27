package br.ufpe.cin.middleware.distribution.qos.observers;

import java.util.Observer;

import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.TimerFactory;

public abstract class TickableObserver implements Observer  
{
	
	public TickableObserver()
	{
		TimerFactory.getInstance().createTimer(TimerFactory.ONE_SECOND_INTERVAL).addObserver(this);
	}
	
	
}
