package br.ufpe.cin.in1118.distribution.server;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.ufpe.cin.in1118.distribution.frontend.Forwarder;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.protocols.communication.MessageBody;
import br.ufpe.cin.in1118.protocols.communication.MessageHeader;
import br.ufpe.cin.in1118.protocols.communication.Parameter;
import br.ufpe.cin.in1118.protocols.communication.ReplyBody;
import br.ufpe.cin.in1118.protocols.communication.ReplyDescriptor;
import br.ufpe.cin.in1118.protocols.communication.ReplyHeader;
import br.ufpe.cin.in1118.services.commons.naming.Naming;

public class Invoker {
	private Message			inUnmarshalledMessage	= null;
	private Message 		outUnmarshalledMessage	= null;
	private ReplyDescriptor	reply 					= new ReplyDescriptor();

	//	LocalServiceRegistry	registry				= null;

	public Invoker() {}

	public Invoker(Message message){
		this.inUnmarshalledMessage = message;
	}

	private Method searchMethod(Message message){
		String		operation			= message.getBody().getRequestHeader().getOperation();
		String		remoteObjectType	=
				this.inUnmarshalledMessage.getBody().getRequestHeader().getContext();
		Class<?>	remoteObjectClass	= null;

		try {
			remoteObjectClass = Class.forName(remoteObjectType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Parameter[]	parameters	= this.inUnmarshalledMessage.getBody().getRequestBody().getParameters();
		Method[]	methods		= remoteObjectClass.getDeclaredMethods();
		Method		targetMethod = null;

		search: {
			for(Method method : methods){
				if(method.getName().equals(operation)
						&& method.getParameterCount() == parameters.length){

					boolean flag = true;
					int i = 0;
					while(flag && i < parameters.length){
						if(parameters[i].getType() != method.getParameterTypes()[i]){
							flag = false;
							break;
						}
						i++;
					}

					if (flag && i == parameters.length){
						targetMethod = method;
						break search;
					}
				}
			}
		}
		return targetMethod;
	}

	public Message invoke() {
		//Checks if message should be processed locally or forwarded
		if(this.inUnmarshalledMessage.getHeader().getMagic().equals("forward")){

			Forwarder forwarder = new Forwarder();
			this.outUnmarshalledMessage = forwarder.forward(this.inUnmarshalledMessage);

		} else {

			String remoteObjectType	=
					this.inUnmarshalledMessage.getBody().getRequestHeader().getContext();
			Parameter[] parameters	=
					this.inUnmarshalledMessage.getBody().getRequestBody().getParameters();

			Serializable[]	paramValues = null;
			if(parameters != null){
				paramValues = new Serializable[parameters.length];
				for(int idx = 0; idx < paramValues.length; idx++)
					paramValues[idx] = parameters[idx].getValue();
			}

			Object remoteObject = null;

			try {
				if(remoteObjectType.contains("Naming"))
					remoteObject = Naming.getInstance();
				else
					remoteObject = Class.forName(remoteObjectType).newInstance();

				Method met = this.searchMethod(this.inUnmarshalledMessage);
				if(paramValues.length == 0)
					reply.setResponse((Serializable) met.invoke(remoteObject));
				else {
					reply.setResponse((Serializable) met.invoke(remoteObject, (Object[])paramValues));
				}
				reply.setStatus(Message.ResponseStatus.SUCCESS);

				this.outUnmarshalledMessage =
						new Message(
								new MessageHeader("response", 0, false, 0, 0),
								new MessageBody(
										this.inUnmarshalledMessage.getBody().getRequestHeader(),
										this.inUnmarshalledMessage.getBody().getRequestBody(),
										new ReplyHeader (this.inUnmarshalledMessage.getBody()
												.getRequestHeader().getContext(), 0, 0),
												new ReplyBody(reply.getResponse())));
				this.outUnmarshalledMessage.setStatus(Message.ResponseStatus.SUCCESS);
			} catch (InstantiationException e) {
				this.outUnmarshalledMessage = new Message();
				this.outUnmarshalledMessage.setStatus(Message.ResponseStatus.SERVICE_EXCEPTION);
				this.outUnmarshalledMessage.setStatusMessage("Instantiation error on Invoker. " + e.getMessage());
				System.out.println("[Invoker:122] Error on server side(Message no. "
						+ this.outUnmarshalledMessage.getUniqueID()
						+ "): Error during instance invocation. " + e.getMessage());
				return this.outUnmarshalledMessage;
			} catch (IllegalAccessException e) {
				this.outUnmarshalledMessage = new Message();
				this.outUnmarshalledMessage.setStatus(Message.ResponseStatus.SERVICE_EXCEPTION);
				this.outUnmarshalledMessage.setStatusMessage("Error on accessing local method by Invoker. "
						+ e.getMessage());
				System.out.println("[Invoker-211] Error on server side(Message no. "
						+ this.outUnmarshalledMessage.getUniqueID()
						+ "): " + e.getMessage());
				return this.outUnmarshalledMessage;
			} catch (ClassNotFoundException e) {
				this.outUnmarshalledMessage = new Message();
				this.outUnmarshalledMessage.setStatus(Message.ResponseStatus.SERVICE_EXCEPTION);
				this.outUnmarshalledMessage.setStatusMessage("Remote class not found locally by Invoker. "
						+ e.getMessage());
				System.out.println("[Invoker-211] Error on server side(Message no. "
						+ this.outUnmarshalledMessage.getUniqueID()
						+ "): " + e.getMessage());
				return this.outUnmarshalledMessage;
			} catch (IllegalArgumentException e) {
				this.outUnmarshalledMessage = new Message();
				this.outUnmarshalledMessage.setStatus(Message.ResponseStatus.SERVICE_EXCEPTION);
				this.outUnmarshalledMessage.setStatusMessage("Illegal argument used on remote object invocation. " 
						+ e.getMessage());
				System.out.println("[Invoker-211] Error on server side(Message no. "
						+ this.outUnmarshalledMessage.getUniqueID()
						+ "): " + e.getMessage());
				return this.outUnmarshalledMessage;
			} catch (InvocationTargetException e) {
				this.outUnmarshalledMessage = new Message();
				this.outUnmarshalledMessage.setStatus(Message.ResponseStatus.SERVICE_EXCEPTION);
				this.outUnmarshalledMessage.setStatusMessage("Error on invocation on Invoker. " + e.getMessage());
				System.out.println("[Invoker-211] Error on server side(Message no. "
						+ this.outUnmarshalledMessage.getUniqueID()
						+ "): " + e.getMessage());
				return this.outUnmarshalledMessage;
			}
		}
		return this.outUnmarshalledMessage;
	}
}