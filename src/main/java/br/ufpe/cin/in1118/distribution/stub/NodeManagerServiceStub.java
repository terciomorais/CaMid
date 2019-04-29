package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;

public class NodeManagerServiceStub extends Stub implements INodeManagerService {
	private static final long serialVersionUID = -7634952577627042836L;

	@Override
	public void addService(String serviceName, String className) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = {serviceName, className};
		super.prepare(clazz, paramValues);
		this.reply = this.request();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<InvokingDataPoint>> getServiceData() {
		class Local{};
		Class<?> clazz = Local.class;
		super.prepare(clazz, new Serializable[0]);
		this.reply = this.request();

		return (Map<String, List<InvokingDataPoint>>) this.reply.getResponse();
	}
		
	@Override
	public SystemDataPoint getSystemData() {
		class Local{};
		Class<?> clazz = Local.class;
		super.prepare(clazz, new Serializable[0]);
		this.reply = this.request();

		return (SystemDataPoint) this.reply.getResponse();
	}
}
