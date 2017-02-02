package br.ufpe.cin.in1118.management.node.monitoring;

public class SystemData {

	private long	timeStamp	= 0L;
	private double	cpuUsage	= 0.0D;
	private double	memUsage	= 0.0D;
	private long	rxTotal		= 0L;
	private long 	txTotal		= 0L;
		
	public SystemData(){
		this.setTimeStamp();
	}
	
	public SystemData(double cpuUsage, double memUsage, long rx, long tx){
		this.setTimeStamp();
		this.setCpuUsage(cpuUsage);
		this.setMemUsage(memUsage);
		this.setRxTotal(rx);
		this.setTxTotal(tx);
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}

	private void setTimeStamp() {
		this.timeStamp = System.currentTimeMillis();
	}
	public double getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public double getMemUsage() {
		return memUsage;
	}
	public void setMemUsage(double memUsage) {
		this.memUsage = memUsage;
	}

	public long getRxTotal() {
		return rxTotal;
	}

	public void setRxTotal(long rxTotal) {
		this.rxTotal = rxTotal;
	}

	public long getTxTotal() {
		return txTotal;
	}

	public void setTxTotal(long txTotal) {
		this.txTotal = txTotal;
	}
}
