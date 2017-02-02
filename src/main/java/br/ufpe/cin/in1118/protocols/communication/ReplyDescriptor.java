package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;

public class ReplyDescriptor implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Message.ResponseStatus	status;
	private String			statusMessage;
	private Serializable	response;
	
	public Message.ResponseStatus getStatus() {
		return status;
	}
	public void setStatus(Message.ResponseStatus status) {
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
