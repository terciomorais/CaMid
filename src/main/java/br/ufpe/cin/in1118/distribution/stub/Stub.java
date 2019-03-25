package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;

import br.ufpe.cin.in1118.distribution.client.Requestor;
import br.ufpe.cin.in1118.protocols.communication.InvocationDescriptor;
import br.ufpe.cin.in1118.protocols.communication.ReplyDescriptor;
import br.ufpe.cin.in1118.utils.Network;

public abstract class Stub implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected String				host;
	protected int 					port;
	protected String				feHost;
	protected int					fePort;
	protected boolean				forwarded			= false;
	protected int 					objectId;
	protected String				className;
	protected String 				serviceName;
	protected Serializable[]		parameterValues;
	protected InvocationDescriptor	invocation			= new InvocationDescriptor();
	protected Requestor				requestor;
	protected ReplyDescriptor		reply 				= null;
	
	public String getEndpoint(){
		return this.host + ":" + this.port;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String objectName) {
		this.className = objectName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Serializable[] getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(Serializable[] parameterValues) {
		this.parameterValues = parameterValues;
	}

	public InvocationDescriptor getInvocation() {
		return invocation;
	}
	public void setInvocation(InvocationDescriptor invocation) {
		this.invocation = invocation;
	}
	public Requestor getRequestor() {
		return requestor;
	}
	public void setRequestor(Requestor requestor) {
		this.requestor = requestor;
	}
	public ReplyDescriptor getReply() {
		return reply;
	}
	public void setReply(ReplyDescriptor reply) {
		this.reply = reply;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = Network.recoverAddress(host);
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getFeHost() {
		return feHost;
	}
	public void setFeHost(String feHost) {
		this.feHost = feHost;
	}
	public int getFePort() {
		return fePort;
	}
	public void setFePort(int fePort) {
		this.fePort = fePort;
	}
	public boolean isForwarded() {
		return forwarded;
	}
	public void setForwarded(boolean forwarded) {
		this.forwarded = forwarded;
	}
	public int getObjectId() {
		return objectId;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public void prepare(Class<?> clazz, Serializable[] parameterValues){
		this.requestor	= new Requestor();
		
		if(this.forwarded){
			this.invocation.setHostIP(this.getFeHost());
			this.invocation.setPort(this.getFePort());
		} else {
			this.invocation.setHostIP(this.getHost());
			this.invocation.setPort(this.getPort());
		}
		this.invocation.setSourceIP("localhost");
		this.invocation.setObjectID(this.getObjectId());
		this.invocation.setServiceName(this.getServiceName());
		this.invocation.setRemoteClassName(this.getClassName());
		this.invocation.setMethodName(clazz.getEnclosingMethod().getName());
		
		if(parameterValues != null)
			this.invocation.setParameters(clazz.getEnclosingMethod().getParameters(), parameterValues);
		if(clazz.getEnclosingMethod().getReturnType().equals(Void.TYPE))
			this.invocation.setHasReturn(false);
		else
			this.invocation.setHasReturn(true);
	}
	
	public ReplyDescriptor request(){
			this.reply = this.requestor.invoke(invocation, this.forwarded);
		return this.reply;
	}
}