package br.ufpe.cin.middleware.services.manager.monitor.local.qos.util;


public class SynchronizedMetric 
{
	private Metric metric;
	
	public SynchronizedMetric()
	{
		metric = new Metric();
	}
	
	public SynchronizedMetric(int maxSize)
	{
		metric = new Metric(maxSize);
	}
	
	public void addMetric(Long responseTime) 
	{
		synchronized(metric)
		{
			metric.addMetric(responseTime);
		}
	}
	
	public double average()
	{
		double average = 0.0;
		synchronized(metric)
		{
			average = metric.average();
		}
		return average;
	}
	
	public double standardDeviation()
	{
		double stddev = 0.0;
		synchronized(metric)
		{
			stddev = metric.standardDeviation();
		}
		return stddev;
	}
	
}
