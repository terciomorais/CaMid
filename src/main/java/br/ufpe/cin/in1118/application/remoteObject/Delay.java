package br.ufpe.cin.in1118.application.remoteObject;

import br.ufpe.cin.in1118.distribution.stub.IDelay;

public class Delay implements IDelay{
	public void delay(int serviceTime){
		long ini = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		while((end - ini) < serviceTime)
			end = System.currentTimeMillis();
	}
}