package br.ufpe.cin.in1118.management.domain;

import static java.lang.Thread.sleep;

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

public class CloudManagerService implements ICloudManagerServiceStub {

    @Override
    public boolean serviceIsUp(String service) {
        if (FrontEnd.getInstance().getService(service) != null)
            return true;
        else
            return false;
    }

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
        System.out.println("[CloudManagerService:74] Removing service from LocalRegistry " + serviceName);
        //FrontEnd.getInstance().removeService(serviceName);
        if(FrontEnd.getInstance().getService(serviceName) != null){
            Broker.getNaming().unbind(serviceName, new EndPoint("10.66.67.21:1313")); //TODO: o método deve ser modificado para incluir o IP como parâmetro
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("[CloudManagerservice:82] Error on thread sleep");
            }
        }
        this.scaleDownVM();//TODO: needs to remove
    }
    
    @Override
    public void removeServiceEndpoint(String service, EndPoint endpoint) {
        System.out.println("[CloudManagerService:89] Removing endpoint of service");
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
                System.out.println("\n[CloudManagerService:86] " + ep + " = " + FrontEnd.getInstance().getEndpoint());
                if(!ep.equals(FrontEnd.getInstance().getEndpoint())){
                    nmss = (NodeManagerServiceStub)Broker.getNaming().lookup("NodeManagerService".toLowerCase() + "@" + ep.getHost());
                    nmss.setForwarded(false);
                    SystemDataPoint sdp = nmss.getSystemData();
                    if (sdp != null && sdp.getLastSystemData().getCpuUsage() < 0.5)
                    nodesSystemData.add(sdp);
                }
            }
        } else if(nodeSize > 0){
            System.out.println("[CloudManagerService:96] Tem recursos livres!!!");
            nodesSystemData = new ArrayList<SystemDataPoint>();
        }
        return nodesSystemData;
    }
    
    @Override
    public boolean allocateResource(Analysis analysis) {
        System.out.println("[CloudManagerService:104] Lugar errado");
        FrontEnd.getInstance().getAdaptor(analysis).adapt();
        return true;
    }
    
    @Override
    public void addServiceOnCloud(String serviceName, Stub targetStub) {

        this.scaleUpVM();

/*         targetStub.setFePort(Integer.parseInt((String) Broker.getSystemProps().getProperties().get("port_number")));
        targetStub.setFeHost(Network.recoverAddress("localhost"));
        targetStub.setForwarded(true);
        targetStub.setHost("10.66.66.21");
        targetStub.setPort(1313);
        targetStub.setForwarded(true);
        
        String[] endpoint       = ((String) Broker.getSystemProps().getProperties().get("cloud_list")).split(":");   
        String  sourceCloudHost = endpoint[0];
        
        System.out.println("\n-----------------------------------------------------------------------------");
        System.out.println("[CloudManagerService:134] Binding service in the cloud at IP " + sourceCloudHost);
        System.out.println("-----------------------------------------------------------------------------\n");
        
        NodeManagerServiceStub targetNodeManager = (NodeManagerServiceStub) Broker.getNaming().lookup("nodemanagerservice@10.66.67.21");
        targetNodeManager.addService(serviceName, targetStub); */
    }
    
    private boolean scaleUpVM() {
        //System.out.println("[CloudManagerService:167] method scaleUpVM");
        // set a new node
        VirtualMachine              vm      = null;
        boolean                     flag    = false;
        ResourceController          cms     = new ResourceController();
        Iterator<VirtualMachine>    it      = cms.getVMPool().iterator();

        while (it.hasNext()) {
            vm = it.next();
            System.out.println("[CloudManagerService:176] VM status " + vm.status() + "; VM ID " + vm.getId());
            if (vm.status().equals("unde") || vm.status().equals("stop")) {//vm.status().equals("poff") || 
                flag = true;
                break;
            }
        }
        
        if (flag) {
            OneResponse or = vm.resume();
            if (or.isError()) {
                System.out.println("[CloudManagerService:194] Resume VM has failed");
                return false;
            }
            String vmIP = cms.getVMIP(vm);
            System.out.println("\n--------------------------------------------");
            System.out.println("[CloudManagerService:199] Resuming new VM at IP " + vmIP);
            System.out.println("---------------------------------------------\n");
            try {
                InetAddress inet = InetAddress.getByName(vmIP);
                while (!inet.isReachable(360000));
                if(inet.isReachable(30)){
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
        } else{
            return false;
        }  
    }
    
    private boolean scaleDownVM(){
        ResourceController rc = new ResourceController();
        System.out.println("\n--------------------------------------------");
        System.out.println("[CloudManagerService:225] Undeploying VM 10.66.66.21");
        System.out.println("---------------------------------------------\n");
        return rc.scaleDownVM(1);
    }
}