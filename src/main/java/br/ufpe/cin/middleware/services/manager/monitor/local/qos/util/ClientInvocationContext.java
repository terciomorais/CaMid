package br.ufpe.cin.middleware.services.manager.monitor.local.qos.util;

public class ClientInvocationContext implements Comparable<ClientInvocationContext>{

	private String serviceName;
	
	private String method;

	public static final String SEPARATOR = "."; 
	
	public ClientInvocationContext() {
		
	}
	
	public ClientInvocationContext(String rawString) {
		super();
		this.fromString(rawString);
	}
	
	public ClientInvocationContext(String serviceName, String method) {
		super();
		this.serviceName = serviceName;
		this.method = method;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public String toString()
	{
		return method != null ? serviceName+SEPARATOR+method : serviceName + SEPARATOR;
	}
	
	public void fromString(String str)
	{
		String split[] = str.split(SEPARATOR);
		this.serviceName = split[0];
		if(split.length > 1)
			this.method = split[1];
	}
	
	public static void main(String[] args) 
	{
		ClientInvocationContext cic = new ClientInvocationContext("Calculadora", "soma");
		String cicStr = cic.toString();
		System.out.println(cicStr);
		
		ClientInvocationContext cic2 = new ClientInvocationContext(cicStr);
		System.out.println(cic2);
		
		ClientInvocationContext coic = new ClientInvocationContext("Calculadora", null);
		String coicStr = coic.toString();
		System.out.println(coicStr);
		
		ClientInvocationContext coic2 = new ClientInvocationContext(coicStr);
		System.out.println(coic2);
		System.out.println(coic2.method);
		
	}

	public int compareTo(ClientInvocationContext arg0) {
		
		int comparison = 0;
		
		comparison = this.serviceName.compareTo(arg0.serviceName);
		
		if(comparison == 0)
			if(method != null && arg0.method != null)
				comparison = this.method.compareTo(arg0.method);
			else if( method == null && arg0.method == null)
				; // DO NOTHING
			else if( method == null )
				comparison = -1;
			else
				comparison = 1;
		
		
		return comparison;
	}
	
	
	
}
//