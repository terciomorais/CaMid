package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;

import br.ufpe.cin.in1118.utils.Network;

public class InvocationDescriptor implements Serializable{

	private static final long serialVersionUID	= 1L;
	private String			hostIP				= null;
	private String			sourceIP			= null;
	private int				port;
	private int				objectID;
	private String			serviceName			= null;
	private String			remoteClassName		= null;
	private String 			methodName			= null;
	private Parameter[]		parameters			= null;
	private boolean			hasReturn			= true;
	private String 			returnType			= null;
	
	public String getHostIP() {
		return hostIP;
	}
	public void setHostIP(String hostIP) {
		this.hostIP = Network.recoverAddress(hostIP);
	}
	
	public String getSourceIP() {
		return this.sourceIP;
	}
	public void setSourceIP(String ip) {
		this.sourceIP = Network.recoverAddress(ip);
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getObjectID() {
		return objectID;
	}
	public void setObjectID(int objectID) {
		this.objectID = objectID;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String servicename) {
		this.serviceName = servicename;
	}
	public void setParameters(java.lang.reflect.Parameter[] params, Serializable[] paramValues) {
//		if(this.parameters == null)
			this.parameters = new Parameter[params.length];

		for(int idx = 0; idx < params.length; idx++){
			this.parameters[idx] = new Parameter(params[idx].getName(), params[idx].getType(), paramValues[idx]);
		}
	}
	
	public void setParameters(Parameter[] params) {
		this.parameters = params;
	}
	
	public String getRemoteClassName() {
		return remoteClassName;
	}
	public void setRemoteClassName(String remoteObjName) {
		this.remoteClassName = remoteObjName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public boolean hasReturn() {
		return hasReturn;
	}
	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}	
	public Parameter getParameter(int index){
		Parameter result = null;
		if (this.parameters != null && (this.parameters.length != 0))
			result = this.parameters[index];
		return result;
	}
	
	public Parameter[] getParameters(){
		return this.parameters;
	}
}
