package br.ufpe.cin.in1118.management.monitoring.metrics;

public class VM{
    private int id;
    private String name;
    private int cpuSize;
    private int memory;
    private int minCpu;
    private int maxCpu;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCpuSize() {
		return this.cpuSize;
	}

	public void setCpuSize(int cpuSize) {
		this.cpuSize = cpuSize;
	}

	public int getMemory() {
		return this.memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getMinCpu() {
		return this.minCpu;
	}

	public void setMinCpu(int minCpu) {
		this.minCpu = minCpu;
	}

	public int getMaxCpu() {
		return this.maxCpu;
	}

	public void setMaxCpu(int maxCpu) {
		this.maxCpu = maxCpu;
	}
}