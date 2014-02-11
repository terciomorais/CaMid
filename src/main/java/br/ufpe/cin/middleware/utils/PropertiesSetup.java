package br.ufpe.cin.middleware.utils;

import java.util.Properties;

public class PropertiesSetup {
	
	private Properties properties = new Properties();
	private String fileAddress = "";
	
	public PropertiesSetup(String fileAddress){
		this.fileAddress = fileAddress;
		try{
			this.properties.load(getClass().getResourceAsStream(this.fileAddress));
		} catch (Exception ioE){
			ioE.printStackTrace();
		}
	}
	
	public Properties getProperties() {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getFileAddress() {
		return this.fileAddress;
	}
	

	
}
