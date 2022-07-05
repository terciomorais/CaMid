package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import br.ufpe.cin.in1118.services.commons.naming.INaming;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;
import br.ufpe.cin.in1118.utils.EndPoint;

public class NamingStub extends Stub implements INaming {

	private static final long serialVersionUID = 1L;

	private final String className = "br.ufpe.cin.in1118.services.commons.naming.Naming";

	public NamingStub(String host, int port) {
		super();
		this.setHost(host);
		this.setPort(port);
	}

	@Override
	public void bind(String serviceName, Stub stub) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = { serviceName, stub };
		this.prepare(clazz, paramValues);
		this.getInvocation().setRemoteClassName(this.className);
		this.reply = this.request();
	}

	@Override
	public Stub lookup(String serviceName) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = { serviceName };
		this.prepare(clazz, paramValues);
		this.getInvocation().setRemoteClassName(this.className);
		this.reply = this.request();
		return (Stub) this.reply.getResponse();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<NameRecord> listRecords() {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = new Serializable[0];
		this.prepare(clazz, paramValues);
		this.getInvocation().setRemoteClassName(this.className);
		this.reply = this.request();
		return (ArrayList<NameRecord>) this.reply.getResponse();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> listServiceNames() {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = new Serializable[0];
		this.prepare(clazz, paramValues);
		this.getInvocation().setRemoteClassName(this.className);
		this.reply = this.request();
		return (ArrayList<String>) this.reply.getResponse();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, NameRecord> getRegistry() {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = new Serializable[0];
		this.prepare(clazz, paramValues);
		this.getInvocation().setRemoteClassName(this.className);
		this.reply = this.request();
		return (Map<String, NameRecord>) this.reply.getResponse();
	}

/* 	@Override
	public void removeEndPoint(String serviceName, EndPoint endpoint) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = { serviceName, endpoint };
		this.prepare(clazz, paramValues);
		this.getInvocation().setRemoteClassName(this.className);
		this.reply = this.request();
	} */

	@Override
	public void unbind(String serviceName, EndPoint endpoint) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = { serviceName, endpoint };
		this.prepare(clazz, paramValues);
		this.getInvocation().setRemoteClassName(this.className);
		this.reply = this.request();
	}
}
