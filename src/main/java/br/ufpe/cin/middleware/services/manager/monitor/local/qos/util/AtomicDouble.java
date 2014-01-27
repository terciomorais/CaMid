package br.ufpe.cin.middleware.services.manager.monitor.local.qos.util;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicDouble {

	private AtomicLong placeHolder;
	
	public AtomicDouble(double d)
	{
		placeHolder = new AtomicLong(Double.doubleToLongBits(d));
	}
	
	public AtomicDouble()
	{
		placeHolder = new AtomicLong(Double.doubleToLongBits(0));
	}
	
	public double get()
	{
		return Double.longBitsToDouble(placeHolder.get());
	}
	
	public double getAndSet(double d)
	{
		return Double.longBitsToDouble(placeHolder.getAndSet(Double.doubleToLongBits(d)));
	}
	
	
}
