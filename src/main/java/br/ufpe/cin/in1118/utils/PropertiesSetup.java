package br.ufpe.cin.in1118.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesSetup {
	
	private Properties properties = new Properties();
	private String fileAddress = "";

	public PropertiesSetup(String fileAddress){
		this.fileAddress = fileAddress;
		
		try{
			InputStream input = new FileInputStream(fileAddress);
			this.properties.load(input);
			
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
