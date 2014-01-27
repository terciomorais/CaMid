package br.ufpe.cin.middleware.services.cloud;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import br.ufpe.cin.middleware.utils.ParserXML;

public class VMResource extends Resource {
	
	// Variables of monitoring
	private List<Integer> state					= new ArrayList<Integer>();
	private List<Integer> lcmState					= new ArrayList<Integer>();
	private List<ManageInformation> memoryUsage	= new ArrayList<ManageInformation>();
	private List<ManageInformation> cpuUsage		= new ArrayList<ManageInformation>();
	private List<ManageInformation> netTX			= new ArrayList<ManageInformation>();
	private List<ManageInformation> netRX			= new ArrayList<ManageInformation>();
	private List<String> msgError					= new ArrayList<String>();
	
	public VMResource(String xml, String context, long ts) {
		super(xml, context, ts);
		this.setInformations();
		// TODO Auto-generated constructor stub
	}
	
	public void setInformations() {
		if (!this.getXmlContent().isEmpty()){
			Document doc = ParserXML.toDocument(this.getXmlContent());
			Node node = doc.getFirstChild();
			this.setInformations(new ArrayList<ManageInformation>());
			for(int i = 0; i < node.getChildNodes().getLength(); i++)
				this.addNode(node.getChildNodes().item(i));	
			this.setIpAddress();
		}
	}
	
	public void addNode(Node n){
		Element e = (Element)n;
		String value = e.getTagName();
		if (n != null)
			if(n.getChildNodes().getLength() == 1)
				if(n.getParentNode().getParentNode().getParentNode() != null){
					this.getInformations().add(new ManageInformation(this.getUId() + 
							"." + n.getParentNode().getNodeName(),
							value, n.getTextContent(), this.getTimeStamp()));
					this.addMonitoringData(this.getInformations().get(this.getInformations().size() - 1), n);
				} else {
					this.getInformations().add(new ManageInformation(this.getUId(),
							value, n.getTextContent(), this.getTimeStamp()));
					this.addMonitoringData(this.getInformations().get(this.getInformations().size() - 1), n);
				}
			else
				for(int i = 0; i < n.getChildNodes().getLength(); i++)
					this.addNode(n.getChildNodes().item(i));
	}
	
	public void addMonitoringData(ManageInformation mi , Node n) {

		if (n.getNodeName().equals("STATE"))
			this.state.add(Integer.parseInt(n.getTextContent()));
		else if (n.getNodeName().equals("LCM_STATE"))
			this.lcmState.add(Integer.parseInt(n.getTextContent()));
		
		if((!mi.getUId().contains("TEMPLATE")) && (!mi.getUId().contains("HISTORY")))
			if (n.getNodeName().equals("MEMORY"))
				this.memoryUsage.add(mi);
			else if(n.getNodeName().equals("CPU"))
				this.cpuUsage.add(mi);
			else if(n.getNodeName().equals("NET_TX"))
				this.netTX.add(mi);
			else if(n.getNodeName().equals("NET_RX"))
				this.netRX.add(mi);	
	}
	
	public void update(VMResource r) {
		super.update(r);
		
		this.state.addAll(r.getState());
		this.lcmState.addAll(r.getLCMState());
		this.memoryUsage.addAll(r.getMemoryUsage());
		this.cpuUsage.addAll(r.getCPUUsage());
		this.netRX.addAll(r.getNetRX());
		this.netTX.addAll(r.getNetTX());
		this.msgError.addAll(r.getMsgError());
	}

	public List<Integer> getState() {
		return state;
	}

	public List<Integer> getLCMState() {
		return lcmState;
	}

	public List<ManageInformation> getMemoryUsage() {
		return memoryUsage;
	}

	public List<ManageInformation> getCPUUsage() {
		return cpuUsage;
	}

	public List<ManageInformation> getNetTX() {
		return netTX;
	}

	public List<ManageInformation> getNetRX() {
		return netRX;
	}

	public List<String> getMsgError() {
		return msgError;
	}
	
	public double getAverage(String resourceName){
		double sum = 0.0;
		if(resourceName.toUpperCase().equals("MEMORY") &&
				!memoryUsage.isEmpty()){
			for(ManageInformation mi : memoryUsage)
				sum =+ Double.parseDouble(mi.getValue());
			return sum/memoryUsage.size();
		} else if(resourceName.toUpperCase().equals("CPU") &&
				!cpuUsage.isEmpty()){
			for(ManageInformation mi : cpuUsage)
				sum =+ Double.parseDouble(mi.getValue());
			return sum/cpuUsage.size();
		} else if(resourceName.toUpperCase().equals("NET_TX") &&
				!netTX.isEmpty()){
			for(ManageInformation mi : netTX)
				sum =+ Double.parseDouble(mi.getValue());
			return sum/netTX.size();
		} else if(resourceName.toUpperCase().equals("NET_RX") &&
				!netRX.isEmpty()){
			for(ManageInformation mi : netRX)
				sum =+ Double.parseDouble(mi.getValue());
			return sum/netRX.size();
		}
		return -1;
	}
}
