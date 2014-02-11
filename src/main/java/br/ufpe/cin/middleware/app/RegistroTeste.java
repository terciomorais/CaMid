package br.ufpe.cin.middleware.app;

import br.ufpe.cin.middleware.naming.Linker;
import br.ufpe.cin.middleware.naming.Service;

//Este executável é apenas para registrar o serviço da nuvem 2 no SN da nuvem 1 
public class RegistroTeste {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Service s = new Service("Calculadora", "localhost", 6001);//Essa porta é a do proxy
		Linker binding	= new Linker("localhost", 5000);
		binding.bind(s);
	}
}
