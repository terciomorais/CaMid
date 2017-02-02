package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Message implements Serializable{
	private static final long	serialVersionUID	= -1812611879978555383L;
	public static enum ResponseStatus{
		SUCCESS,
		SERVICE_EXCEPTION,
		RECEPTION_EXCEPTION,
		SENDING_EXCEPTION,
		ROUTING_EXCEPTION
	}
	private ResponseStatus	status			= null;
	private String 			statusMessage	= null;
	private MessageHeader	header			= new MessageHeader();
	private MessageBody		body			= new MessageBody();
	private String 			uniqueID 		= UUID.randomUUID().toString();
	private List<String>	routeTrack		= new ArrayList<String>();
	
	public Message(){}
	public Message(MessageHeader header, MessageBody body) {
		this.header	= header;
		this.body	= body;
	}
	
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
	public MessageHeader getHeader() {
		return header;
	}
	public void setHeader(MessageHeader header) {
		this.header = header;
	}
	public MessageBody getBody() {
		return body;
	}
	public void setBody(MessageBody body) {
		this.body = body;
	}
	public String getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	public List<String> getRouteTrack() {
		return routeTrack;
	}
	public void addRouteTrack(String routeTrack) {
		this.routeTrack.add(routeTrack);
	}
}
