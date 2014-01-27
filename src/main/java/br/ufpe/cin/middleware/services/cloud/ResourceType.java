package br.ufpe.cin.middleware.services.cloud;

import org.w3c.dom.Node;

public interface ResourceType {
	abstract void setInformations();
	abstract void addNode(Node n);
	abstract void addMonitoringData(ManageInformation mi , Node n);
}
