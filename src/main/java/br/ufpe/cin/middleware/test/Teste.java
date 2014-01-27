package br.ufpe.cin.middleware.test;

import br.ufpe.cin.middleware.services.cloud.manager.monitor.IaaSMonitor;

public class Teste {
	//private static final String ONE_AUTH = "cam:cam2012";
	//private static final String ONE_XMLRPC = "http://172.20.16.32:2633/RPC2";
	//private static final String ONE_XMLRPC = "http://10.8.0.4:2633/RPC2";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Network.isReachable("10.0.0.2", 500);
		//Network.isReachable("10.0.0.1", 500);
		IaaSMonitor im = new IaaSMonitor();
		//EnvironmentMonitor em = new EnvironmentMonitor();
		//em.start();
		im.start();

	}
}

/*		try{
long begin = System.currentTimeMillis();
Client client = new Client(ONE_AUTH, ONE_XMLRPC);
VirtualMachinePool vmp = new VirtualMachinePool(client);
OneResponse or = vmp.info();
System.out.println("Tempo de espera: " + (System.currentTimeMillis() - begin) + " ms");
System.out.println("RESULTADO: " + or.getMessage());

} catch (Exception e) {
System.err.println(e);
}*/