package br.ufpe.cin.in1118.application.server;

import br.ufpe.cin.in1118.distribution.frontend.FrontEnd;

public class CloudFrontEndServer {
	public static void main(String[] args) {
		FrontEnd frontend = FrontEnd.getInstance("/root/config/frontend.config");
		frontend.start();
	}
}
