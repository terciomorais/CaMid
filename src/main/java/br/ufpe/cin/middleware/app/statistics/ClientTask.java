package br.ufpe.cin.middleware.app.statistics;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import br.ufpe.cin.camid.distribution.stub.DelayStub;

class ClientTask implements Callable<Boolean>
{
	private DelayStub delayService;
	private AtomicLong successes;
	private StatFileWriter writer;
	private int workload;
	private long delay;
	
	public ClientTask(DelayStub calc, StatFileWriter writer, AtomicLong successes, int workload, long serviceDelay)
	{
		this.delayService = calc;
		this.successes = successes;
		this.writer = writer;
		this.workload = workload;
		this.delay = serviceDelay;
	}

	@Override
	public Boolean call() {
		
		long start = 0;
		long end = 0;
		boolean success = false;
		try 
		{
			start = System.currentTimeMillis();
			delayService.delay(delay);
			end = System.currentTimeMillis();
			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			end = System.currentTimeMillis();
			e.printStackTrace();
		}
		if(success) 
			successes.incrementAndGet();
		writer.write(end-start, success, workload, delay);
		
		return success;
	}
	
}