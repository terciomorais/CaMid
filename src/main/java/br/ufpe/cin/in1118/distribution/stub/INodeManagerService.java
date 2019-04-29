package br.ufpe.cin.in1118.distribution.stub;

import java.util.List;
import java.util.Map;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;

public interface INodeManagerService {
	public void addService(String serviceName, String className);
	public Map<String, List<InvokingDataPoint>> getServiceData();
	public SystemDataPoint getSystemData();
}
