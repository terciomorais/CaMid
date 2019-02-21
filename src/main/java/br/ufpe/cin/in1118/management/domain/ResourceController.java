package br.ufpe.cin.in1118.management.domain;

import java.net.InetAddress;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

import br.ufpe.cin.in1118.application.remoteObject.Delay;
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

public class DomainManager implements IDomainManagerStub{
	private String ONE_AUTH		= "oneadmin:gfads!@#";
	private String ONE_XMLRPC	= "http://10.66.66.6:2633/RPC2";
	private Client oneClient;// = new Client(ONE_AUTH, ONE_XMLRPC);
	
	public DomainManager(){
		try {
			oneClient = new Client(ONE_AUTH, ONE_XMLRPC);
		} catch (ClientConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean scale(String action, short alertType) {
		// TODO Auto-generated method stub
		return false;
	}
	//scale level refers to creation of replicas of VMs or remote objects
	public boolean scaleOut(String scaleLevel){
		long init = System.currentTimeMillis();
		if(scaleLevel.equals("vm")){
			try {
				VirtualMachine vm = new VirtualMachine(4,oneClient);
				String xml = vm.info().getMessage();
				String ip = xml.substring(xml.indexOf("<ETH0_IP>") + 18, xml.indexOf("</ETH0_IP>") - 3);

				InetAddress  inet = InetAddress.getByName(ip);
				OneResponse rc = vm.resume();
			    if( rc.isError() ){
			        System.out.println( "failed!");
			        throw new Exception( rc.getErrorMessage() );
			    }
				while(!inet.isReachable(180000));
				if(inet.isReachable(10)){
					//System.out.println("time spent to be reachable " + (System.currentTimeMillis() - init)/1000 + " ms");
					FrontEnd.getInstance().updateServices();
/*					for(EndPoint ep : FrontEnd.getInstance().getService("delay").getEndPoints()){
						System.out.println("[DomainManager:52] Endpoint " + ep.getEndpoint());
					}*/
				}
			} catch (ClientConfigurationException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(scaleLevel.equals("app")){
			MessageHeader header	= new MessageHeader("request", 0, false, 1, 0);
			RequestHeader reqHeader	=
					new RequestHeader("br.ufpe.cin.in1118.management.node.NodeManagerService", 0, false, 0, "NodeManagerService", "addService");
			Parameter[] params		= {new Parameter("serviceName", String.class, "delay"),
					new Parameter("className", Class.class, Delay.class)};
			RequestBody reqBody		= new RequestBody(params);
			MessageBody body		= new MessageBody(reqHeader, reqBody, null, null);
			Message message			= new Message(header, body);
			
			ClientSender sender		= new ClientSender("10.66.66.43", 1313, message);
			ClientRequestHandler.getInstance().submit(sender);
		} else
			System.out.println("Selecting a new VM and starting the remote object");
		return true;
	}

	@Override
	public void addService(String service, NameRecord record) {
		System.out.println("[DomainManager:68] Updating services on DomainManager");
		FrontEnd.getInstance().addService(service, record);
	}
}
