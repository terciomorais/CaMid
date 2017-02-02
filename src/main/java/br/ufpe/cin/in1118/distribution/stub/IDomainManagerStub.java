package br.ufpe.cin.in1118.distribution.stub;

import br.ufpe.cin.in1118.services.commons.naming.NameRecord;

public interface IDomainManagerStub {
	public boolean scaleOut(String scaleLevel);
	public void addService(String service, NameRecord record);
}
