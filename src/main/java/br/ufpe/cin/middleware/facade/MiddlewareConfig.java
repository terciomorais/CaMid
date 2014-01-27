package br.ufpe.cin.middleware.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class MiddlewareConfig {

	public final static String NAMING_ENABLED = "naming.enabled";
	
	public final static String NAMING_SERVICE_HOST = "naming.host";
	
	public final static String NAMING_SERVICE_PORT = "naming.port";
	
	public final static String SERVER_PORT = "server.port";
	
	public final static String SERVER_ENABLED = "server.enabled";
	
	public final static String IAAS_USER = "iaas.user";
	
	public final static String IAAS_ENDPOINT = "iaas.endpoint";
	

	
//	event.invoker.observer.rtm.enabled=true
	public final static String INVOKER_OBSERVER_RTM_ENABLED = "event.invoker.observer.rtm.enabled";
	
//	event.invoker.observer.rps.source.enabled=true
	public final static String INVOKER_OBSERVER_RPS_ENABLED = "event.invoker.observer.rps.enabled";
	
//	event.invoker.observer.fr.source.enabled=true
	public final static String INVOKER_OBSERVER_FR_ENABLED = "event.invoker.observer.fr.enabled";

	
	
//	event.remoteobject.observer.rtm.enabled=true
	public final static String REMOTE_OBJECT_OBSERVER_RTM_ENABLED = "event.remoteobject.observer.rtm.enabled";
	
//	event.remoteobject.observer.rps.source.enabled=true
	public final static String REMOTE_OBJECT_OBSERVER_RPS_ENABLED = "event.remoteobject.observer.rps.enabled";
	
//	event.remoteobject.observer.fr.source.enabled=true
	public final static String REMOTE_OBJECT_OBSERVER_FR_ENABLED = "event.remoteobject.observer.fr.enabled";	
	
	
//	event.system.observer.enabled
	public final static String SYSTEM_OBSERVER_ENABLED = "event.system.observer.enabled";

	public static final String MONITORING_ENABLED = "monitoring.enabled";

	
	private Properties config;
	
	public MiddlewareConfig ()
	{
		config = new Properties();
	}
	
	public void setProperty(String propName, String value)
	{
		config.setProperty(propName, value);
//		config.put(propName, value);
	}
	
	public String getProperty(String propName)
	{
		return config.getProperty(propName);
	}
	
	public Properties getConfig()
	{
		return this.config;
	}
	
	public void readFromFile(File file)
	{
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			config.load(fis);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(fis != null)
			{
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void readFromURL(URL url)
	{
		InputStream is = null;
		try {
			is = url.openConnection().getInputStream();
			config.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
}
