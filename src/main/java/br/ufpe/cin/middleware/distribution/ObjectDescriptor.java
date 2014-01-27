package br.ufpe.cin.middleware.distribution;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.middleware.distribution.protocol.RequestParameter;

public class ObjectDescriptor {
	
	private Class<?> classType = null;
	private Parameter method = null;
	private String returnType = "";
	private String serviceName;
	
	private List<RequestParameter> parameters = new ArrayList<RequestParameter>();
	
	public String getReturnType() {
		return returnType;
	}
	
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	public List<RequestParameter> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<RequestParameter> parameters) {
		this.parameters = parameters;
	}
	
	
	public Class<?> getClassType() {
		return classType;
	}
	
	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}
	
	public Parameter getMethod() {
		return method;
	}
	
	public void setMethod(Parameter out) {
		this.method = out;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}	
}


