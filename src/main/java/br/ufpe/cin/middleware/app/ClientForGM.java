package br.ufpe.cin.middleware.app;

import br.ufpe.cin.middleware.services.cloud.manager.monitor.EnvironmentMonitor;

public class ClientForGM {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EnvironmentMonitor em = new EnvironmentMonitor();
		em.start();
		long begin = System.currentTimeMillis();
		do{
			System.out.println("Esperando a finalização das threads...");
		} while (em.getAppinformations() == null);
		System.out.println("Tempo de espera: " + (System.currentTimeMillis() - begin) + " ms");
/*		for(ManageInformation mi : em.getAppinformations())
			System.out.println("	" + mi.getName() + " = " + mi.getValue() +
					"\n	" + mi.getUId());*/
	}
}
