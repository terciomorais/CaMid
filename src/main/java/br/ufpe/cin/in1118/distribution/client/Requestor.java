package br.ufpe.cin.in1118.distribution.client;

import java.io.Serializable;
import br.ufpe.cin.in1118.infrastructure.client.ClientSender;
import br.ufpe.cin.in1118.infrastructure.client.ClientRequestHandler;
import br.ufpe.cin.in1118.protocols.communication.InvocationDescriptor;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.protocols.communication.MessageBody;
import br.ufpe.cin.in1118.protocols.communication.MessageHeader;
import br.ufpe.cin.in1118.protocols.communication.ReplyDescriptor;
import br.ufpe.cin.in1118.protocols.communication.RequestBody;
import br.ufpe.cin.in1118.protocols.communication.RequestHeader;

public class Requestor implements Serializable{

	private static final long serialVersionUID = 4227212500799083422L;

	public ReplyDescriptor invoke (InvocationDescriptor inv, boolean forward){// throws UnknownHostException, IOException, Throwable{
		
		ClientRequestHandler	clientRequestHandler	= ClientRequestHandler.getInstance();
		Message 				messageToBeMarshelled 	= null;		
		Message					replyUnmarshalled		= null;
		ReplyDescriptor			reply					= null;

		//map
		RequestHeader	requestHeader	= new RequestHeader(
											inv.getRemoteClassName(),
											0,
											inv.hasReturn(),
											0,
											inv.getServiceName(),
											inv.getSourceIP(),
											inv.getMethodName());
		RequestBody		requestBody 	= new RequestBody(inv.getParameters());
		
		String context = (forward) ? "forward" : "request";

		MessageHeader	messageHeader	= new MessageHeader(context, 0, false, 0, 0);
		MessageBody		messageBody 	= new MessageBody(requestHeader, requestBody, null, null);
		
		messageToBeMarshelled = new Message(messageHeader, messageBody);
		ClientSender clientSender = new ClientSender(inv.getHostIP(), inv.getPort(), messageToBeMarshelled);

		replyUnmarshalled = clientRequestHandler.submit(clientSender);
		reply = new ReplyDescriptor();
		reply.setStatus(replyUnmarshalled.getStatus());
		reply.setStatusMessage(replyUnmarshalled.getStatusMessage());
		
		if(inv.hasReturn())	
			reply.setResponse((Serializable)replyUnmarshalled.getBody().getReplyBody().getOperationResult());
		return reply;
	}
}
