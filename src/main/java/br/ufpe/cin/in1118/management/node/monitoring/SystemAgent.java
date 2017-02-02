package br.ufpe.cin.in1118.management.node.monitoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import br.ufpe.cin.in1118.management.node.SigarLoader;

public class SystemAgent implements Runnable{
 
	private Sigar 					sigar		= null;
	private	static List<SystemData>	dataList	= null;
	private long 					interval	= 1000;

	private Map<String, Long> 		rxCurrentMap	= new ConcurrentHashMap<String, Long>();
	private Map<String, List<Long>>	rxChangeMap		= new ConcurrentHashMap<String, List<Long>>();
	private Map<String, Long>		txCurrentMap	= new ConcurrentHashMap<String, Long>();
	private Map<String, List<Long>>	txChangeMap		= new ConcurrentHashMap<String, List<Long>>();
	
	public SystemAgent()	{
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		dataList	= 	Collections.synchronizedList(new ArrayList<SystemData>());
		sigar  		= 	SigarLoader.getInstance().getSigar();
	}
	
	public SystemAgent(long interval)	{
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		dataList		= 	Collections.synchronizedList(new ArrayList<SystemData>());
		this.sigar  	= 	SigarLoader.getInstance().getSigar();
		this.interval	=	interval;
	}
	
	public SystemAgent(Sigar sigar){
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		dataList	= Collections.synchronizedList(new ArrayList<SystemData>());
		this.sigar	= sigar;
	}
	
	public static List<SystemData> getSystemDataList() {
		return dataList;
	}
	
	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	private void update() {
		try {
			Long[] m = this.getNetworkMetrics();
			dataList.add(new SystemData((sigar.getCpuPerc().getCombined()),
					sigar.getMem().getUsedPercent(),
					m[0],
					m[1]));
		} catch (SigarException e) {
			e.printStackTrace();
		}
	}
	
	public static SystemData getLastMeasurement(){
		if (dataList.isEmpty())
			return null;
		else
			return dataList.get(dataList.size() - 1);
	}
	
	private Long[] getNetworkMetrics() throws SigarException{

        for (String ni : sigar.getNetInterfaceList()) {
            NetInterfaceStat netStat = sigar.getNetInterfaceStat(ni);
            NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ni);
            String hwaddr = null;
            if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
                hwaddr = ifConfig.getHwaddr();
            }
            if (hwaddr != null) {
                long rxCurrenttmp = netStat.getRxBytes();
                saveChange(rxCurrentMap, rxChangeMap, hwaddr, rxCurrenttmp, ni);
                long txCurrenttmp = netStat.getTxBytes();
                saveChange(txCurrentMap, txChangeMap, hwaddr, txCurrenttmp, ni);
            }
        }
        long totalrx = getMetricData(rxChangeMap);
        long totaltx = getMetricData(txChangeMap);
        for (List<Long> l : rxChangeMap.values())
            l.clear();
        for (List<Long> l : txChangeMap.values())
            l.clear();
        return new Long[] { totalrx, totaltx };
	}
	
    private long getMetricData(Map<String, List<Long>> rxChangeMap) {
        long total = 0;
        for (Entry<String, List<Long>> entry : rxChangeMap.entrySet()) {
            int average = 0;
            for (Long l : entry.getValue()) {
                average += l;
            }
            total += average / entry.getValue().size();
        }
        return total;
    }

    private void saveChange(Map<String, Long> currentMap,
            Map<String, List<Long>> changeMap, String hwaddr, long current,
            String ni) {
        Long oldCurrent = currentMap.get(ni);
        if (oldCurrent != null) {
            List<Long> list = changeMap.get(hwaddr);
            if (list == null) {
                list = new LinkedList<Long>();
                changeMap.put(hwaddr, list);
            }
            list.add((current - oldCurrent));
        }
        currentMap.put(ni, current);
    }

	@Override
	public void run() {
		while(true){
				Thread.currentThread();
				try {
					Thread.sleep(this.interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.update();
		}	
	}
}