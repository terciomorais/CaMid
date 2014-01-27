package br.ufpe.cin.middleware.services.cloud;

import java.util.List;
import org.w3c.dom.Document;
import br.ufpe.cin.middleware.utils.Network;
import br.ufpe.cin.middleware.utils.ParserXML;

public abstract class Resource implements ResourceType{
	private int id									= -1;
	private long timeStamp							= -1;
	private String xmlContent						= null;
	private String context							= null;
	private String domain							= null;
	private String name							= null;
	private String uId								= null;
	private List<ManageInformation> informations	= null;
	private String ipAddress						= null;

	public Resource(){}
	
	public Resource(String xml, String context, long ts){
		this.setTimeStamp(ts);
		this.setXmlContent(xml);
		this.setContext(context);
		this.setName();
		this.setDomain();
		this.setId();
		this.setUId();
	}
	
	public String getXmlContent() {
		return xmlContent;
	}
	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
	public String getDomain() {
		return domain;
	}

	private void setDomain() {
		Document doc = ParserXML.toDocument(this.xmlContent);
		this.domain = doc.getFirstChild().getNodeName();
	}

	public String getUId() {
		return uId;
	}
	private void setUId() {
		this.uId = this.getContext() + "." + this.getDomain() + "." +
			this.getName() + "." +this.getId();
	}
	public int getId() {
		return id;
	}
	private void setId() {
		Document doc = ParserXML.toDocument(this.xmlContent);
		this.id = Integer.parseInt(doc.getElementsByTagName("ID").item(0).getTextContent());
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
	private void setName() {
		Document doc = ParserXML.toDocument(this.xmlContent);
		this.name = doc.getElementsByTagName("NAME").item(0).getTextContent();
	}
	public List<ManageInformation> getInformations() {
		return informations;
	}

	public ManageInformation searchInformationByName(String name){
		if(this.informations != null){
			for(ManageInformation mi : this.informations)
				if(mi.getName().trim().toUpperCase().equals(name.trim().toUpperCase()))
					return mi;
			return null;
		} else{
			return null;
			//TODO: handle information null error
		}
	}
	
	public ManageInformation searchInformationByUId(String uId){
		for(ManageInformation mi : this.informations)
			if(mi.getName().trim().toUpperCase().equals(uId.trim().toUpperCase()))
				return mi;
		return null;
	}

	public void update(Resource r) {
		this.uId = r.getUId();
		this.setTimeStamp(r.getTimeStamp());
		this.setXmlContent(r.getXmlContent());
		this.setIpAddress();
	}

	public String getuId() {
		return uId;
	}
	
	protected void setInformations(List<ManageInformation> mi){
		this.informations = mi;
		//this.setIpAddress();
	}

	public boolean isReachable() {
		return Network.isReachable(this.getIpAddress(), 500);
	}

	public void setIpAddress() {
		if(this.getInformations() != null && !this.getInformations().isEmpty())
			this.ipAddress = (String)this.searchInformationByName("IP_PUBLIC").getValue();
	}

	public String getIpAddress() {
		return ipAddress;
	}
}
