package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;

public class Parameter implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private Class<?> type;
	private Serializable value;
	
	public Parameter(String name, Class<?> type) {
		super();
		this.setType(type);
		this.setName(name);
		this.setValue(value);
	}
	public Parameter(String name, Class<?> type, Serializable value) {
		super();
		this.setType(type);
		this.setName(name);
		this.setValue(value);
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
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
