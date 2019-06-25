package br.ufpe.cin.in1118.services.commons.naming;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import br.ufpe.cin.in1118.distribution.stub.Stub;
import br.ufpe.cin.in1118.utils.EndPoint;

public interface INaming {
	public void bind(String serviceName, Stub stub) throws UnknownHostException, IOException, Throwable;
	
	public Stub lookup(String serviceName) throws UnknownHostException, IOException, Throwable;
	
	public ArrayList<NameRecord> listRecords() throws UnknownHostException, IOException, Throwable;

	public ArrayList<String> listServiceNames() throws UnknownHostException, IOException, Throwable;
	
	public Map<String, NameRecord> getRegistry() throws UnknownHostException, IOException, Throwable;

	//public void removeEndPoint(String serviceName, EndPoint endpoint);

	public void unbind(String serviceName, EndPoint endpoint) throws UnknownHostException, IOException, Throwable;
}
