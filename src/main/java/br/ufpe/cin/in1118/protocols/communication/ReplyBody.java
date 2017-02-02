package br.ufpe.cin.in1118.protocols.communication;

import java.io.Serializable;

public class ReplyBody implements Serializable{

	private static final long serialVersionUID = 4912788784310337640L;
	private Object 				operationResult;
	
	public ReplyBody(Object operationResult){
		this.setOperationResult(operationResult);
	}

	public Object getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(Object operationResult) {
		this.operationResult = operationResult;
	}
}
