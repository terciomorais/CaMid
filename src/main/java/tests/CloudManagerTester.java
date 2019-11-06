package tests;

import br.ufpe.cin.in1118.distribution.stub.CloudManagerServiceStub;
import br.ufpe.cin.in1118.distribution.stub.DelayStub;
import br.ufpe.cin.in1118.distribution.stub.NamingStub;

public class CloudManagerTester{
    
    public static void main(String[] args){
        
        NamingStub  ns = new NamingStub("10.66.67.20", 1111);
        DelayStub   ds = (DelayStub)ns.lookup("delay");
        
        ns.setHost("10.66.67.20");
        ns.setPort(1111);
        CloudManagerServiceStub cmss = (CloudManagerServiceStub) ns.lookup("management");

        System.out.println(" Servico está ativo? " + cmss.serviceIsUp("delay")); 
        //cmss.addServiceOnCloud("delay", ns.lookup("delay"));
/* 
        System.out.println(" Endpoint do serviço " + ds.getEndpoint());
        System.out.println(" Endpoint do Front " + ds.getFeHost() + ":" + ds.getFePort());
        ds.setHost("10.66.67.20");
        ds.setPort(1212);
        ds.setFeHost("10.66.67.20");
        ds.setFePort(1212);
        ns.bind("delay", ds);
        CloudManagerServiceStub cmss = (CloudManagerServiceStub)ns.lookup("management");
        cmss.setForwarded(false);
        cmss.removeServiceEndpoint("delay", new EndPoint("10.66.67.11:1313"));
        ns.unbind("delay", new EndPoint("10.66.67.11:1313"));
        Set<EndPoint> endpoints = new HashSet<EndPoint>();
        
        endpoints.add(new EndPoint("10.66.67.20:1212"));
        endpoints.add(new EndPoint("10.66.67.10:1212"));
        ClientCloudManager client = new ClientCloudManager (endpoints);
        Map<EndPoint, List<SystemDataPoint>> resources = client.requestCloudResources();
        if(resources!= null && !resources.isEmpty())
        for(EndPoint ep : resources.keySet()){
            System.out.println("Cloud " + ep.getHost());
            if(resources.get(ep) == null){
                System.out.println("Não há recursos");
            } else if(resources.get(ep).isEmpty()){
                System.out.println("A nuvem não está utilizando suas VMs");
            } else
            for(SystemDataPoint sdp : resources.get(ep))
            System.out.println("    System data " + sdp.getCpuUsage().getAverage());
        }  */
        System.exit(0);
    }
}