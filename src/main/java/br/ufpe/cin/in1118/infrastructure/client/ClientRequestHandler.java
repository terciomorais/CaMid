package br.ufpe.cin.in1118.infrastructure.client;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import br.ufpe.cin.in1118.protocols.communication.Message;

public class ClientRequestHandler {

	private static ClientRequestHandler	INSTANCE;
	private ExecutorService handlerPool = Executors.newFixedThreadPool(512);

	private ClientRequestHandler(){};
	
	public static synchronized ClientRequestHandler getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ClientRequestHandler();
		}
		return INSTANCE;
	}

	public Message submit(ClientSender clientSender) {
		Message response = null;
		
		Future<Message> futureMsg = handlerPool.submit(clientSender);
		
		try {
			response = futureMsg.get();
		} catch (InterruptedException e) {
			System.err.println("[ClientRequestHandler:31] Error: Thread interrupted " + e.getMessage());
			response = new Message();
			response.setStatus(Message.ResponseStatus.SENDING_EXCEPTION);
			response.setStatusMessage("Remote invocation interrupted by ClientRequestHandler");
			return response;
		} catch (ExecutionException e) {
			System.err.println("[ClientRequestHandler:37] Error: Thread execution " + e.getMessage());
			response = new Message();
			response.setStatus(Message.ResponseStatus.SENDING_EXCEPTION);
			response.setStatusMessage("Execution exception on ClientRequestHandler");
			return response;
		}
		return response;
	}
	public int getActiveConn() {
		return ((ThreadPoolExecutor) handlerPool).getActiveCount();
	}

	public void close(){


		handlerPool.shutdown();
		INSTANCE = null;
	}
}
