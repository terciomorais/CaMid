package br.ufpe.cin.in1118.distribution.stub;

import br.ufpe.cin.in1118.protocols.communication.Message;

public interface IForwarder{
	Message forward(Message incomingMessage);
}
