package br.ufpe.cin.middleware.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class Network {

	public static boolean isReachable(String ip, int timeout){
		try
		{
			InetAddress inet = InetAddress.getByName(ip);
			return inet.isReachable(timeout);
		} catch (UnknownHostException e) {
			System.err.println("Host does not exists");
		} catch (IOException e) {
			System.err.println("Error in reaching the Host");
		}
		return true;
	}
}
