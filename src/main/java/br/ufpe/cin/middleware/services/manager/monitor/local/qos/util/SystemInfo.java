package br.ufpe.cin.middleware.services.manager.monitor.local.qos.util;

public class SystemInfo {

	private Double cpuUsage;
	
	private Double memUsage;
	
	private Integer numberOfCores;

	public Double getCpuUsage()
	{
		return cpuUsage;
	}

	public void setCpuUsage(Double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public Double getMemUsage() {
		return memUsage;
	}

	public void setMemUsage(Double memUsage) {
		this.memUsage = memUsage;
	}

	public Integer getNumberOfCores() {
		return numberOfCores;
	}

	public void setNumberOfCores(Integer numberOfCores) {
		this.numberOfCores = numberOfCores;
	}
	
}
