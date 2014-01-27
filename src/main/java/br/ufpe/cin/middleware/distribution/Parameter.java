package br.ufpe.cin.middleware.distribution;

public class Parameter {
	private String type 	= "";
	private String value	= "";
	private boolean returnable = true;


	public Parameter(String type, String name, boolean ret)
	{
		this.setType(type);
		this.setValue(name);
		this.setReturnable(ret);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isReturnable() {
		return returnable;
	}
	public void setReturnable(boolean returnable) {
		this.returnable = returnable;
	}
}