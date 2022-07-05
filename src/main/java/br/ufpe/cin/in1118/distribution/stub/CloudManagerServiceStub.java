package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;
import java.util.List;

import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;

public class CloudManagerServiceStub extends Stub implements ICloudManagerServiceStub, Serializable {

  private static final long serialVersionUID = 1L;

  @Override
  public boolean serviceIsUp(String service) {
		class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = {service};
		super.prepare(clazz, paramValues);
		this.request();
		return (boolean) this.reply.getResponse();
  }

  @Override
  public void alert(Analysis analysis) {
    class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = { analysis };
    super.prepare(clazz, paramValues);
    this.reply = this.request();
  }

  @Override
  public void addService(String service, NameRecord record) {
    class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = { service, record };
    super.prepare(clazz, paramValues);
    this.reply = this.request();
  }

  @Override
  public void removeService(String service) {
    class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = { service };
    super.prepare(clazz, paramValues);
    this.reply = this.request();
  }

  @Override
  public void removeServiceEndpoint(String service, EndPoint endpoint) {
    class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = { service, endpoint };
    super.prepare(clazz, paramValues);
    this.reply = this.request();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<SystemDataPoint> requestCloudResources() {
		class Local {};
		Class<?> clazz = Local.class;
		super.prepare(clazz, new Serializable[0]);
		this.request();
		return (List<SystemDataPoint>) this.reply.getResponse();
  }

  @Override
  public boolean allocateResource(Analysis analysis) {
		class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = {analysis};
		super.prepare(clazz, paramValues);
		this.request();
		return (boolean) this.reply.getResponse();
  }

  public void addServiceOnCloud(String serviceName, Stub stub) {
    class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = {serviceName, stub};
    super.prepare(clazz, paramValues);
    this.reply = this.request();
  }
}