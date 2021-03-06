package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;

import br.ufpe.cin.in1118.services.commons.naming.NameRecord;

public class ResourceControllerStub extends Stub implements IDomainManagerStub{

	private static final long serialVersionUID = 8291223894321404153L;

	public ResourceControllerStub() {
		super();
	}
	
	@Override
	public boolean scaleOut(String scaleLevel) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = {scaleLevel};
		super.prepare(clazz, paramValues);
		this.reply = this.request();
		return (boolean) this.reply.getResponse();
	}

	public boolean scale(String action, short alertType) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = {action, alertType};
		super.prepare(clazz, paramValues);
		this.reply = this.request();
		return (boolean) this.reply.getResponse();
	}
	
	@Override
	public void addService(String service, NameRecord record) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = {service, record};
		super.prepare(clazz, paramValues);
		this.reply = this.request();
	}
}
