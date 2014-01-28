package br.ufpe.cin.middleware.infrastructure.client;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;

public class Sender {
	
	private ObjectOutputStream outputMessage	= null;
	private ObjectInputStream incomingMessage	= null;
	private Socket conn = null;
	
	private String host;
	private int port;
	
	private MethodResponseMessage response;
	
	public Sender(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public MethodResponseMessage sendReceive(MethodRequestMessage request) {
		
		MethodResponseMessage response = null;
		try {
			conn = new Socket(this.host, this.port);
			this.send(request);
			response = this.receive();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotSerializableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			end();
		}
		
		return response;
	}
	
	private void send(MethodRequestMessage request) throws IOException{
	
		request.setHost(conn.getInetAddress().getHostAddress());
		request.setPort(conn.getPort());

		this.outputMessage = new ObjectOutputStream(conn.getOutputStream());
		this.outputMessage.writeObject(request);
		this.outputMessage.flush();
	}
	
	private MethodResponseMessage receive() throws ClassNotFoundException{
		try {
			this.incomingMessage = new ObjectInputStream(this.conn.getInputStream());
			this.response = (MethodResponseMessage) this.incomingMessage.readObject();
//			this.responseMessage =  this.incomingMessage.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.response;
	}
	
	public MethodResponseMessage getResponseMessage(){
		return this.response;
	}
	
	private void end(){
		if(incomingMessage != null)
		{
			try {
				incomingMessage.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(outputMessage != null)
		{
			try {
				outputMessage.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(conn != null)
		{
			try {
				conn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}