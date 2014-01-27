package br.ufpe.cin.middleware.distribution.remote;

import br.ufpe.cin.middleware.distribution.ObjectDescriptor;

public interface RemoteObject {
	
	public String execute(ObjectDescriptor od);
}
