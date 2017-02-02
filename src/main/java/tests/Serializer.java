package tests;

import java.io.IOException;

import br.ufpe.cin.in1118.distribution.marshaling.Marshaller;
import br.ufpe.cin.in1118.protocols.communication.Message;

public class Serializer implements Runnable{

	Message	msgIn		= null;
	byte[]	serieIn		= null;
	Message	msgOut		= null;
	
	public Serializer(Message msg){
		this.msgIn = msg;
	}
	
	@Override
	public void run() {
		Marshaller marshaller = new Marshaller();
		try {
			this.serieIn = marshaller.marshall(this.msgIn);
			this.msgIn = marshaller.unmarshall(this.serieIn);
			System.out.println("[Serializer] task" + Thread.currentThread().getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
