package tests;

import java.util.Iterator;

import org.opennebula.client.Client;
import org.opennebula.client.host.HostPool;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

public class OpennebulaTest {
	private static String ONE_AUTH = "oneadmin:gfads!@#";
	private static String ONE_XMLRPC = "http://10.66.66.4:2633/RPC2";

	public static void main(String[] args) {
		
		try {
			Client oneClient = new Client(ONE_AUTH, ONE_XMLRPC);

			HostPool hp = new HostPool(oneClient);
			
			VirtualMachinePool vmp = new VirtualMachinePool(oneClient);
			
			System.out.println(">>> " + vmp.info().getMessage());
			hp.info();
			System.out.println("número de hosts " + hp.getLength());
			//vmp.info();
			System.out.println(" Número de VMs " + vmp.getLength());
			Iterator<VirtualMachine> it = vmp.iterator();
			while (it.hasNext()) {
				VirtualMachine vm = it.next();
				String xml = vm.info().getMessage();
				System.out.println("VM id: " + vm.getId()
					+ "\nIP: " + xml.substring(xml.indexOf("<ETH0_IP>") + 18, xml.indexOf("</ETH0_IP>") - 3)
					+ "\nStatus " + vm.status());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}
/* 		try {
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
		} */

