package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;

public class ReplyHeader implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4736261116628254136L;
	private String	serviceContext;
	private int		requestId;
	private int		replyStatus;
	
	public ReplyHeader(String serviceContext, int requestId, int replyStatus) {
		this.serviceContext = serviceContext;
		this.requestId = requestId;
		this.replyStatus = replyStatus;
	}
	
	public String getServiceContext() {
		return serviceContext;
	}
	public void setServiceContext(String serviceContext) {
		this.serviceContext = serviceContext;
	}
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public int getReplyStatus() {
		return replyStatus;
	}
	public void setReplyStatus(int replyStatus) {
		this.replyStatus = replyStatus;
	}
}