package br.ufpe.cin.middleware.naming;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.util.ExceptionUtils;

public class Naming {
	
	private String host;
	private int port;
	
	public Naming(String host, int port) {
		super();
		this.setHost(host);
		this.setPort(port);
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<Service> lookup(String serviceName) throws Exception{
		Class c1 = null;
		NamingServiceResponse response = null;
		
		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try	{
			socket = new Socket(host, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			NamingServiceRequest request = new NamingServiceRequest(serviceName);
			out.writeObject(request);
			out.flush();
	//		out.writeUTF("c:" + classe);
	
			in = new ObjectInputStream(socket.getInputStream());
			response = (NamingServiceResponse)in.readObject();
		} catch (Exception e) {
			System.out.println("-- " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			if(in != null)
				in.close();
			if(out != null)
				out.close();
			if(socket != null)
				socket.close();
		}
		return response.getServiceEndpoints();
	}
	
	public Map<String, Set<Service>> lookupAll() throws Exception{
		Class c1 = null;
		NamingServiceResponse response = null;
		
		Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		
		try	{
			socket = new Socket(host, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			NamingServiceRequest request = new NamingServiceRequest("ALL_SERVICES");
			out.writeObject(request);
			out.flush();
	
			in = new ObjectInputStream(socket.getInputStream());
			response = (NamingServiceResponse)in.readObject();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if(in != null)
				in.close();
			if(out != null)
				out.close();
			if(socket != null)
				socket.close();
		}
		return response.getServiceMap();
	}
}
