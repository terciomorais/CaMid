package br.ufpe.cin.middleware.distribution.cloud;

public class TesteProxy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CloudProxy cp = new CloudProxy("/cloud1.properties");
		cp.run();
	}

}
