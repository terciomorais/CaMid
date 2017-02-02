package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;

public class MessageHeader implements Serializable {

	private static final long serialVersionUID = -6183080774145585483L;
	private String	magic;
	private int 	version;
	private boolean	byteOrder;
	private int 	messageType;
	private long 	messageSize;
	
	public MessageHeader(){};
	
	public MessageHeader(String magic, int version, boolean byteOrder, int messageType, long messageSize) {
		this.setMagic(magic);
		this.setVersion(version);
		this.setByteOrder(byteOrder);
		this.setMessageType(messageType);
		this.setMessageSize(messageSize);
	}

	public String getMagic() {
		return magic;
	}

	public void setMagic(String magic) {
		this.magic = magic;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isByteOrder() {
		return byteOrder;
	}

	public void setByteOrder(boolean byteOrder) {
		this.byteOrder = byteOrder;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public long getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(long messageSize) {
		this.messageSize = messageSize;
	}
}
