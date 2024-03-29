package br.ufpe.cin.in1118.application.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import br.ufpe.cin.in1118.distribution.stub.DelayStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.management.monitoring.Event;
import br.ufpe.cin.in1118.protocols.communication.Message;

public class SenderRunner implements Runnable{ // implements Runnable {

	public static AtomicInteger	fails;
	public static AtomicInteger	success;
	public static AtomicInteger	systemFails;
	public static List<Event>	elapsedTimes	= null;
	private int 				serviceTime		= 1;
	private DelayStub			delay			= null;
	private int 				sleep			= 0;			
	public static int 			threadCount;
	private boolean				isByTax			= false;
	private int					times			= 0;

	public SenderRunner(DelayStub delay, int serviceTime) {
		this.serviceTime			= serviceTime;
		this.delay					= delay;		
		SenderRunner.createMetricList();
	}

	public SenderRunner(int reqTax, int serviceTime, int times, String host, int port){
		this.serviceTime			= serviceTime;
		NamingStub naming			= new NamingStub(host, port);
		this.delay					= (DelayStub) naming.lookup("delay");
		this.sleep					= 1000/reqTax;
		this.times					= times;
		this.serviceTime			= serviceTime;
		SenderRunner.createMetricList();
		this.isByTax = true;
	}
	public SenderRunner(int reqTax, int serviceTime, int time, String host, int port, boolean isByTax){
		this.serviceTime			= serviceTime;
		NamingStub naming			= new NamingStub(host, port);
		this.delay					= (DelayStub) naming.lookup("delay");
		this.sleep					= 1000/reqTax;
		this.serviceTime			= serviceTime;
		this.times					= time;
		SenderRunner.createMetricList();
		this.isByTax = false;
	}

	public static void createMetricList(){
		if (SenderRunner.elapsedTimes == null)
			SenderRunner.elapsedTimes = Collections.synchronizedList(new ArrayList<Event>());
		if(SenderRunner.success == null)
			SenderRunner.success = new AtomicInteger(0);
		if(SenderRunner.fails == null)
			SenderRunner.fails = new AtomicInteger(0);
		if(SenderRunner.systemFails == null)
			SenderRunner.systemFails = new AtomicInteger(0);
	}

	public void run(){
		if(this.isByTax){
			for(int i = 0; i < this.times; i++)
				this.invoke(this.sleep);
		} else {
			long iniTime = System.currentTimeMillis();
			while((System.currentTimeMillis() - iniTime) < this.times)
				this.invoke(this.sleep);

			//			this.begin 	= System.currentTimeMillis();
			//			this.delay.delay(this.serviceTime);
			//			this.end = System.currentTimeMillis();
			//			if(this.delay.getReply().getStatus().equals(Message.ResponseStatus.SUCCESS)){
			//				SenderRunner.elapsedTimes.add(end - begin);
			//				SenderRunner.success.incrementAndGet();
			//			} else
			//				SenderRunner.fails.incrementAndGet();
		}
	}
	
	private void invoke(int sleep){

		Event event = new Event();
		event.setStartTime(System.currentTimeMillis());
		this.delay.delay(this.serviceTime);
		event.setEndTime(System.currentTimeMillis());
		event.setSuccess(this.delay.getReply().getStatus().equals(Message.ResponseStatus.SUCCESS));
		SenderRunner.elapsedTimes.add(event);
		
		if(event.isSuccess())			
			SenderRunner.success.incrementAndGet();
		else
			SenderRunner.fails.incrementAndGet();
		
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
