package br.ufpe.cin.in1118.management.monitoring.metrics;

import java.util.Map;

public class Metering {
    private VM vm;
    private Map<String, float[]> services;

	public VM getVm() {
		return this.vm;
	}

	public void setVm(VM vm) {
		this.vm = vm;
	}

	public Map<String, float[]> getServices() {
		return this.services;
	}

	public void setServices(Map<String, float[]> services) {
		this.services = services;
    }
    
    //service is the service name, pos assumes value 0 for max response time and 1 for max_throughput
    public void setServiceMetering(String service, int pos, float value){
        float[] values = this.services.get(service);
        values[pos] = value;
        this.services.put(service, values);
    }

    public float getServiceMetric(String service, int pos){
        if(this.services.containsKey(service)){
            float[] values = this.services.get(service);
            return values[pos];
        } else 
            return -1;
    }

}