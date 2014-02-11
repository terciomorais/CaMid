package br.ufpe.cin.middleware.naming;

public class NameServerStarterCloud2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println( "\033[H\033[2J" );
		NamingService ns = new NamingService("/cloud2.properties");
		ns.run();
	}

}
