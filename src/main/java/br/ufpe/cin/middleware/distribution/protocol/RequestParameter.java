package br.ufpe.cin.middleware.distribution.protocol;

import java.io.Serializable;

public class RequestParameter {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5428546673066602632L;

	private String name;
	
	private Serializable value;
	
	public RequestParameter(String name, Serializable value) {
		super();
		this.setName(name);
		this.setValue(value);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Serializable getValue() {
		return this.value;
	}

	public void setValue(Serializable value) {
		this.value = value;
	}
	

}
