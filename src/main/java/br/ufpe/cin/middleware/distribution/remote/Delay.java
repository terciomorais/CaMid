package br.ufpe.cin.middleware.distribution.remote;

import java.util.Random;

public class Delay {

	public static final String SERVICE_NAME = "Delay";
	
	public long delay(long timeInMilis)
	{
		try 
		{
			Random rd = new Random();
			Double meanDelay = new Long(timeInMilis).doubleValue();
			Double stddev = meanDelay/4;
			
			Double delayGaussian = rd.nextGaussian()*stddev + meanDelay;
			Thread.sleep(delayGaussian.longValue());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeInMilis;
	}
}


