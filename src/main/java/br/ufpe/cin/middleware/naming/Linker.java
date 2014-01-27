package br.ufpe.cin.middleware.naming;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Linker {
	
	private Service remoteObject = null;
	
	private String namingHost;
	
	private int namingPort;
	
	public Linker(){
	}
	
	public Linker(String namingHost, int namingPort){
		this.namingHost = namingHost;
		this.namingPort = namingPort;
	}

	public Service getRemoteObject() {
		return remoteObject;
	}

	public void setRemoteObject(Service remoteObject) {
		this.remoteObject = remoteObject;
	}

	public String getNamingHost() {
		return namingHost;
	}

	public void setNamingHost(String namingHost) {
		this.namingHost = namingHost;
	}

	public int getNamingPort() {
		return namingPort;
	}

	public void setNamingPort(int namingPort) {
		this.namingPort = namingPort;
	}

	public void register(String objName, String host, int port){
		this.remoteObject = new Service(objName, host, port);
		this.bind(this.remoteObject);
	}
	
	public void bind(Service endpoint) {
		Socket socket = null;
		ObjectOutputStream oos = null;
		try {
			socket = new Socket(this.namingHost, this.namingPort);

			oos = new ObjectOutputStream(socket.getOutputStream());
			NamingServiceRequest request =
					new NamingServiceRequest
					(NamingServiceRequest.NamingServiceOperation.REGISTER, endpoint);
//			obj.writeUTF(this.createMessage("r"));
			oos.writeObject(request);
			
		} catch (UnknownHostException e) {
			System.out.println("It's not possible to registry. Endpoint unreachable");	
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(oos != null)
			{
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(socket != null)
			{
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void unbind(Service object){
		try {
			Socket socket = new Socket(object.getHost(), this.namingPort);
			DataOutputStream obj = new DataOutputStream(socket.getOutputStream());
			obj.writeUTF(this.createMessage("u"));
			
		} catch (UnknownHostException e) {
			System.out.println("It's not possible to registry. Endpoint unreachable");	
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	private String createMessage(String op){
		return "s:" + this.remoteObject.getName() + ":" + this.remoteObject.getHost() + ":" + this.remoteObject.getPort() + ":" + op;
	}
}
