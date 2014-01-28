package br.ufpe.cin.middleware.app;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;
import br.ufpe.cin.middleware.distribution.protocol.MethodResponseMessage;
import br.ufpe.cin.middleware.distribution.protocol.RequestParameter;
import br.ufpe.cin.middleware.infrastructure.client.Sender;
import br.ufpe.cin.middleware.utils.PropertiesSetup;

public class ClientProxy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertiesSetup properties = new PropertiesSetup("/service.properties");
		String host = properties.getProperties().getProperty("cloudproxy_endpoint");
		int port = Integer.parseInt(properties.getProperties().getProperty("cloudproxy_port"));
		
		MethodRequestMessage mrm =new MethodRequestMessage();
		mrm.setMethod("soma");
		mrm.setHost(host);
		mrm.setPort(port);
		mrm.setServiceName("Calculadora");
		mrm.getParameters().add(new RequestParameter("a", 1));
		mrm.getParameters().add(new RequestParameter("b", 2));
		
		Sender sender = new Sender("10.6.0.19", port);
		MethodResponseMessage mresm = sender.sendReceive(mrm);
		System.out.println("Final Response: " + mresm.getResponse());
	}
}
