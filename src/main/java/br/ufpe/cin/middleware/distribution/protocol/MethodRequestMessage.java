package br.ufpe.cin.middleware.distribution.protocol;

import java.util.ArrayList;
import java.util.List;

public class MethodRequestMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3999883611902898693L;

	private String senderHost;
	private String host;
	private int port;
	private String serviceName;
	private String method;
	private List<RequestParameter> parameters;
	
	public MethodRequestMessage(){
		parameters = new ArrayList<RequestParameter>();
	}
	
	public String getSenderHost() {
		return senderHost;
	}

	public void setSenderHost(String senderHost) {
		this.senderHost = senderHost;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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

	public List<RequestParameter> getParameters() {
		return parameters;
	}	
}
