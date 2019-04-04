package com.thinkerwolf.mimo.test;

import java.util.concurrent.ExecutionException;

import org.junit.Test;

import com.thinkerwolf.mimo.channel.ChannelFuture;
import com.thinkerwolf.mimo.channel.ChannelFutureListener;
import com.thinkerwolf.mimo.channel.DefaultChannelPromise;

public class FutureTest {

	@Test
	public void promiseTest() {
		final DefaultChannelPromise promise = new DefaultChannelPromise(null);
		ChannelFutureListener listener1 = new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println("operationComplete 1 : " + future.channel());
			}
		};
		ChannelFutureListener listener2 = new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println("operationComplete 2 : " + future.channel());
			}
		};
		promise.addListeners(listener1, listener2);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				promise.setSuccess(null);
			}
		});
		t.start();
		
		try {
			promise.sync();
			System.out.println("sync over : " + promise.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FutureTest test = new FutureTest();
		test.promiseTest();
	}

}
