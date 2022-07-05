package br.ufpe.cin.in1118.management;

import java.io.Serializable;
import java.util.List;

import br.ufpe.cin.in1118.utils.EndPoint;

public class SchedullerRoundRobin implements Serializable{
	private static final long	serialVersionUID	= 7813705391836590780L;
	private List<EndPoint>		endPoints			= null;
	private int 				next				= 0;
	private int					tax					= 0;

	public int getTax() {
		return this.tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}
	
	public SchedullerRoundRobin (List<EndPoint> endPoints){
		this.updateEndPoint(endPoints);
	}

	private int getNext() {
		this.next = (this.next + 1) % this.endPoints.size();
		return this.next;
	}
	
	public EndPoint getNextEndPoint(){
		if ((this.endPoints != null) && !this.endPoints.isEmpty()){
			return this.endPoints.get(this.getNext());
		} else {
			return null;
		}
	}
	
	public synchronized void updateEndPoint(List<EndPoint> endPoints){
		if(this.endPoints != null)
			this.endPoints.clear();
		this.endPoints = endPoints;
	}

}
