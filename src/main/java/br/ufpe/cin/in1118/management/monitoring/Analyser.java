package br.ufpe.cin.in1118.management.monitoring;

import br.ufpe.cin.in1118.application.server.Broker;

public class Analyser {

	private float lowerThreshold	= 0;
	private float higherThreshold	= 0;
	private boolean paused			= false;
	
	public Analyser(){
		this.lowerThreshold =
				Broker.getSystemProps().getProperties().containsKey("lower_threshold")
				?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("lower_threshold"))
				:0;
		this.higherThreshold =
				Broker.getSystemProps().getProperties().containsKey("higher_threshold")
				?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("higher_threshold"))
				:0;
	}
	
	public float getLowerThreshold(){
		return this.lowerThreshold;
	}
	
	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

/*	public boolean analyse(double metric){
		//call the domain manager if necessary
		if (this.higherThreshold == 0 || this.paused)
			return true;
//		System.out.println("[Analyser:33] Analysis (" + this.paused +")" + this.threshold + " > " + metric + "?");
		return this.higherThreshold > metric;
	}*/
	
/*	public boolean analyse(double metric){
		if (this.higherThreshold == 0 || this.paused)
			return true;
		return this.higherThreshold > metric && this.lowerThreshold > metric;
	}
*/	
	public short analyse(double metric){
		short alert = 0;
		if (this.higherThreshold == 0 || this.paused)
			return alert;
		else if (metric < this.lowerThreshold)
			alert = -1;
		else if (metric > this.higherThreshold)
			alert = 1;
		return alert;
	}	
}
	