package br.ufpe.cin.middleware.distribution.stub;

import br.ufpe.cin.middleware.distribution.protocol.MethodRequestMessage;

public interface Stub {
	void setConnection(String host, int port);
	void send(MethodRequestMessage message);
	Object receive();
	void close();
}
