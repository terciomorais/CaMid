package br.ufpe.cin.middleware.distribution.qos.observers;

import java.util.Observable;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import br.ufpe.cin.middleware.services.manager.monitor.local.LocalAgent;

public class SystemObserver extends TickableObserver {

	private Sigar sigar;
	
	private int counter = 0;
	
	private LocalAgent la = LocalAgent.getInstance();
	
	public SystemObserver()
	{
		sigar  = SigarLoader.getInstance().getSigar();
	}
	
	public void update(Observable arg0, Object arg1) {
		try {
			if(counter++ % 30 == 0)
			{
				Double cpuUsage = 1-sigar.getCpuPerc().getIdle();
				Double memUsage = sigar.getMem().getUsedPercent();
				
	//			SystemInfo sysinfo = new SystemInfo();
	//			sysinfo.setCpuUsage(cpuUsage);
	//			sysinfo.setMemUsage(memUsage);
	//			sysinfo.setNumberOfCores(numberOfCPUs);
				
				la.getCPUUsage().getAndSet(cpuUsage);
				la.getMemUsage().getAndSet(memUsage);
			}
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args) throws SigarException {
		Sigar sigar = new Sigar();
		System.out.println(sigar.getMem().getFreePercent());
//		System.out.println(sigar.getCpu().getTotal());
		System.out.println(1-sigar.getCpuPerc().getIdle());
		
	}
	
}


