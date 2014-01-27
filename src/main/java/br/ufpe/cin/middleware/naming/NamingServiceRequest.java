package br.ufpe.cin.middleware.naming;

import java.io.Serializable;

public class NamingServiceRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3030696290515324060L;

	public enum NamingServiceOperation{
		REGISTER,
		UNREGISTER,
		SEARCH,
		LIST
	}

	private NamingServiceOperation operation;
	
	private Service service;
	
	private String requestedService;
	
	public NamingServiceRequest(NamingServiceOperation operation,
			Service service) {
		super();
		this.operation = operation;
		this.service = service;
		this.requestedService = null;
	}
	
	public NamingServiceRequest(String requestedService) {
		super();
		
		this.setOperation(NamingServiceOperation.SEARCH);
		
		//Provisorio para testes. Procurar solução mais elegante.
		if(requestedService.equals("ALL_SERVICES"))
				this.setOperation(NamingServiceOperation.LIST);

		this.service = null;
		this.setRequestedService(requestedService);
	}

	public NamingServiceOperation getOperation() {
		return operation;
	}

	public void setOperation(NamingServiceOperation operation) {
		this.operation = operation;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getRequestedService() {
		return requestedService;
	}

	public void setRequestedService(String requestedService) {
		this.requestedService = requestedService;
	}
	
}
