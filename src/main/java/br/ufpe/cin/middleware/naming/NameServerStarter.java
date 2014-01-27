package br.ufpe.cin.middleware.naming;

public class NameServerStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println( "\033[H\033[2J" );
		NamingService ns = new NamingService();
		ns.run();
	}

}
