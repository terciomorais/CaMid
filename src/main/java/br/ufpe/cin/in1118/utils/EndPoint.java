package br.ufpe.cin.in1118.utils;

import java.io.Serializable;

public class EndPoint implements Serializable{
	private static final long serialVersionUID = 1L;
	private String	host;
	private int		port;
	private String	endpoint;
	private String	cloud;
	
	public EndPoint(String endpoint) {
		String[] split = endpoint.split(":");
		this.host = split[0];
		this.port = Integer.parseInt(split[1]);
		this.endpoint = endpoint;
	}
	
	public String getEndpoint(){
		return this.endpoint;
	}
	public String getHost(){
		return this.host;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public String getCloud() {
		return cloud;
	}

	public void setCloud(String cloud) {
		this.cloud = cloud;
	}

	public boolean equals(Object obj){
		if (this == obj) return true;
		else if(obj != null 
				&& this.getEndpoint().equals(((EndPoint)obj).getEndpoint())) return true;
		else return false;
	}
}
