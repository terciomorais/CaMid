package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;

public class RequestBody implements Serializable{

	private static final long serialVersionUID = -1229889781907380739L;
	private Parameter[] parameters = null;

	public RequestBody(Parameter[] parameters) {
		this.setParameters(parameters);
	}

	public Parameter[] getParameters() {
		return parameters;
	}
	
	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}
	
	public Object getParameter(int index){
		return this.parameters[index];
	}
}
