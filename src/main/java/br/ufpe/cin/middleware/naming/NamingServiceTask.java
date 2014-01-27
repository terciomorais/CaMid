package br.ufpe.cin.middleware.naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class NamingServiceTask implements Runnable {

	private Socket socket;
	private Map<String,Set<Service>> serviceMap;
	
	private NamingServiceRequest incomingMessage = null;
	
	private ReadWriteLock lock;
	
	public NamingServiceTask(NamingServiceRequest serviceRequest, Map<String,Set<Service>> serviceMap, ReadWriteLock lock){
		this.setIncomingMassage(serviceRequest);
		this.serviceMap	= serviceMap;
		this.lock		= lock;
	}
	
	public NamingServiceTask(Socket socket, Map<String,Set<Service>> serviceMap, ReadWriteLock lock)
	{
		this.socket 	= socket;
		this.serviceMap = serviceMap;
		this.lock 		= lock;
	}

	public void run() {
		ObjectInputStream in = null;
		try {
			System.out.println("Incoming message ...");
			in = new ObjectInputStream(socket.getInputStream());
			this.setIncomingMassage((NamingServiceRequest) in.readObject());
			NamingServiceResponse response = this.processMessage();
			if (response != null){
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(response);
				out.close();
			}
			in.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setIncomingMassage(NamingServiceRequest incomingMassage){
		this.incomingMessage = incomingMassage;
	}
	
	public NamingServiceResponse processMessage() {
		NamingServiceResponse response = null;
		Lock readLock = this.lock.readLock();
		Lock writeLock = this.lock.writeLock();
		
		switch(this.incomingMessage.getOperation()) {
			case REGISTER :	{
				try {
					writeLock.lock();
					Service service = this.incomingMessage.getService();
					service.setHost(this.recoverAddress());
					Set<Service> registeredEndpoints = this.serviceMap.get(service.getName());
					System.out.println("Serviço: " + service.getName());
					
					if(registeredEndpoints == null)
						registeredEndpoints = new TreeSet<Service>();

					registeredEndpoints.add(service);
					this.serviceMap.put(service.getName(), registeredEndpoints);
					
					for(Service s : registeredEndpoints){
						System.out.println("	" + s.getHost() + ":" + s.getPort());
					}
				}
				finally {
					writeLock.unlock();
				}
				break;
			}
			case UNREGISTER : {
				try	{
					writeLock.lock();
					Service service = this.incomingMessage.getService();
					Set<Service> registeredEndpoints = this.serviceMap.get(service.getName());
					
					for(Iterator<Service> iterator = registeredEndpoints.iterator(); iterator.hasNext();) {
						Service temp = iterator.next();
						if(temp.equals(service)) {
							iterator.remove();
							break;
						}
					}
				}
				finally {
					writeLock.unlock();
				}
				break;
			}
			case SEARCH : {
				try	{
					System.out.println("Search initialized...");
					readLock.lock();
					response = new NamingServiceResponse();
					Set<Service> registeredServices = this.serviceMap.get(
						this.incomingMessage.getRequestedService());
					
					if(registeredServices != null) {
						for(Service service : registeredServices) {
							response.getServiceEndpoints().add(service);
						}
					}
				}
				finally	{
					readLock.unlock();
				}
				break;
			}
			case LIST: {
				try {
					readLock.lock();
					response = new NamingServiceResponse ();
					response.getServiceMap().putAll(this.serviceMap);
				}
				finally {
					readLock.unlock();
				}
				break;
			}
		}
		
		return response;
	}

	private String recoverAddress() {
		String address = this.socket.getInetAddress().getHostAddress();
		if(address.startsWith("127.")) {
			try {
				Enumeration<NetworkInterface> netifs = NetworkInterface.getNetworkInterfaces();
				while(netifs.hasMoreElements())	{
					NetworkInterface netif = netifs.nextElement();
					if(netif.isUp() && !netif.isLoopback() && !netif.isPointToPoint()) {
						Enumeration<InetAddress> iaddrs = netif.getInetAddresses();
						while(iaddrs.hasMoreElements())	{
							InetAddress iaddr = iaddrs.nextElement();
							if(iaddr instanceof Inet4Address) {
								address = iaddr.getHostAddress();
								break;
							}
						}
					}
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return address;
	}
}
