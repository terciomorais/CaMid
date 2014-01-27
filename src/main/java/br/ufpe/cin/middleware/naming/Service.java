package br.ufpe.cin.middleware.naming;

import java.io.Serializable;

public class Service implements Serializable, Comparable<Service>{

	private static final long serialVersionUID = 1L;
	
	private String name = "";
	private String host = "";
	private Integer port;
	
	public Service(){}
	
	public Service(String service, int port) {
		this.setName(service);
		this.setPort(port);
	}
	
	public Service(String name, String host, Integer port)
	{
		this.setName(name);
		this.setHost(host);
		this.setPort(port);
	}
	
	public String getHost() {
		return this.host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return this.port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

/*	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		return result;
	}*/

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Service other = (Service) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		return true;
	}
	
	public int compareTo(Service arg0) 
	{
		int compare = this.name.compareTo(arg0.name);
		if(compare != 0)
			return compare;
		compare = this.host.compareTo(arg0.host);
		if(compare != 0)
			return compare;
		compare = this.getPort() - arg0.getPort();
		
		return compare;
	}
	
	public String toString()
	{
		return String.format("cloud://%s:%d/%s", host, port, name);
	}	
}
