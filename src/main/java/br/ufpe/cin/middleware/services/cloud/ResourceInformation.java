package br.ufpe.cin.middleware.services.cloud;

import java.util.ArrayList;
import java.util.List;

public class ResourceInformation {
	
		private long timeStamp	= -1;
		private String context	= null;
		private String domain	= null;
		private String name	= null;
		private String uuid	= null;
		
		private List<ManageInformation> information;
		
		public static final String LOCAL_CONTEXT = "LOCAL";
		public static final String GLOBAL_CONTEXT = "GLOBAL";
		
		public static final String DOMAIN_APP = "APP";
		public static final String DOMAIN_VNET = "VNET";
		public static final String DOMAIN_VM = "VM";
		
		public ResourceInformation(String context, String domain, String name, String uuid) {
			this.timeStamp = System.currentTimeMillis();
			this.context = context;
			this.name = name;
			this.information = new ArrayList<ManageInformation>();
		}

		public long getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}

		public String getContext() {
			return context;
		}

		public void setContext(String context) {
			this.context = context;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getuId() {
			return uuid;
		}

		public void setuId(String uId) {
			this.uuid = uId;
		}

		public List<ManageInformation> getInformation() {
			return information;
		}

		public void setInformation(List<ManageInformation> information) {
			this.information = information;
		}
		
		public String toString()
		{
			String mainString = this.context + "." + domain + "." + name + "@" + uuid  ;
			
			StringBuilder builder = new StringBuilder();
			for(ManageInformation info : this.information)
			{
				builder.append(toString(mainString, info));
			}
			
			return mainString;
		}
		
		public String toString(String mainString, ManageInformation info)
		{
			StringBuilder builder = new StringBuilder();
			builder.append(String.format("%s.%s=%s (%d)\n", mainString, info.getName(), info.getValue()));
			for(ManageInformation childInfo : info.getChildNodes())
			{
				builder.append(toString(mainString, childInfo));
			}
			return builder.toString();
		}

}