package br.ufpe.cin.in1118.infrastructure.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientSenderREM implements Callable <byte[]> {
	private DataOutputStream	dos				= null;
	private DataInputStream		dis				= null;
	private Socket				conn			= null;
	
	private String				host			= "localhost";
	private int					port			= 1313;
	
	private byte[]				receivedMessage	= null;
	private byte[]				messageToSend	= null;
	private int 				receivedMessageSize;
	private int 				messageToSendSize;
	
	public ClientSenderREM(String host, int port, byte[] marshalledMessage){
		this.host 			= host;
		this.port			= port;
		this.messageToSend	= marshalledMessage;
		this.messageToSendSize	= this.messageToSend.length;
	}

	@Override
	public byte[] call() { //throws Exception {
		try {
			this.conn	= new Socket(this.host, this.port);
			this.dis		= new DataInputStream(new BufferedInputStream(this.conn.getInputStream()));
			this.dos		= new DataOutputStream(new BufferedOutputStream(this.conn.getOutputStream()));
			
			//this.messageToSendSize = this.messageToSend.length;
			this.dos.writeInt(this.messageToSendSize);
			this.dos.write(this.messageToSend);
			this.dos.flush();
			
			this.receivedMessageSize = this.dis.readInt();
			this.receivedMessage = new byte[this.receivedMessageSize];
			this.dis.read(this.receivedMessage, 0, this.receivedMessageSize);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try{
			if(this.dis != null)
				this.dis.close();
			if(this.dos != null)
				this.dos.close();
			if(!this.conn.isClosed())
				this.conn.close();
			} catch(IOException ioe){
				
			}
		}
		return this.receivedMessage;
	}
}
