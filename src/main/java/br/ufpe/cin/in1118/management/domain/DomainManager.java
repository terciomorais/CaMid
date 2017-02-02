package br.ufpe.cin.in1118.management.domain;

import java.net.InetAddress;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;
import br.ufpe.cin.in1118.distribution.stub.IDomainManagerStub;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;
import br.ufpe.cin.in1118.utils.EndPoint;

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
	
	//scale level refers to creation of replicas of VMs or remote objects
	public boolean scaleOut(String scaleLevel){
		System.out.println("[DomainManager:30] Somebody is asking for resources");
		long init = System.currentTimeMillis();
		if(!scaleLevel.equals("vm")){
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
					System.out.println("time spent to be reachable " + (System.currentTimeMillis() - init)/1000 + " ms");
					FrontEnd.getInstance().updateServices();
					for(EndPoint ep : FrontEnd.getInstance().getService("delay").getEndPoints()){
						System.out.println("[DomainManager:52] Endpoint " + ep.getEndpoint());
					}
				}
			} catch (ClientConfigurationException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
