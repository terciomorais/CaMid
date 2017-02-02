package tests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.protocols.communication.MessageBody;
import br.ufpe.cin.in1118.protocols.communication.MessageHeader;

public class SerializerMain {

	public static void main(String[] args) {
		int t = Integer.parseInt(args[0]);
		ExecutorService executor = Executors.newFixedThreadPool(256);
		Message msg = new Message(new MessageHeader(), new MessageBody());
		for(int i = 0; i < t; i++){
			executor.execute(new Serializer(msg));
		}
		executor.shutdown();
		try {
			executor.awaitTermination(1L, java.util.concurrent.TimeUnit.DAYS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

}
