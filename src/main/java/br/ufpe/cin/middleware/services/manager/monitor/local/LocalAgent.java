package br.ufpe.cin.middleware.services.manager.monitor.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import br.ufpe.cin.middleware.services.cloud.Resource;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.AtomicDouble;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.SynchronizedMetric;

public class LocalAgent
{
	private static LocalAgent INSTANCE = new LocalAgent();
	
	private Map<String, SynchronizedMetric> remoteObjectRTMMap;
	private Map<String, SynchronizedMetric> remoteObjectRPSMap;
	
	private SynchronizedMetric invokerRTM;
	private SynchronizedMetric invokerRPS;
	
	private String uuid;
	
	public static LocalAgent getInstance() {
		
		return INSTANCE;
	}
	
	private LocalAgent()
	{
		uuid = UUID.randomUUID().toString();
			
		remoteObjectRTMMap = new TreeMap<String, SynchronizedMetric>();
		remoteObjectRPSMap = new TreeMap<String, SynchronizedMetric>();
		remoteObjectFRMap = new TreeMap<String, AtomicLong> ();
		
		invokerRTM = new SynchronizedMetric();
		invokerRPS = new SynchronizedMetric(10);
		
	}

	/* (non-Javadoc)
	 * @see br.ufpe.cin.middleware.services.monitoring.ILocalAgent#getResponseTimeMetricForRemoteObject(java.lang.String)
	 */
	public SynchronizedMetric getResponseTimeMetricForRemoteObject(
			String invocationContext) 
	{
		SynchronizedMetric metric = remoteObjectRTMMap.get(invocationContext);
		if(metric == null)
		{
			metric = new SynchronizedMetric(1000);
			remoteObjectRTMMap.put(invocationContext, metric);
		}
		return metric;
	}
	
	/* (non-Javadoc)
	 * @see br.ufpe.cin.middleware.services.monitoring.ILocalAgent#isAlive()
	 */
	public Boolean isAlive()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see br.ufpe.cin.middleware.services.monitoring.ILocalAgent#getResponseTimeMetricForInvoker()
	 */
	public SynchronizedMetric getResponseTimeMetricForInvoker() 
	{
		return invokerRTM;
	}

	/* (non-Javadoc)
	 * @see br.ufpe.cin.middleware.services.monitoring.ILocalAgent#getRequestsPerSecondMetricForRemoteObject(java.lang.String)
	 */
	public SynchronizedMetric getRequestsPerSecondMetricForRemoteObject(
			String invocationContext) {
		
		SynchronizedMetric metric = remoteObjectRPSMap.get(invocationContext);
		if(metric == null)
		{
			metric = new SynchronizedMetric(10);
			remoteObjectRPSMap.put(invocationContext, metric);
		}
		return metric;
	}

	/* (non-Javadoc)
	 * @see br.ufpe.cin.middleware.services.monitoring.ILocalAgent#getRequestsPerSecondMetricForInvoker()
	 */
	public SynchronizedMetric getRequestsPerSecondMetricForInvoker()
	{
		return invokerRPS;
	}

	private Map<String, AtomicLong> remoteObjectFRMap;
	
	/* (non-Javadoc)
	 * @see br.ufpe.cin.middleware.services.monitoring.ILocalAgent#getFailureRatioForRemoteObject(java.lang.String)
	 */
	public AtomicLong getFailureRatioForRemoteObject(String string) {
		AtomicLong metric = remoteObjectFRMap.get(string);
		if(metric == null)
		{
			metric = new AtomicLong(0);
			remoteObjectFRMap.put(string, metric);
		}
		return metric;
	}
	
	private AtomicLong invocationFR = new AtomicLong(0);
	
	/* (non-Javadoc)
	 * @see br.ufpe.cin.middleware.services.monitoring.ILocalAgent#getFailureRatioForInvoker()
	 */
	public AtomicLong getFailureRatioForInvoker() {
		
		return invocationFR;
	}
	
	
	private AtomicDouble cpuUsage = new AtomicDouble(0);
	
	public AtomicDouble getCPUUsage()
	{
		return cpuUsage;
		
	}
	
	private AtomicDouble memUsage = new AtomicDouble(0);
	
	public AtomicDouble getMemUsage()
	{
		return memUsage;
	}
	
	public Resource getLocalResources()
	{
		
		return null;
		
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public List<String> getFRKeys()
	{
		List<String> frKeys = new ArrayList<String>();
		frKeys.addAll(this.remoteObjectFRMap.keySet());
		
		return frKeys;
	}
	
	public List<String> getRPSKeys()
	{
		List<String> rpsKeys = new ArrayList<String>();
		rpsKeys.addAll(this.remoteObjectRPSMap.keySet());
		
		return rpsKeys;
	}
	
	public List<String> getRTMKeys()
	{
		List<String> rtmKeys = new ArrayList<String>();
		rtmKeys.addAll(this.remoteObjectRTMMap.keySet());
		
		return rtmKeys;
	}
	
}
