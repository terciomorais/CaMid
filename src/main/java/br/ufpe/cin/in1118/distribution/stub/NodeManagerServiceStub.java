package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;

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

}
