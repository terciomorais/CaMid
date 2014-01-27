package br.ufpe.cin.middleware.app;

import java.io.InputStream;
import java.util.Properties;

public class TestProperties {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			Properties properties = new Properties();
			InputStream inStream = TestProperties.class.getResourceAsStream("service.properties");
			properties.load(inStream);
			
			System.out.println("Service name: " + properties.getProperty("servicename"));
			System.out.println("Hostname : " + properties.getProperty("hostname"));
			System.out.println("Port: " + properties.getProperty("port"));
		} catch (Exception ioE){
			ioE.printStackTrace();
		}
	}

}
