package br.ufpe.cin.in1118.management;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;
import br.ufpe.cin.in1118.distribution.stub.CloudManagerServiceStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.distribution.stub.NodeManagerServiceStub;
import br.ufpe.cin.in1118.distribution.stub.Stub;
import br.ufpe.cin.in1118.management.analysing.Analysis;
import br.ufpe.cin.in1118.management.domain.ResourceController;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.management.node.ClientCloudManager;
import br.ufpe.cin.in1118.management.node.NodeManager;
import br.ufpe.cin.in1118.utils.EndPoint;

public class Adaptor {

    private Analysis analysis = null;

    public Adaptor() {
    }

    public Adaptor(Analysis analysis) {
        this.analysis = analysis;
    }

    public Analysis getAnalysis() {
        return this.analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public void adapt() {
        if (this.analysis.getServiceAlert()) {
            NodeManager.getInstance().getObjectAnalyser().setPaused(false);
            if (this.analysis.getAlertMessage().equals("response time overload")) {
                //this.scaleUpVM();
                //this.scaleUpApp(this.analysis.getService(), "10.66.66.12"); 
                this.scaleUpCloud(this.analysis.getService());
            } else if(this.analysis.getAlertMessage().equals("response time underload")
                    && FrontEnd.getInstance().getService(this.analysis.getService()).hasReplicas()){
                //this.scaleDownApp(this.analysis.getService(), "10.66.66.12");
                //this.scaleDownVM(3);
                this.scaleDownCloud(this.analysis.getService());
            }
        } else if(this.analysis.getResourceAlert()){

        }  
    }
    
    private EndPoint selectCloud(){
        ClientCloudManager                      ccm         = new ClientCloudManager();
        Map<EndPoint, List<SystemDataPoint>>    clouds      = ccm.requestCloudResources();
        EndPoint                                bestCloud   = null;
        SystemDataPoint                         bestNode    = null;

        if(clouds != null && !clouds.isEmpty()){
            Iterator<EndPoint> it = clouds.keySet().iterator();
            
            while(it.hasNext()){
                EndPoint ep = (EndPoint)it.next();
                if(bestCloud == null){
                    bestCloud = ep;
                    Iterator<SystemDataPoint> itn = clouds.get(ep).iterator();
                    bestNode = itn.next();
                    while(itn.hasNext()){
                        SystemDataPoint node = itn.next();
                        bestNode = node.getCpuUsage().getAverage() < bestNode.getCpuUsage().getAverage() ? node : bestNode;
                    } 
                } else {
                    Iterator<SystemDataPoint> itn = clouds.get(ep).iterator();
                    while(itn.hasNext()){
                        SystemDataPoint node = itn.next();
                        if(node.getCpuUsage().getAverage() < bestNode.getCpuUsage().getAverage()){
                            bestNode = node;
                            bestCloud = ep;
                        }
                    }
                }
            }
        }
        return bestCloud;
    }
    
    private boolean scaleUpApp(String service, String host) {
        NodeManagerServiceStub nodeManager = null;
        int attemps = 0;
        do{
            nodeManager = (NodeManagerServiceStub) Broker.getNaming().lookup("NodeManagerService".toLowerCase() + "@" + host);
            attemps++;
        } while(nodeManager == null && attemps < 3);

        if (nodeManager != null){
            nodeManager.setForwarded(false);
            nodeManager.addService(service, FrontEnd.getInstance().getService(service).getStub());
            return true;
        }
        return false;
    }
    
    private boolean scaleDownApp(String service, String host){
        NodeManagerServiceStub nodeManager = null;
        int attemps = 0;
        do{
            nodeManager = (NodeManagerServiceStub) Broker.getNaming().lookup("NodeManagerService".toLowerCase() + "@" + host);
            attemps++;
        } while(nodeManager == null && attemps < 3);

        
        if (nodeManager != null){
            nodeManager.setForwarded(false);
            nodeManager.removeService(service);
            return true;
        }
        return false;
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
            System.out.println("[Adaptor:154] Resuming new VM at IP " + vmIP);
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

    private boolean scaleDownVM(int vmID){
        ResourceController cms = new ResourceController();
        System.out.println("\n--------------------------------------------");
        System.out.println("[Adaptor:177] Undeploying VM " + vmID);
        System.out.println("---------------------------------------------\n");
        return cms.scaleDownVM(vmID);
    }

    private boolean scaleUpCloud(String serviceName){
        String[] endpoint = null;
        if(Broker.getSystemProps().getProperties().containsKey("cloud_list")){
            endpoint = ((String) Broker.getSystemProps().getProperties().get("cloud_list")).split(":");
            
            String  cloudHost   = endpoint[0];
            int     cloudPort   = Integer.parseInt(endpoint[1]);
            
            NamingStub  ns = new NamingStub(cloudHost, 1111);
            
            CloudManagerServiceStub cloudManager = (CloudManagerServiceStub)ns.lookup("management");
            cloudManager.setForwarded(false);
            Stub stub = Broker.getNaming().lookup(serviceName);
            cloudManager.addServiceOnCloud(serviceName, stub);
            stub.setHost(cloudHost);
            stub.setPort(cloudPort);
            stub.setFeHost(cloudHost);
            stub.setFePort(cloudPort);
            stub.setForwarded(true);
            
            Stub st = null;
            int attemps = 0;
            do{
                st = ns.lookup(serviceName);
                attemps++;
            } while(st == null && attemps < 3);
            
            if(st != null){
                Broker.getNaming().bind(serviceName, stub);
                return true;
            } else{ 
                return false;
            } 
        } else{
            return false;
        }
    }

    private boolean scaleDownCloud(String serviceName){
        String      endpoint    = (String) Broker.getSystemProps().getProperties().get("cloud_list");
        String      cloudHost   = endpoint.substring(0, endpoint.indexOf(':'));
        NamingStub  ns          = new NamingStub(cloudHost, 1111);

        Broker.getNaming().unbind(serviceName, new EndPoint(endpoint));
        
        CloudManagerServiceStub cloudManager = (CloudManagerServiceStub)ns.lookup("management");
        cloudManager.removeService(serviceName);
        return true;
    }
}