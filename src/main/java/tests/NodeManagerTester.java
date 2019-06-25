package tests;

import br.ufpe.cin.in1118.distribution.stub.NamingStub;
import br.ufpe.cin.in1118.distribution.stub.NodeManagerServiceStub;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;

public class NodeManagerTester {
    public static void main(String[] args){
        NamingStub				naming		= new NamingStub("10.66.66.10", 1111);
        NodeManagerServiceStub	nodeManager = (NodeManagerServiceStub) naming.lookup("NodeManagerService".toLowerCase() + "@10.66.66.11");
        nodeManager.setForwarded(false);
        SystemDataPoint         sdp         = nodeManager.getSystemData();
        
        System.out.println("Dados do sistema");
        System.out.println(" >   CPU average: " + sdp.getCpuUsage().getAverage());
    }
}