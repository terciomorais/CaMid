package br.ufpe.cin.middleware.distribution.protocol;

import java.io.Serializable;


public class MethodResponseMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1108283228335725908L;

	public enum ResponseStatus{
		SUCCESS,
		SERVICE_EXCEPTION,
		RECEPTION_EXCEPTION
	}
	
	private ResponseStatus status;
	
	private String statusMessage;
	
	private Serializable response;

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Serializable getResponse() {
		return response;
	}

	public void setResponse(Serializable response) {
		this.response = response;
	}

}
