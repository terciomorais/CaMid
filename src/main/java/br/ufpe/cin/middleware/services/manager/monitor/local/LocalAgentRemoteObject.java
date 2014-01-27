package br.ufpe.cin.middleware.services.manager.monitor.local;

import java.util.ArrayList;
import java.util.List;
import br.ufpe.cin.middleware.services.cloud.ResourceInformation;
import br.ufpe.cin.middleware.services.manager.monitor.local.qos.util.LocalAgentReport;

public class LocalAgentRemoteObject 
{
	private LocalAgent localAgent = LocalAgent.getInstance();
	
	public static String SERVICE_NAME = "LocalAgent";

	public Double getResponseTimeMetricForRemoteObject(
			String invocationContext) {
		return localAgent.getResponseTimeMetricForRemoteObject(invocationContext).average();
	}

	public Boolean isAlive() {
		return localAgent.isAlive();
	}

	public Double getResponseTimeMetricForInvoker() {
		return localAgent.getResponseTimeMetricForInvoker().average();
	}

	public Double getRequestsPerSecondMetricForRemoteObject(
			String invocationContext) {
		return localAgent.getRequestsPerSecondMetricForRemoteObject(invocationContext).average();
	}

	public Double getRequestsPerSecondMetricForInvoker() {
		return localAgent.getRequestsPerSecondMetricForInvoker().average();
	}

	public Double getFailureRatioForRemoteObject(String string) {
		return Double.longBitsToDouble(localAgent.getFailureRatioForRemoteObject(string).get());
	}

	public Double getFailureRatioForInvoker() {
		return Double.longBitsToDouble(localAgent.getFailureRatioForInvoker().get());
	}
	
	public Double getCPUUsage()
	{
		return localAgent.getCPUUsage().get();
	}
	
	public Double getMemUsage()
	{
		return localAgent.getMemUsage().get();
	}
	
	public List<ResourceInformation> getLocalAgentReport()
	{
		List<ResourceInformation> resourceInformation = new ArrayList<ResourceInformation>();
		LocalAgentReport report = new LocalAgentReport(localAgent);
		
		resourceInformation.add(report.fetchSystemMetrics());
		resourceInformation.add(report.fetchInvokerMetrics());
		resourceInformation.addAll(report.fetchResourceInformationForAllRemoteObjects());
		
		return resourceInformation;
	}
	
//	public String execute(ObjectDescriptor od) {
//		
//		String methodName = od.getMethod().getValue();
//		List<Parameter> params = od.getParameters();
//
//		String result = null;
//		if (methodName.equals("getResponseTimeMetricForRemoteObject"))
//		{
//			String invocationContext = params.get(0).getValue();
//			result = "" + localAgent.getResponseTimeMetricForRemoteObject(invocationContext).average();
//		}
//		if (methodName.equals("getResponseTimeMetricForInvoker"))
//		{
//			result = "" + localAgent.getResponseTimeMetricForInvoker().average();
//		}
//		if (methodName.equals("getRequestsPerSecondMetricForRemoteObject"))
//		{
//			String invocationContext = params.get(0).getValue();
//			result = "" + localAgent.getRequestsPerSecondMetricForRemoteObject(invocationContext).average();
//		}
//		if (methodName.equals("getRequestsPerSecondMetricForInvoker"))
//		{
//			result = "" + localAgent.getRequestsPerSecondMetricForInvoker().average();
//		}
//		if (methodName.equals("getFailureRatioForRemoteObject"))
//		{
//			String invocationContext = params.get(0).getValue();
//			result = "" + Double.longBitsToDouble(localAgent.getFailureRatioForRemoteObject(invocationContext).get());
//		}
//		if (methodName.equals("getFailureRatioForInvoker"))
//		{
//			result = "" + Double.longBitsToDouble(localAgent.getFailureRatioForInvoker().get());
//		}
//		
//		System.out.println("O resultado no objeto remoto: " + result);
//		return result;
//	}

}
