package br.ufpe.cin.middleware.services.qos.metrics;

import junit.framework.Assert;

import org.junit.Test;

import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.Metric;

public class ResponseTimeMetricTest {

	private long responseTimes[] = {100,200,150,120,130}; 
	
	@Test
	public void testResponseTimeAverage()
	{
		Metric rta = new Metric();
		for(long responseTime : responseTimes)
		{
			rta.addMetric(responseTime);
		}
		double mean = rta.average();
		System.out.println(mean);
		Assert.assertEquals(140.0, mean);
	}
	
	
	
	
}
