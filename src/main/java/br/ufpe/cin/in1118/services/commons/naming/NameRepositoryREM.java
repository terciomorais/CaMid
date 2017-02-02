package br.ufpe.cin.in1118.services.commons.naming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NameRepositoryREM {
	
	private Map<String, NameRecord> repository = new HashMap<String, NameRecord>();
	
	public NameRepositoryREM(){}
	
	public synchronized void addRecord(String serviceName, NameRecord namingRecord){
		if(this.repository.containsKey(serviceName))
			this.repository.get(serviceName).addRemoteEndPoint(namingRecord.getStub().getHost());
		else
			this.repository.put(serviceName, namingRecord);
	}
	
	public synchronized void removeRecord(String serviceName){
		this.repository.remove(serviceName);
	}
	
	public NameRecord find(String serviceName){
		NameRecord target = this.repository.get(serviceName);
		return target;
	}
	
	public synchronized void clearRecord(){
		this.repository.clear();
	}
	
	public ArrayList<NameRecord> getList(){
		return (ArrayList<NameRecord>) this.repository.values();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getKeys(){
		return (ArrayList<String>) this.repository.keySet();
	}
}
