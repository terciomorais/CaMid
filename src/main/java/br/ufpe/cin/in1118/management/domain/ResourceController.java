package br.ufpe.cin.in1118.management.domain;

import java.net.InetAddress;
import java.util.Iterator;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

import br.ufpe.cin.in1118.application.remoteObject.Delay;
import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;
import br.ufpe.cin.in1118.distribution.stub.IDomainManagerStub;
import br.ufpe.cin.in1118.infrastructure.client.ClientRequestHandler;
import br.ufpe.cin.in1118.infrastructure.client.ClientSender;
import br.ufpe.cin.in1118.protocols.communication.Message;
import br.ufpe.cin.in1118.protocols.communication.MessageBody;
import br.ufpe.cin.in1118.protocols.communication.MessageHeader;
import br.ufpe.cin.in1118.protocols.communication.Parameter;
import br.ufpe.cin.in1118.protocols.communication.RequestBody;
import br.ufpe.cin.in1118.protocols.communication.RequestHeader;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.utils.Network;

public class ResourceController implements IDomainManagerStub{
	private String ONE_AUTH		= (String)Broker.getSystemProps().getProperties().get("one_auth");
	private String ONE_XMLRPC	= (String)Broker.getSystemProps().getProperties().get("one_xmlrpc");

	private Client 				oneClient	= null;
	private VirtualMachinePool	vmPool		= null;
	private OneResponse 		oneResponse = null;
	
	public ResourceController(){
		try {
			oneClient	= new Client(ONE_AUTH, ONE_XMLRPC);
			vmPool		= new VirtualMachinePool(oneClient);
			oneResponse		= this.vmPool.info();
		} catch (ClientConfigurationException e) {
			e.printStackTrace();
		}
	}

	public ResourceController(String oneAuth, String oneXMLRPC){
		this.ONE_AUTH	= oneAuth;
		this.ONE_XMLRPC	= oneXMLRPC;
		try {
			this.oneClient	= new Client(ONE_AUTH, ONE_XMLRPC);
			this.vmPool		= new VirtualMachinePool(oneClient);
			oneResponse		= this.vmPool.info();

		} catch (ClientConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String getONE_AUTH() {
		return this.ONE_AUTH;
	}

	public String getONE_XMLRPC() {
		return this.ONE_XMLRPC;
	}

	public Client getOneClient() {
		return this.oneClient;
	}

	public VirtualMachinePool getVMPool() {
		if(this.vmPool == null)
			this.vmPool = new VirtualMachinePool(this.oneClient);
		return this.vmPool;
	}

	
	public int searchNode(EndPoint ep){
		boolean flag = false;
		VirtualMachinePool vmp = new VirtualMachinePool(this.oneClient);
		Iterator<VirtualMachine> it = vmp.iterator();
		while(it.hasNext() && !flag){
			VirtualMachine vm = it.next();
			String xml = vm.info().getErrorMessage();
			String ip = xml.substring(xml.indexOf("<ETH0_IP>") + 18, xml.indexOf("</ETH0_IP>") - 3);
			if (ep.getHost().equals(ip))
				return vm.gid();
		}
		return -1;
	}

	public int seachNode(String ip){
		boolean flag = false;
		VirtualMachinePool vmp = new VirtualMachinePool(this.oneClient);
		Iterator<VirtualMachine> it = vmp.iterator();
		while(it.hasNext() && !flag){
			VirtualMachine vm = it.next();
			String xml = vm.info().getErrorMessage();
			String vmIP = xml.substring(xml.indexOf("<ETH0_IP>") + 18, xml.indexOf("</ETH0_IP>") - 3);
			if (ip.equals(vmIP))
				return vm.gid();
		}
		return -1;
	}

	public VirtualMachine getNode(String ip){
		boolean flag = false;
		VirtualMachinePool vmp = new VirtualMachinePool(this.oneClient);
		Iterator<VirtualMachine> it = vmp.iterator();
		while(it.hasNext() && !flag){
			VirtualMachine vm = it.next();
			String xml = vm.info().getErrorMessage();
			String vmIP = xml.substring(xml.indexOf("<ETH0_IP>") + 18, xml.indexOf("</ETH0_IP>") - 3);
			if (ip.equals(vmIP))
				return vm;
		}
		return null;
	}	

	public VirtualMachine getNode(EndPoint ep){
		boolean flag = false;
		VirtualMachinePool vmp = new VirtualMachinePool(this.oneClient);
		Iterator<VirtualMachine> it = vmp.iterator();
		while(it.hasNext() && !flag){
			VirtualMachine vm = it.next();
			String xml = vm.info().getErrorMessage();
			String ip = xml.substring(xml.indexOf("<ETH0_IP>") + 18, xml.indexOf("</ETH0_IP>") - 3);
			if (ep.getHost().equals(ip))
				return vm;
		}
		return null;
	}

	public String getVMIP(VirtualMachine vm){
		String xml = vm.info().getMessage();
		return xml.substring(xml.indexOf("<ETH0_IP>") + 18, xml.indexOf("</ETH0_IP>") - 3);
	}
	
	@Override
	public boolean scale(String action, short alertType) {
		// TODO Auto-generated method stub
		return false;
	}
	//scale level refers to creation of replicas of VMs or remote objects
	public boolean scaleOut(String scaleLevel){
		//long init = System.currentTimeMillis();
		if(scaleLevel.equals("vm")){
			try {
				VirtualMachine vm = new VirtualMachine(4,oneClient);
				String xml = vm.info().getMessage();
				String ip = xml.substring(xml.indexOf("<ETH0_IP>") + 18, xml.indexOf("</ETH0_IP>") - 3);

				InetAddress  inet = InetAddress.getByName(ip);
				OneResponse rc = vm.resume();
			    if( rc.isError() ){
			        System.out.println( "[ResourceController:156] failed!");
			        throw new Exception( rc.getErrorMessage() );
			    }
				while(!inet.isReachable(360000));
				if(inet.isReachable(30)){
					//System.out.println("[ResourceController:161]time spent to be reachable " + (System.currentTimeMillis() - init)/1000 + " ms");
					FrontEnd.getInstance().updateServices();
/*					for(EndPoint ep : FrontEnd.getInstance().getService("delay").getEndPoints()){
						System.out.println("[ResourceController:164] Endpoint " + ep.getEndpoint());
					}*/
				}
			} catch (ClientConfigurationException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(scaleLevel.equals("app")){
			MessageHeader header	= new MessageHeader("request", 0, false, 1, 0);
			RequestHeader reqHeader	= new RequestHeader(
										"br.ufpe.cin.in1118.management.node.NodeManagerService",
										0,
										false,
										0,
										"NodeManagerService",
										Network.recoverAddress("localhost"),
										"addService");
			Parameter[] params		= {new Parameter("serviceName", String.class, "delay"),
					new Parameter("className", Class.class, Delay.class)};
			RequestBody reqBody		= new RequestBody(params);
			MessageBody body		= new MessageBody(reqHeader, reqBody, null, null);
			Message message			= new Message(header, body);
			
			ClientSender sender		= new ClientSender("10.66.66.43", 1313, message);
			ClientRequestHandler.getInstance().submit(sender);
		} else
			System.out.println("[ResourceController:191] Selecting a new VM and starting the remote object");
		return true;
	}

	@Override
	public void addService(String service, NameRecord record) {
		System.out.println("[ResourceController:197] Updating services on CloudManager");
		FrontEnd.getInstance().addService(service, record);
	}

	public boolean scaleDownVM(int vmID){
		try {
			VirtualMachine vm = new VirtualMachine(vmID, oneClient);
			String xml = vm.info().getMessage();
			if(vm.status().equals("runn")){
			OneResponse rc = vm.undeploy(false);
				if( rc.isError() ){
					System.out.println( "[ResourceController:208] Error on turning VM off");
					throw new Exception(rc.getErrorMessage());
				}
			}
		} catch (ClientConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
