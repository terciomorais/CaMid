package br.ufpe.cin.in1118.management.domain;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;
import br.ufpe.cin.in1118.distribution.stub.ICloudManagerServiceStub;
import br.ufpe.cin.in1118.distribution.stub.NodeManagerServiceStub;
import br.ufpe.cin.in1118.distribution.stub.Stub;
import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.management.node.NodeManager;
import br.ufpe.cin.in1118.services.commons.naming.NameRecord;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.utils.Network;

public class CloudManagerService implements ICloudManagerServiceStub {
    
    @Override
    public void alert(Analysis analysis) {
        System.out.println("\n-----------------------------\n  ALERT TRIGGED\n-----------------------------\n");
        System.out.println("Alert type: " + analysis.getAlertMessage());
        System.out.println(
        "Alert Metrics: CPU usage = " + analysis.getSystemDataPoint().getLastSystemData().getCpuUsage());
        if (analysis.isObjectMonitorEnabled()) {
            System.out.println("Service metrics: ");
            for (String service : analysis.getServicesMetering().keySet()) {
                System.out.println("Service: " + service);
                List<InvokingDataPoint> dp = analysis.getServicesMetering().get(service);
                System.out.println(
                "Last measures (response time): " + dp.get(dp.size() - 1).getStatistics().getAverage());
            }
        }
        
        if (!analysis.getServices().isEmpty()) {
            System.out.println("[CloudManager:28] List of service on Node");
            for (String service : analysis.getServices()) {
                System.out.println("                                         :: " + service);
                if (FrontEnd.getInstance().getServices().keySet().contains(service)
                && !service.equals("NodeManagerService") && !service.equals("CloudManager"))
                System.out.println("                                          Response time average :: "
                + new BigDecimal(NodeManager.getInstance().getObjectMonitor().getLastDataPoint(service)
                .getStatistics().getAverage()).toPlainString());
            }
        }
    }
    
    @Override
    public void addService(String service, NameRecord record) {
        FrontEnd.getInstance().addService(service, record);
    }
    
    @Override
    public void removeService(String serviceName) {
        System.out.println("[CloudManagerService:56] Removing service from LocalRegistry " + serviceName);
        FrontEnd.getInstance().removeService(serviceName);
    }
    
    @Override
    public void removeServiceEndpoint(String service, EndPoint endpoint) {
        System.out.println("[CloudManagerService:63] Removing endpoint of service");
        FrontEnd.getInstance().getService(service).removeRemoteEndPoint(endpoint.getEndpoint());
    }
    
    @Override
    public List<SystemDataPoint> requestCloudResources() {
        List<SystemDataPoint> nodesSystemData = null;
        int nodeSize = FrontEnd.getInstance().getProperties().getProperties().containsKey("max_vms")
        ?Integer.parseInt((String)FrontEnd.getInstance().getProperties().getProperties().get("max_vms"))
        :0;
        
        if(nodeSize > 0 && FrontEnd.getInstance().getNodes() != null && !FrontEnd.getInstance().getNodes().isEmpty()){
            nodesSystemData = new ArrayList<SystemDataPoint>();
            NodeManagerServiceStub nmss = null;
            for(EndPoint ep : FrontEnd.getInstance().getNodes()){
                System.out.println("\n[CloudManagerService:77] " + ep + " = " + FrontEnd.getInstance().getEndpoint());
                if(!ep.equals(FrontEnd.getInstance().getEndpoint())){
                    nmss = (NodeManagerServiceStub)Broker.getNaming().lookup("NodeManagerService".toLowerCase() + "@" + ep.getHost());
                    nmss.setForwarded(false);
                    SystemDataPoint sdp = nmss.getSystemData();
                    if (sdp != null && sdp.getLastSystemData().getCpuUsage() < 0.5)
                    nodesSystemData.add(sdp);
                }
            }
        } else if(nodeSize > 0){
            System.out.println("[CloudManagerService:88] Tem recursos livres!!!");
            nodesSystemData = new ArrayList<SystemDataPoint>();
        }
        return nodesSystemData;
    }
    
    @Override
    public boolean allocateResource(Analysis analysis) {
        FrontEnd.getInstance().getAdaptor(analysis).adapt();
        return true;
    }
    
    @Override
    public void addServiceOnCloud(String serviceName, Stub stub) {
        stub.setFePort(Integer.parseInt((String) Broker.getSystemProps().getProperties().get("port_number")));
        stub.setFeHost(Network.recoverAddress("localhost"));
        stub.setForwarded(true);
        stub.setHost("10.66.66.21");
        stub.setPort(1313);
        
        //this.scaleUpVM();
        NodeManagerServiceStub nodeManager = null;
        int attemps = 0;
        do{
            nodeManager = (NodeManagerServiceStub) Broker.getNaming().lookup("NodeManagerService".toLowerCase() + "@10.66.66.21");
            attemps++;
        } while(nodeManager == null && attemps < 3);
        
        if(nodeManager != null){
            nodeManager.setForwarded(false);
            nodeManager.addService(serviceName, stub);
        }
    }
    
    private boolean scaleUpVM() {
        // set a new node
        VirtualMachine              vm      = null;
        boolean                     flag    = false;
        ResourceController          cms     = new ResourceController();
        Iterator<VirtualMachine>    it      = cms.getVMPool().iterator();
        while (it.hasNext()) {
            vm = it.next();
            if (vm.status().equals("poff") || vm.status().equals("unde") || vm.status().equals("stop")) {
                flag = true;
                break;
            }
        }
        
        if (flag) {
            OneResponse or = vm.resume();
            if (or.isError()) {
                System.out.println("[ResourceController:148] Resume VM has failed");
                return false;
            }
            String vmIP = cms.getVMIP(vm);
            System.out.println("\n--------------------------------------------");
            System.out.println("[Adaptor:145] Resuming new VM at IP " + vmIP);
            System.out.println("---------------------------------------------\n");
            try {
                InetAddress inet = InetAddress.getByName(vmIP);
                while (!inet.isReachable(180000));
                if(inet.isReachable(3)){
                    FrontEnd.getInstance().updateServices();
                    return true;
                } else {
                    return false;
                }
            } catch (UnknownHostException uhe) {
                uhe.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else
        return false;
    }
}