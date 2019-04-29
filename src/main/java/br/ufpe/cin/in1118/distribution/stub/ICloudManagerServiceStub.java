package br.ufpe.cin.in1118.distribution.stub;

import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;

public interface ICloudManagerServiceStub {

    public void alert(Analysis analysis);
    public void addService(String service, NameRecord record);
    public void removeService(String service);
}