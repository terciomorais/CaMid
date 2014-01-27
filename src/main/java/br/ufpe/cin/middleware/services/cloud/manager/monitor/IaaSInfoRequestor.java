package br.ufpe.cin.middleware.services.cloud.manager.monitor;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.PoolElement;
import org.opennebula.client.host.Host;
import org.opennebula.client.host.HostPool;
import org.opennebula.client.image.Image;
import org.opennebula.client.image.ImagePool;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;
import org.opennebula.client.vnet.VirtualNetwork;
import org.opennebula.client.vnet.VirtualNetworkPool;

import br.ufpe.cin.middleware.facade.Manager;
import br.ufpe.cin.middleware.facade.Middleware;
import br.ufpe.cin.middleware.facade.MiddlewareConfig;

public class IaaSInfoRequestor {
	private final String ONE_AUTH = "cam:cam2012";
	private final String ONE_XMLRPC = "http://172.20.16.32:2633/RPC2";
	//private final String ONE_XMLRPC = "http://10.8.0.4:2633/RPC2";
	//private final String ONE_XMLRPC = "http://localhost:2633/RPC2";
	
	private Client client					= null;
	private ImagePool imagePool			= null;
	private VirtualMachinePool vmPool 		= null;
	private HostPool hostPool 				= null;
	private VirtualNetworkPool vNetPool	= null;
	private long timeStamp					= -1;
	
	//new Client(ONE_AUTH, ONE_XMLRPC);
	public IaaSInfoRequestor(String resourceType){
		
		MiddlewareConfig config = Manager.getInstance().getConfig();
		try{
			this.client = new Client((String)config.getProperty(MiddlewareConfig.IAAS_USER), 
					(String)config.getProperty(MiddlewareConfig.IAAS_ENDPOINT));
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(resourceType.equals("VM")){
			this.vmPool = new VirtualMachinePool(this.client);	
		//	VirtualMachine vm = new VirtualMachine(81, client);
		}
		else if (resourceType.equals("IMAGE"))
			this.imagePool = new ImagePool(this.client);
		else if (resourceType.equals("HOST"))
			this.hostPool = new HostPool(this.client);
		else if(resourceType.equals("VNET"))
			this.vNetPool = new VirtualNetworkPool(this.client);
		else{
			//TODO: handle error resource type
		}
		this.setTimeStamp();
	}
	public IaaSInfoRequestor(){
		
		try{
			this.client = new Client(MiddlewareConfig.IAAS_USER, MiddlewareConfig.IAAS_ENDPOINT);
		} catch (Exception e) {
			// TODO: handle exception
		}		
/*		this.vmPool 	= new VirtualMachinePool(this.client);
		this.imagePool	= new ImagePool(this.client);
		this.hostPool	= new HostPool(this.client);
*/	
		this.setTimeStamp();
	}
	
	public String getPoolInfo(String resourceType){
		this.setTimeStamp();
		if(resourceType.equals("VM"))
			return this.getVMPooInfo();
		else if (resourceType.equals("IMAGE"))
			return this.getImagePoolInfo();
		else if (resourceType.equals("HOST"))
			return this.getHostPoolInfo();
		else if(resourceType.equals("VNET"))
			return this.getVNetPoolInfo();
		else{
			return null;
		}	
	}
	
	public String getVMInfo(int id){
		this.setTimeStamp();
		VirtualMachine vm = new VirtualMachine(id, this.client);
		if (vm.info().isError()){
			//TODO: handle VM errors
		} 
		return vm.info().getMessage();
	}
	
	public String getVMPooInfo(){
		this.setTimeStamp();
		OneResponse or = this.vmPool.info();
		if(or.isError()){
		//TODO: handle VM Pool errors	
		}
		return or.getMessage();
	}
	
	public String getHostInfo(int id){
		this.setTimeStamp();
		Host h = new Host(id, this.client);
		if(h.info().isError()){
			//TODO: handle host errors
		}
		return h.info().getMessage();
	}
	
	public String getHostPoolInfo(){
		this.setTimeStamp();
		OneResponse or = this.hostPool.info();
		if(or.isError()){
			//TODO: handle Host errors
		}
		return or.getMessage();
	}
	
	public String getImageInfo(int id){
		this.setTimeStamp();
		Image i = (Image)this.imagePool.item(id);
		if(i.info().isError()){
			//TODO: handle Host errors
		}
		return i.info().getMessage();
	}
	
	public String getImagePoolInfo(){
		this.setTimeStamp();
		OneResponse or = this.imagePool.info();
		if(or.isError()){
			//TODO: handle Host errors
		}
		return or.getMessage();
	}
	
	public String getVNetInfo(int id){
		this.setTimeStamp();
		VirtualNetwork vn = (VirtualNetwork)this.vNetPool.item(id);
		if(vn.info().isError()){
			//TODO: handle Host errors
		}
		return vn.info().getMessage();
	}
	
	public String getVNetPoolInfo(){
		this.setTimeStamp();
		OneResponse or = this.vNetPool.info();
		if(or.isError()){
			//TODO: handle Host errors
		}
		return or.getMessage();
	}
	private void setTimeStamp() {
		this.timeStamp = System.currentTimeMillis();
	}
	public long getTimeStamp() {
		return timeStamp;
	}
}












