package br.ufpe.cin.in1118.management.monitoring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ufpe.cin.in1118.management.node.monitoring.SystemData;

public class SystemDataPoint {
	private DataPoint	cpuUsage	=	new DataPoint();
	private DataPoint	memUsage	=	new DataPoint();
	private DataPoint	netRx		=	new DataPoint();
	private DataPoint	netTx		=	new DataPoint();
	private List<SystemData> dataList	= new ArrayList<SystemData>();

	public SystemDataPoint(){
	}

	public SystemDataPoint(List<SystemData> systemDataList){
		
		for(Iterator<SystemData> it = systemDataList.iterator(); it.hasNext();){
			SystemData sd = it.next();
			if(Double.isNaN(sd.getCpuUsage()) &&
					Double.isNaN(sd.getMemUsage()) &&
					Double.isNaN(sd.getRxTotal()) &&
					Double.isNaN(sd.getTxTotal()))
				this.dataList.add(sd);
		}
		this.setDataPoint();
	}

	public List<SystemData> getSystemDataList() {
		return this.dataList;
	}

	public synchronized void setDataPoint(){

		double	maxCPU		= 0;
		double 	minCPU		= 0;
		double	maxMem		= 0;
		double 	minMem		= 0;
		double	maxNetRx	= 0;
		double 	minNetRx	= 0;
		double	maxNetTx	= 0;
		double 	minNetTx	= 0;

		if(!dataList.isEmpty()){
				maxCPU = dataList.get(0).getCpuUsage();
				minCPU = dataList.get(0).getCpuUsage();
				this.cpuUsage.setBeginTimeStamp(dataList.get(0).getTimeStamp());
				this.cpuUsage.setEndTimeStamp(dataList.get(dataList.size() - 1).getTimeStamp());
				this.cpuUsage.setCount(dataList.size());

				maxMem = dataList.get(0).getMemUsage();
				minMem = dataList.get(0).getMemUsage();
				this.memUsage.setBeginTimeStamp(dataList.get(0).getTimeStamp());
				this.memUsage.setEndTimeStamp(dataList.get(dataList.size() - 1).getTimeStamp());
				this.memUsage.setCount(dataList.size());

				maxNetRx = dataList.get(0).getRxTotal();
				minNetRx = dataList.get(0).getRxTotal();
				this.netRx.setBeginTimeStamp(dataList.get(0).getTimeStamp());
				this.netRx.setEndTimeStamp(dataList.get(dataList.size() - 1).getTimeStamp());
				this.netRx.setCount(dataList.size());

				maxNetTx = dataList.get(0).getTxTotal();
				minNetTx = dataList.get(0).getTxTotal();
				this.netTx.setBeginTimeStamp(dataList.get(0).getTimeStamp());
				this.netTx.setEndTimeStamp(dataList.get(dataList.size() - 1).getTimeStamp());
				this.netTx.setCount(dataList.size());
			
			for(Iterator<SystemData> it = dataList.iterator(); it.hasNext();){
				SystemData sd = it.next();
					this.cpuUsage.getMetrics().add(sd.getCpuUsage());
					this.memUsage.getMetrics().add(sd.getMemUsage());
					this.netRx.getMetrics().add((double) sd.getRxTotal());
					this.netTx.getMetrics().add((double) sd.getTxTotal());

					maxCPU = sd.getCpuUsage() > maxCPU ? sd.getCpuUsage() : maxCPU;
					minCPU = (sd.getCpuUsage() < minCPU ? sd.getCpuUsage() : minCPU);

					maxMem = sd.getMemUsage() > maxMem ? sd.getMemUsage() : maxMem;
					minMem = (sd.getMemUsage() < minMem ? sd.getMemUsage() : minMem);

					maxNetTx = sd.getTxTotal() > maxNetTx ? sd.getTxTotal() : maxNetTx;
					minNetTx = (sd.getTxTotal() < minNetTx ? sd.getTxTotal() : minNetTx);

					maxNetRx = (sd.getRxTotal() < maxNetTx ? sd.getRxTotal() : maxNetRx);
					minNetRx = sd.getRxTotal() > minNetRx ? sd.getRxTotal() : minNetRx;

					maxCPU = Double.isNaN(maxCPU)? 1.0 : maxCPU;
					minCPU = Double.isNaN(minCPU)? 1.0 : minCPU;

					cpuUsage.setSum(cpuUsage.getSum() + sd.getCpuUsage());
					memUsage.setSum(memUsage.getSum() + sd.getMemUsage());
					netRx.setSum(netRx.getSum() + sd.getRxTotal());
					netTx.setSum(netTx.getSum() + sd.getTxTotal());

					cpuUsage.setSquareSum(cpuUsage.getSquareSum() + (sd.getCpuUsage() * sd.getCpuUsage()));
					memUsage.setSquareSum(memUsage.getSquareSum() + (sd.getMemUsage() * sd.getMemUsage()));
					netRx.setSquareSum(netRx.getSquareSum() + (sd.getRxTotal() * sd.getRxTotal()));
					netTx.setSquareSum(netTx.getSquareSum() + sd.getTxTotal() * sd.getTxTotal());
			}
			System.out.println("[SystemDataPoint:110] ::: " + maxCPU + " " + maxMem + " " + maxNetRx + " " + maxNetTx);
			this.cpuUsage.setHighValue(maxCPU);
			this.memUsage.setHighValue(maxMem);
			this.netRx.setHighValue(maxNetRx);
			this.netTx.setHighValue(maxNetTx);

			System.out.println("[SystemDataPoint:110] ::: " + minCPU + " " + minMem + " " + minNetRx + " " + minNetTx);
			this.cpuUsage.setLowerValue(minCPU);
			this.memUsage.setLowerValue(minMem);
			this.netRx.setLowerValue(minNetRx);
			this.netTx.setLowerValue(minNetTx);

			this.cpuUsage.setStatistics();
			this.memUsage.setStatistics();
			this.netRx.setStatistics();
			this.netTx.setStatistics();
			
			System.out.println("[SystemDataPoint:110] data " + this.cpuUsage.getAverage() + " " + this.memUsage.getAverage() + " " + this.netRx.getAverage() + " " + this.netTx.getAverage());
		}
	}
}
