package br.ufpe.cin.middleware.distribution.cloud;

public class TesteProxyCloud2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CloudProxy cp = new CloudProxy("/cloud2.properties");
		cp.run();
	}
}
