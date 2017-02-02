package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;

public class RequestHeader implements Serializable{

	private static final long serialVersionUID = -2862742090482565938L;
	private String 				context;
	private int 				requestId;
	private String				serviceName;
	private boolean				responseExpected;
	private int					objectKey;
	private String				operation;

	public RequestHeader(String context, int requestId, boolean responseExpected, int objectKey, String serviceName, String operation) {
		this.setContext(context);
		this.setRequestId(requestId);
		this.setResponseExpected(responseExpected);
		this.setObjectKey(objectKey);
		this.setOperation(operation);
		this.setServiceName(serviceName);
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public boolean isResponseExpected() {
		return responseExpected;
	}

	public void setResponseExpected(boolean responseExpected) {
		this.responseExpected = responseExpected;
	}

	public int getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(int objectKey) {
		this.objectKey = objectKey;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
