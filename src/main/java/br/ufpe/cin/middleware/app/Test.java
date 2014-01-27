package br.ufpe.cin.middleware.app;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

public class Test {
	static String ONE_AUTH = "oneadmin:secmosc";
	static String ONE_XMLRPC = "http://10.8.0.4:2633/RPC2";
	//private final String ONE_XMLRPC = "http://localhost:2633/RPC2";

	
	public static void main (String []args){
		Client client;
		try {
			client = new Client(ONE_AUTH, ONE_XMLRPC);
			VirtualMachinePool vmPool = new VirtualMachinePool(client);
			VirtualMachine vm = new VirtualMachine(90, client);
			OneResponse or = vm.resume();
			if (or.isError()){
				throw new Exception(or.getErrorMessage());
			}
			
			System.out.println("RESPONSE: " + vm.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
