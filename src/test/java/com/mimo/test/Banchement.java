package com.mimo.test;

import com.mimo.util.NetUtils;

public class Banchement {
	public static void main(String[] args) {

		int i = 50;
		for (; i > 0; i--) {
			final int ti = i;
			new Thread("banchement-thread-" + ti) {
				@Override
				public void run() {
					try {
						BootstrapTest.start(NetUtils.getLocalhostAddress().getHostAddress(), 8088, ti + "");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}

	}
}
