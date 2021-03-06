package com.askcs.dialog.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class ClientConThread implements java.lang.Runnable {
//	private static final Logger log = Logger
//			.getLogger("DialogHandler");
	@Override
	public void run() {
		
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_THREADPOOL_SIZE,10);
		ParallelInit.client = Client.create(cc);
		ParallelInit.client.setConnectTimeout(1000);
		ParallelInit.client.setReadTimeout(15000);
		ParallelInit.clientActive = true;
	}
}
