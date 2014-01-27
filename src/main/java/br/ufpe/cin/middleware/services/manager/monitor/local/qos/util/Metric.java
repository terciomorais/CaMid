package br.ufpe.cin.middleware.services.manager.monitor.local.qos.util;

import java.util.Deque;
import java.util.LinkedList;

public class Metric {

	private Deque<Long> responseTimeQueue;

	private int maxSize;
	
	private double sum;
	private double squaresSum;
	
	
	public Metric()
	{
		responseTimeQueue = new LinkedList<Long>();
		maxSize = 100;
	
		sum = 0;
		squaresSum = 0;
		
	}
	
	public Metric(int maxSize)
	{
		responseTimeQueue = new LinkedList<Long>();
		this.maxSize = maxSize;
	
		sum = 0;
		squaresSum = 0;
	}
	
	public void addMetric(Long responseTime) 
	{
		if(responseTimeQueue.size() < maxSize)
		{
			responseTimeQueue.offer(responseTime);
		}
		else
		{
			double lastDvalue = responseTimeQueue.poll().doubleValue();
			sum -= lastDvalue;
			squaresSum -= (lastDvalue * lastDvalue);
			responseTimeQueue.offer(responseTime);
		}
		
		double newDvalue = responseTime.doubleValue();
		sum += newDvalue;
		squaresSum += (newDvalue*newDvalue);
	}
	
	public double average()
	{
		double n = (double)responseTimeQueue.size();
		double average = sum / n;
		return average;
	}
	
	public double standardDeviation()
	{
		double n = (double)responseTimeQueue.size();
		double stdDev = (n*squaresSum - sum*sum) / (n * (n-1));
		return stdDev;
	}
	
}
