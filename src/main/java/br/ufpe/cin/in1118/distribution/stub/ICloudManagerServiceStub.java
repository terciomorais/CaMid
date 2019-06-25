package br.ufpe.cin.in1118.distribution.stub;

import java.util.List;

import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;
import br.ufpe.cin.in1118.utils.EndPoint;

public interface ICloudManagerServiceStub {

    public void alert(Analysis analysis);
    public void addService(String service, NameRecord record);
    public void removeService(String service);
    public void removeServiceEndpoint(String service, EndPoint endpoint);
    public List<SystemDataPoint> requestCloudResources();
    public boolean allocateResource(Analysis analysis);
    public void addServiceOnCloud(String serviceName, Stub stub);
}