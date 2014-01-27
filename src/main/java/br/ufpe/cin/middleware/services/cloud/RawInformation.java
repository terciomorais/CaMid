package br.ufpe.cin.middleware.services.cloud;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import br.ufpe.cin.middleware.utils.ParserXML;

public class RawInformation {
	
	private final String CONTEXT		= "GLOBAL";
	
	private long timeStamp				= -1;
	private String resourceSpec		= null;
	private Document doc				= null;
	private String xml					= null;
	private List<Resource> resources	= new ArrayList<Resource>();
	
	private void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public RawInformation(){}
	
	public RawInformation(String xml, String resourceSpec, long timeStamp){	
		this.setXml(xml);
		this.setDoc();
		this.setResourceSpec(resourceSpec);
		this.setResources();
		this.setTimeStamp(timeStamp);
	}
	
	public String getResourceSpec() {
		return resourceSpec;
	}
	
	public void setResourceSpec(String resourceSpec) {
		this.resourceSpec = resourceSpec;
	}

	public Document getDoc() {
		return doc;
	}
	
	private void setDoc(){
		this.doc = ParserXML.toDocument(this.getXml());
	}
	
	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
	
	public void setResources(){
		NodeList nodes = this.doc.getFirstChild().getChildNodes();
		if(this.getResourceSpec().equals("VM"))
			for (int i = 0; i < nodes.getLength(); i++){
				VMResource r = new VMResource(ParserXML.toString(nodes.item(i)),
						this.CONTEXT, this.getTimeStamp());
				int index = this.searchResourceByName(r.getName());
				if (index < 0)
					this.resources.add(r);
				else {
					if((boolean)this.resources.get(index).isReachable())
						((VMResource)this.resources.get(index)).update(r);
				}
/*			System.out.println("[Raw Information] CPUUsage");
			for(ManageInformation mi : ((VMResource)this.resources.get(0)).getCPUUsage())
				System.out.println("	. " + mi.getName() + " - " + mi.getValue());
*/			}
		else if(this.getResourceSpec().equals("HOST")){}
	}
	
	public List<Resource> getResources(){
		return this.resources;
	}
	
	public int searchResourceByName(String name){
		for(Resource r : this.resources){
			if(r.getName().equals(name))
				return this.resources.indexOf(r);
		}
		return -1;
	}

	public void update(long timeStamp, String xml) {
		this.setXml(xml);
		this.setDoc();
		this.setResources();
		this.setTimeStamp(timeStamp);
	}

}
