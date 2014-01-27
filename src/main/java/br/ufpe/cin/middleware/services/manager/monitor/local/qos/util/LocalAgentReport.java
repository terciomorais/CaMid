package br.ufpe.cin.middleware.services.manager.monitor.local.qos.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import br.ufpe.cin.middleware.services.cloud.ManageInformation;
import br.ufpe.cin.middleware.services.cloud.ResourceInformation;
import br.ufpe.cin.middleware.services.manager.monitor.local.LocalAgent;

public class LocalAgentReport {
	
	private LocalAgent localAgent;
	
	
	public LocalAgentReport(LocalAgent localAgent)
	{
		this.localAgent = localAgent;
	}
	
	private static UUID invokerUUID = UUID.randomUUID();
	
	private static Map<String,UUID> remoteObjectUUID = Collections.synchronizedMap(new TreeMap<String,UUID> ());

	public ManageInformation fetchMemoryManageInformation() {
		Double usage = this.localAgent.getMemUsage().get();
		
		ManageInformation mi = buildDefault();
		
		mi.setName("MemUsage");
		mi.setValue(usage.toString());
		
		return mi;
	}

	public ManageInformation fetchCpuManageInformation()
	{
		Double usage = this.localAgent.getCPUUsage().get();
		
		ManageInformation mi = buildDefault();
		
		mi.setName("CpuUsage");
		mi.setValue(usage.toString());
		
		return mi;
	}
	
	public ResourceInformation fetchSystemMetrics()
	{
		List<ManageInformation> metricList = new ArrayList<ManageInformation>();
		
		metricList.add(fetchCpuManageInformation());
		metricList.add(fetchMemoryManageInformation());
		
		ResourceInformation resourceInfo = new ResourceInformation(
				ResourceInformation.LOCAL_CONTEXT,
				ResourceInformation.DOMAIN_VM,
				"System",
				invokerUUID.toString());
		
		resourceInfo.setInformation(metricList);
		
		return resourceInfo;
	}
	
	public ResourceInformation fetchInvokerMetrics()
	{
		List<ManageInformation> metricList = new ArrayList<ManageInformation>();
		
		Double fr = Double.longBitsToDouble(this.localAgent.getFailureRatioForInvoker().get());
		Double rps = this.localAgent.getRequestsPerSecondMetricForInvoker().average();
		Double rtm = this.localAgent.getResponseTimeMetricForInvoker().average();
		
		ResourceInformation resourceInfo = new ResourceInformation(
				ResourceInformation.LOCAL_CONTEXT,
				ResourceInformation.DOMAIN_APP,
				"Invoker",
				invokerUUID.toString());
		
		
		long timestamp = System.currentTimeMillis();
		
		metricList.add(new ManageInformation(resourceInfo.getuId(), "FailureRate", fr.toString(), timestamp));
		metricList.add(new ManageInformation(resourceInfo.getuId(), "ResponseTime", rtm.toString(), timestamp));
		metricList.add(new ManageInformation(resourceInfo.getuId(), "RequestsPerSecond", rps.toString(), timestamp));
		
		resourceInfo.setInformation(metricList);
		
		return resourceInfo;
		
	}
	
	
	public ResourceInformation fetchRemoteObjectMetrics(String invocationContext)
	{
		List<ManageInformation> metricList = new ArrayList<ManageInformation>();
		
		Double fr = Double.longBitsToDouble(this.localAgent.getFailureRatioForRemoteObject(invocationContext).get());
		Double rps = this.localAgent.getRequestsPerSecondMetricForRemoteObject(invocationContext).average();
		Double rtm = this.localAgent.getResponseTimeMetricForRemoteObject(invocationContext).average();
		
		UUID uuid = null;
		
		if( (uuid = remoteObjectUUID.get(invocationContext)) == null)
		{
			uuid = UUID.randomUUID();
			remoteObjectUUID.put(invocationContext, uuid);
		}
				
		ResourceInformation resourceInfo = new ResourceInformation(
				ResourceInformation.LOCAL_CONTEXT,
				ResourceInformation.DOMAIN_APP,
				invocationContext,
				uuid.toString());
		
		
		long timestamp = System.currentTimeMillis();
		
		metricList.add(new ManageInformation(resourceInfo.getuId(), "FailureRate", fr.toString(), timestamp));
		metricList.add(new ManageInformation(resourceInfo.getuId(), "ResponseTime", rtm.toString(), timestamp));
		metricList.add(new ManageInformation(resourceInfo.getuId(), "RequestsPerSecond", rps.toString(), timestamp));
		
		resourceInfo.setInformation(metricList);
		
		return resourceInfo;
	}
	
	public List<ResourceInformation> fetchResourceInformationForAllRemoteObjects()
	{
		List<String> invocationContexts = localAgent.getFRKeys();
		List<ResourceInformation> remoteObjectResourceInfo = new ArrayList<ResourceInformation>();
		for(String context : invocationContexts )
		{
			remoteObjectResourceInfo.add(fetchRemoteObjectMetrics(context));
		}
		
		return remoteObjectResourceInfo;
	}
	
	private ManageInformation buildDefault()
	{
		return new ManageInformation();
	}
	

}
