package br.ufpe.cin.middleware.naming;

import br.ufpe.cin.middleware.distribution.stub.Stub;

public class ServiceCreator {

	private Service endpoint;
	
	public ServiceCreator(Service endpoint) {
		super();
		this.endpoint = endpoint;
	}

	public Stub createStub() throws Exception
	{
		try 
		{
			Class<?> c1 = Class.forName("br.ufpe.cin.middleware.distribution.stub." + endpoint.getName() + "Stub");
			try {
				if (c1.getInterfaces()[0].getName().equals("br.ufpe.cin.middleware.distribution.stub.Stub")){
					Stub o = (Stub)c1.newInstance();
					o.setConnection(endpoint.getHost(), endpoint.getPort());
					return o;
				} else {
					throw new Exception( endpoint.getName() + " Stub is not implemented by " + c1.getName());
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getHost()
	{
		return this.endpoint.getHost();
	}
	
	public Integer getPort()
	{
		return this.endpoint.getPort();
	}
}
