package br.ufpe.cin.middleware.services.manager.monitor.local.qos.util;

import java.util.Observable;

public class Timer extends Observable implements Runnable 
{
	
	private Thread t;
	
	private long intervalLength;
	
	public Timer(long intervalLength)
	{
		this.intervalLength = intervalLength;
		t = new Thread(this);
		t.start();
	}
	
	public void run() {
		 
		while(true)
		{
			try {
				Thread.sleep(intervalLength);
				this.setChanged();
				this.notifyObservers();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}
