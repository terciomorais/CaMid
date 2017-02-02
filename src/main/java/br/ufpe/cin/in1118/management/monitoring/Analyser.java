package br.ufpe.cin.in1118.management.monitoring;

import br.ufpe.cin.in1118.application.server.Broker;

public class Analyser {

	private float threshold = 0;
	private boolean paused	= false;
	
	public Analyser(){
		this.threshold =
				Broker.getSystemProps().getProperties().containsKey("threshold")
				?Float.parseFloat((String) Broker.getSystemProps().getProperties().get("threshold"))
				:0;
	}
	
	public float getThreshold(){
		return this.threshold;
	}
	
	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean analyse(double metric){
		//call the domain manager if necessary
		if (this.threshold == 0 || this.paused)
			return true;
		System.out.println("[Analyser:33] Analysis (" + this.paused +")" + this.threshold + " > " + metric + "?");
		return (this.threshold > metric);
	}
}
	