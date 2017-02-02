package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;

public class ParameterBK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9069101238140247870L;

	private String		argument	= null;
	private Class<?>	type		= null;
	
	public ParameterBK(String argument, Class<?> clazz){
		this.argument	= argument;
		this.type		= clazz;
	}

	public String getArgument() {
		return argument;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}
}
