package br.ufpe.cin.middleware.utils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public abstract class Network {

	public static boolean isReachable(String ip, int timeout){
		try
		{
			InetAddress inet = InetAddress.getByName(ip);
			return inet.isReachable(timeout);
		} catch (UnknownHostException e) {
			System.err.println("Host does not exists");
		} catch (IOException e) {
			System.err.println("Error trying to reach the host " + ip);
		}
		return true;
	}
	
	public static String recoverAddress(String address) {
		
		if(Character.isAlphabetic(address.charAt(0))){
			try {
				address = InetAddress.getByName(address).getHostAddress();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
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
