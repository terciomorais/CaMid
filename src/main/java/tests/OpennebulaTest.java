package tests;

import java.net.InetAddress;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

public class OpennebulaTest {
	private static String ONE_AUTH = "oneadmin:gfads!@#";
	private static String ONE_XMLRPC = "http://10.66.66.6:2633/RPC2";
	public static void main(String[] args) {
		// 
		try {
			long init = System.currentTimeMillis();
			Client oneClient = new Client(ONE_AUTH, ONE_XMLRPC);
			VirtualMachine vm = new VirtualMachine(4,oneClient);

			String xml = vm.info().getMessage();
			
			String ip = xml.substring(xml.indexOf("<ETH0_IP>") + 18, xml.indexOf("</ETH0_IP>") - 3);

			init = System.currentTimeMillis();
			InetAddress  inet = InetAddress.getByName(ip);
			OneResponse rc = vm.resume();
		    if( rc.isError() ){
		        System.out.println( "failed!");
		        throw new Exception( rc.getErrorMessage() );
		    }
			while(!inet.isReachable(5000));
		    //System.out.println(inet.isReachable(5000) ? "Host is reachable" : "Host is NOT reachable");
		    System.out.println("time spent to be reachable " + (System.currentTimeMillis() - init)*1000 + " s");

		} catch (ClientConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
