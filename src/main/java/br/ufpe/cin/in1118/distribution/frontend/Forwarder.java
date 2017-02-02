package br.ufpe.cin.in1118.distribution.frontend;

import br.ufpe.cin.in1118.distribution.stub.IForwarder;
import br.ufpe.cin.in1118.infrastructure.client.ClientSender;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;

public class Forwarder implements IForwarder{

	private ClientSender clientObjectSender = null;
	
	@Override
	public Message forward(Message incomingMessage) {
		
		String magic = incomingMessage.getBody().getRequestHeader().getServiceName();
		incomingMessage.getHeader().setMagic("request");

		if(FrontEnd.getInstance().getService(magic).getEndPoints().isEmpty())
			FrontEnd.getInstance().updateServices();
		
		NameRecord nr = FrontEnd.getInstance().getService(magic);
		
		if(nr.getEndPoints().size() == 1){
			String[] endpoint = nr.getEndPoints().iterator().next().getEndpoint().split(":");
			clientObjectSender = new ClientSender(endpoint[0],
											Integer.parseInt(endpoint[1]),incomingMessage);
			
		} else if (nr.getEndPoints().size() > 1){
			String[] endpoint = nr.getSchduller().getNextEndPoint().getEndpoint().split(":");
//			System.out.println("[Forwader:29] Forwarding request to " + endpoint[0]);
			clientObjectSender = new ClientSender(endpoint[0],
											Integer.parseInt(endpoint[1]), incomingMessage);
		
		} else {
			Message msg = new Message();
			msg.setStatus(Message.ResponseStatus.ROUTING_EXCEPTION);
			msg.setStatusMessage("Error: remote object is not registered on Name Service.");
			return  msg;
		}

		Message msg = this.clientObjectSender.call();
		if(msg.getStatus().equals(Message.ResponseStatus.SENDING_EXCEPTION) ||
				msg.getStatus().equals(Message.ResponseStatus.RECEPTION_EXCEPTION))
			msg.setStatus(Message.ResponseStatus.ROUTING_EXCEPTION);
		return msg;
	}
}
