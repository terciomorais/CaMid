package br.ufpe.cin.middleware.services.cloud;

import java.util.ArrayList;
import java.util.List;

public class ManageInformation {
	private String UId							= null;
	private long timeStamp						= -1;
	private String name						= null;
	private String value						= null;
	private List<String> values				= null;
	private List<ManageInformation> childNodes	= new ArrayList<ManageInformation>();
	
	public ManageInformation(){}
	
	public ManageInformation(String parentUId, String name, String value, long ts){
		this.setTimeStamp(ts);
		this.setName(name);
		this.setValue(value);
		this.setUId(parentUId);
		this.childNodes = new ArrayList<ManageInformation> ();

	}
	
	public String getUId() {
		return UId;
	}
	
	private void setUId(String uId) {
		if(this.isLeaf())
			this.UId = uId + "." + this.getName();
		else
			this.UId = uId + "." + this.getValue();
	}

	private void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public List<String> getValues(){
		return this.values;
	}
	
	public void addValue(String value){
		this.values.add(value);
	}
	public boolean isLeaf() {
		return ((this.childNodes == null) || this.childNodes.isEmpty());
	}
	public List<ManageInformation> getChildNodes() {
		return childNodes;
	}
	public void setChildNodes(List<ManageInformation> childNodes) {
		this.childNodes = childNodes;
	}
	
	public void addChildNode(ManageInformation info){
		this.childNodes.add(info);
	}	
}
