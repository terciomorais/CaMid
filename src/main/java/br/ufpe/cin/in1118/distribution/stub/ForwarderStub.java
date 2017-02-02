package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;

import br.ufpe.cin.in1118.protocols.communication.Message;

public class ForwarderStub  extends Stub implements IForwarder {

	private static final long serialVersionUID = -6889314699956595266L;

	public ForwarderStub() {
		super();
	}

	@Override
	public Message forward(Message incomingMessage) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = new Serializable[0];
		super.prepare(clazz, paramValues);
		this.request();
		return (Message) this.reply.getResponse();
	}
}
