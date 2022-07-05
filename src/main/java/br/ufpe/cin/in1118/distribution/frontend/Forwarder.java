package br.ufpe.cin.in1118.distribution.frontend;

import java.net.ConnectException;
import java.net.NoRouteToHostException;

import br.ufpe.cin.in1118.distribution.stub.IForwarder;
import br.ufpe.cin.in1118.infrastructure.client.ClientSender;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;

public class Forwarder implements IForwarder {

	private ClientSender clientObjectSender = null;

	@Override
	public Message forward(Message incomingMessage) {

		String service = incomingMessage.getBody().getRequestHeader().getServiceName();
		if (FrontEnd.getInstance().getService(service).getEndPoints().isEmpty())
			FrontEnd.getInstance().updateServices();

		NameRecord nr = FrontEnd.getInstance().getService(service);

		String[] endpoint = null;
		if (nr.getEndPoints().size() == 1) {
			endpoint = nr.getEndPoints().iterator().next().getEndpoint().split(":");
			// System.out.println("\n[Forwarder:23] Redirecting to single instance " +
			// incomingMessage.getHeader().getMagic());
			clientObjectSender = new ClientSender(endpoint[0], Integer.parseInt(endpoint[1]), incomingMessage);
		} else if (nr.getEndPoints().size() > 1) {
			endpoint = nr.getSchduller().getNextEndPoint().getEndpoint().split(":");
			/*
			 * if(nr.getSchduller().getTax() == 0){ endpoint =
			 * nr.getSchduller().getNextEndPoint().getEndpoint().split(":");
			 * nr.getSchduller().setTax(nr.getSchduller().getTax() + 1); } else {
			 * nr.getSchduller().setTax(nr.getSchduller().getTax() - 1); }
			 */
			/*
			 * System.out.println("\n[Forwader:35] Redirecting service "+ service + " to " +
			 * endpoint[0] + ":" + endpoint[1] + "\n              Incominng message " +
			 * incomingMessage.getHeader().getMagic());
			 */
			clientObjectSender = new ClientSender(endpoint[0], Integer.parseInt(endpoint[1]), incomingMessage);

		} else {
			Message msg = new Message();
			msg.setStatus(Message.ResponseStatus.ROUTING_EXCEPTION);
			msg.setStatusMessage("Error: remote object is not registered on Name Service.");
			return msg;
		}

		Message msg = null;
		msg = this.clientObjectSender.call();
		if(msg.getStatus().equals(Message.ResponseStatus.SENDING_EXCEPTION) || msg.getStatus().equals(Message.ResponseStatus.RECEPTION_EXCEPTION))
			msg.setStatus(Message.ResponseStatus.ROUTING_EXCEPTION);
		return msg;
	}
}
