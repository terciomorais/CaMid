package br.ufpe.cin.in1118.distribution.stub;

import java.io.Serializable;

public class DelayStub extends Stub implements IDelay {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7820073057374834425L;

	public DelayStub() {
		super();
	}
	
	@Override
	public void delay(int serviceTime) {
		class Local {};
		Class<?> clazz = Local.class;
		Serializable[] paramValues = {serviceTime};
		super.prepare(clazz, paramValues);
		this.reply = this.request();
	}
}
