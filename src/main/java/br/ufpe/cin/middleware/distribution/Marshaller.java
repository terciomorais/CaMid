package br.ufpe.cin.middleware.distribution;


import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.middleware.distribution.remote.RemoteObject;
import br.ufpe.cin.middleware.services.LocalServiceRegistry;

public class Marshaller {
	String source = "";
	RemoteObject remoteObject = null;
	ObjectDescriptor objDesc = null;

	public Marshaller() {
	}

	public ObjectDescriptor getObjectDescription() {
		return objDesc;
	}

	public void setSource(String primitiveMessage) {
		this.source = primitiveMessage;
	}

	public void unmarshal() {
		String[] split = this.source.split("\\|");
		this.objDesc = new ObjectDescriptor();

//		try {
//			this.objDesc.setClassType(
//					Class.forName(
//						"br.ufpe.cin.middleware.distribution.remote."
//							+ split[0]
//			));
//					
//		} catch (ClassNotFoundException e) {
//			System.out.println("Remote Object not found");
//			e.printStackTrace();
//		}
		this.objDesc.setServiceName(split[0]);

		String[] method = split[1].split(":");

		if (method[0].equals("null"))
			this.objDesc.setMethod(new Parameter(method[0], method[1], false));
		else
			this.objDesc.setMethod(new Parameter(method[0], method[1], true));

		
		String[] term = null;
		if(split.length > 2)
		{
			term = split[2].split("@");
			List<Parameter> parameters = new ArrayList<Parameter>();
			for (int i = 0; i < term.length; i++) {
				split = term[i].split(":");
				parameters.add(new Parameter(split[0], split[1], false));
			}
//			this.objDesc.setParameters(parameters);
		}

		
//		try {
//			this.setRemoteObject((RemoteObject) this.objDesc.getClassType()
//					.newInstance());
//
//		} catch (InstantiationException e) {
//
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//
//			e.printStackTrace();
//		}
	}

//	public RemoteObject getRemoteObject() {
//		return this.remoteObject;
//	}

//	private void setRemoteObject(RemoteObject remoteObject) {
//		this.remoteObject = remoteObject;
//	}

	public String getSource() {
		return source;
	}
}
