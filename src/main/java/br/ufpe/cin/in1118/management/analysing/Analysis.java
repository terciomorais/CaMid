package br.ufpe.cin.in1118.management.analysing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufpe.cin.in1118.application.server.Broker;
import br.ufpe.cin.in1118.management.monitoring.InvokingDataPoint;
import br.ufpe.cin.in1118.management.monitoring.SystemDataPoint;
import br.ufpe.cin.in1118.utils.EndPoint;
import br.ufpe.cin.in1118.utils.Network;

public class Analysis implements Serializable{
    
    private static final long serialVersionUID = -6243005645199171953L;

    private String                                  alertMessage            = "regular";
    private boolean                                 resourceAlert           = false;
    private boolean                                 serviceAlert            = false;
    private boolean                                 objectMonitorEnabled    = false;
    private boolean                                 systemMonitorEnabled    = false;
    private SystemDataPoint                         sysDataPoint            = null;
    private List<String>                            services                = new ArrayList<String>(Broker.getRegistry().getAllServiceNames());
    private Map<String, List<InvokingDataPoint>>    servicesMetering        = null;
    private String                                  sourceNode              = Network.recoverAddress("localhost");
    private Set<EndPoint>                           availableNodes          = null;
    private List<SystemDataPoint>                   nodesSystemData         = null;
    private List<SystemDataPoint>                   replicaSystemData       = null;
    
    public Analysis(){}

    public Analysis(String alertMessage){
        this.alertMessage = alertMessage;
    }

    public String getAlertMessage() {
        return this.alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public boolean isResourceAlert() {
        return this.resourceAlert;
    }

    public void setResourceAlert(boolean resourceAlert) {
        this.resourceAlert = resourceAlert;
    }

    public boolean getResourceAlert() {
        return this.resourceAlert;
    }

    public boolean getServiceAlert() {
        return this.serviceAlert;
    }

    public boolean isObjectMonitorEnabled() {
        return this.objectMonitorEnabled;
    }

    public void setObjectMonitorEnabled(boolean objectMonitorEnabled) {
        this.objectMonitorEnabled = objectMonitorEnabled;
    }

    public boolean isSystemMonitorEnabled() {
        return this.systemMonitorEnabled;
    }

    public void setSystemMonitorEnabled(boolean systemMonitorEnabled) {
        this.systemMonitorEnabled = systemMonitorEnabled;
    }

    public boolean isServiceAlert() {
        return this.serviceAlert;
    }

    public void setServiceAlert(boolean serviceAlert) {
        this.serviceAlert = serviceAlert;
    }

    public SystemDataPoint getSystemDataPoint() {
        return this.sysDataPoint;
    }

    public void setSystemDataPoint(SystemDataPoint sysDataPoint) {
        this.sysDataPoint = sysDataPoint;
    }

	public void setServicesMetering(Map<String, List<InvokingDataPoint>> timeseries) {
        this.servicesMetering = new HashMap<String, List<InvokingDataPoint>>(timeseries);// timeseries;
    
    }

    public Map<String,List<InvokingDataPoint>> getServicesMetering() {
        return this.servicesMetering;
    }
    public String getSourceNode() {
        return this.sourceNode;
    }

    public List<String> getServices() {
        return this.services;
    } 

    public Set<EndPoint> getAvailableNodes() {
        return this.availableNodes;
    }

    public void setAvailableNodes(Set<EndPoint> availableNodes) {
        this.availableNodes = availableNodes;
    }


    public List<SystemDataPoint> getNodesSystemData() {
        return this.nodesSystemData;
    }

    public void setNodesSystemData(List<SystemDataPoint> nodesSystemData) {
        this.nodesSystemData = nodesSystemData;
    }

    public void addNodeSystemData(SystemDataPoint sdp){
        if(this.nodesSystemData == null)
            this.nodesSystemData = new ArrayList<SystemDataPoint>();
        this.nodesSystemData.add(sdp);
    }

    public List<SystemDataPoint> getReplicaSystemData() {
        return this.replicaSystemData;
    }

    public void setReplicaSystemData(List<SystemDataPoint> replicaSystemData) {
        this.replicaSystemData = replicaSystemData;
    }

    public void addReplicaSystemData(SystemDataPoint sdp){
        if(this.replicaSystemData == null)
            this.replicaSystemData = new ArrayList<SystemDataPoint>();
        this.replicaSystemData.add(sdp);
    }

}