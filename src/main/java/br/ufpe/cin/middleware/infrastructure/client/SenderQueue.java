package br.ufpe.cin.middleware.infrastructure.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;

public class SenderQueue {

	private static SenderQueue INSTANCE;
	private ExecutorService executor;
	
	private SenderQueue() {
		this.executor = Executors.newCachedThreadPool();
	}
	
	public static synchronized SenderQueue getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SenderQueue();
		}
		
		return INSTANCE;
	}
	
	public MethodResponseMessage doSend(Sender sender, MethodRequestMessage request)
	{
		SenderTask task = new SenderTask(sender, request);
		Future<MethodResponseMessage> futureMsg = executor.submit(task);
		MethodResponseMessage response = null;
		try {
			response = futureMsg.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public void close()
	{
		executor.shutdown();
		INSTANCE = null;
	}
	
}
