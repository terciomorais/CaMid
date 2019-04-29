package br.ufpe.cin.in1118.management.domain;

import java.math.BigDecimal;
import java.util.List;

import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;
import br.ufpe.cin.in1118.distribution.stub.ICloudManagerServiceStub;
import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.node.NodeManager;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;

public class CloudManagerService implements ICloudManagerServiceStub {
    
    @Override
    public void alert(Analysis analysis) {
        System.out.println("\n-----------------------------\n  ALERT TRIGGED\n-----------------------------\n");
        System.out.println("Alert type: " + analysis.getAlertMessage());
        System.out.println("Alert Metrics: CPU usage = " + analysis.getSystemDataPoint().getLastSystemData().getCpuUsage());
        if(analysis.isObjectMonitorEnabled()){
            System.out.println("Service metrics: ");
            for(String service : analysis.getServicesMetering().keySet()){
                System.out.println("Service: " + service);
                List<InvokingDataPoint> dp = analysis.getServicesMetering().get(service);
                System.out.println("Last measures (response time): " + dp.get(dp.size() - 1).getStatistics().getAverage());
            }
        }

        if(!analysis.getServices().isEmpty()){
            System.out.println("[CloudManager:28] List of service on Node");
            for(String service : analysis.getServices()){
                System.out.println("                                         :: " + service);
                if(FrontEnd.getInstance().getServices().keySet().contains(service)
                    && !service.equals("NodeManagerService")
                    && !service.equals("CloudManager"))
                    System.out.println("                                          Response time average :: "
                            + new BigDecimal(NodeManager.getInstance().getObjectMonitor().getLastDataPoint(service).getStatistics().getAverage()).toPlainString());
            }
        }
    }
    
    @Override
    public void addService(String service, NameRecord record) {
        System.out.println("[DomainManager:29] Updating service list");
        FrontEnd.getInstance().addService(service, record);
    }
    
    @Override
    public void removeService(String service) {
        System.out.println("[DomainManager:29] Updating service list");
        FrontEnd.getInstance().removeService(service);
    }
    
    
}