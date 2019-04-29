package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;

import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;

public class CloudManagerServiceStub extends Stub implements ICloudManagerServiceStub {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public void alert(Analysis analysis) {
    class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = {analysis};
    super.prepare(clazz, paramValues);
    this.reply = this.request();
  }
  
  @Override
  public void addService(String service, NameRecord record) {
    class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = {service, record};
    super.prepare(clazz, paramValues);
    this.reply = this.request();
  }
  
  @Override
  public void removeService(String service) {
    class Local {};
    Class<?> clazz = Local.class;
    Serializable[] paramValues = {service};
    super.prepare(clazz, paramValues);
    this.reply = this.request(); 
  }
  
  
}