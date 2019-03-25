package br.ufpe.cin.in1118.distribution.marshaling;

import java.io.IOException;

import org.apache.commons.lang3.SerializationUtils;
//import org.apache.commons.lang3.SerializationUtils;

import br.ufpe.cin.in1118.protocols.communication.Message;


public class Marshaller {
	
	public byte[] marshall(Message message) throws IOException, InterruptedException, 
	ClassNotFoundException {
		
		return SerializationUtils.serialize(message);
		
/*		try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
			try(ObjectOutputStream oos = new ObjectOutputStream(bos)){
				oos.writeObject(message);
			}
			return bos.toByteArray();
		}
		
		ByteArrayOutputStream byteStream	= new ByteArrayOutputStream();
		ObjectOutputStream objectStream		= new ObjectOutputStream(new ByteArrayOutputStream());
		objectStream.writeObject(message);
		byte[] b = byteStream.toByteArray();
		//objectStream.flush();
		objectStream.close();

		byteStream.close();
		return b;*/
	}
	
	public Message unmarshall (byte [] messageToBeUnmarshalled) throws IOException, InterruptedException, 
	ClassNotFoundException{
		Message msg = null;
		synchronized (messageToBeUnmarshalled) {
			msg = (Message) SerializationUtils.deserialize(messageToBeUnmarshalled);
		}
		
		return new Message(msg.getHeader(), msg.getBody());
/*		try(ByteArrayInputStream bis = new ByteArrayInputStream(messageToBeUnmarshalled)){
			try (ObjectInputStream ois = new ObjectInputStream(bis)){
				return (Message) ois.readObject();
			}
		}*/
		
/*		ByteArrayInputStream byteStream = new ByteArrayInputStream(messageToBeUnmarshalled);
		ObjectInputStream objectStream = new ObjectInputStream(byteStream);
		Message msg = (Message) objectStream.readObject();
		byteStream.close();
		objectStream.close();
		return msg;*/
	}
}
