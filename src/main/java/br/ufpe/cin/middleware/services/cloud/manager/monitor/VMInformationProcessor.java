package br.ufpe.cin.middleware.services.cloud.manager.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class VMInformationProcessor{
	Properties properties			= new Properties();
	Enumeration<String> metricList	= null;
	
	public VMInformationProcessor(){
		try {
			this.properties.load(new FileInputStream(
					new File("VMmetrics.properties")));
			
		} catch (FileNotFoundException fnfException) {
			// TODO Auto-generated catch block
			fnfException.printStackTrace();
		} catch (IOException ioException) {
			// TODO Auto-generated catch block
			ioException.printStackTrace();
		}
	}
}