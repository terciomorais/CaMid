package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;

public class CalculatorStub extends Stub implements ICalculator{
	public CalculatorStub() {
		super();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public float add(float x, float y){
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = {x, y};
		this.prepare(clazz, paramValues);
		this.request();
		return (float) this.reply.getResponse();
	}

	@Override
	public float sub(float x, float y)  {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = {x, y};
		this.prepare(clazz, paramValues);
		this.request();
		return (float) this.reply.getResponse();
	}

	@Override
	public float mul(float x, float y){
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = {x, y};
		this.prepare(clazz, paramValues);
		this.request();
		return (float) this.reply.getResponse();
	}

	@Override
	public float div(float x, float y) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = {x, y};
		this.prepare(clazz, paramValues);
		this.request();
		return (float) this.reply.getResponse();
	}
}
