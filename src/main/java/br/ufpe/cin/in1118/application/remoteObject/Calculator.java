package br.ufpe.cin.in1118.application.remoteObject;

import br.ufpe.cin.in1118.distribution.stub.ICalculator;

public class Calculator implements ICalculator{
	
	public float add(float a, float b){
		return a + b;
	}
	
	public float sub(float a, float b){
		return a - b;
	}
	
	public float mul(float a, float b){
		return a * b;
	}
	
	public float div(float a, float b){
		if (b != 0)
			return a / b;
		else
			return Float.NaN;
	}
}
