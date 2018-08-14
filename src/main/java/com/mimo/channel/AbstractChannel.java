package com.mimo.channel;

import com.mimo.concurrent.ChannelFuture;

public abstract class AbstractChannel implements Channel {

	protected ChannelFuture finishConnectFuture;

	protected boolean server;

	private final DefaultChannelProcessorChain chain;


	protected AbstractChannel(ChannelProcessorChain chain) {
		this.chain = (DefaultChannelProcessorChain) chain;
	}

	public AbstractChannel() {
		this.chain = newProcessorChain();
	}

	public ChannelFuture getFinishConnectFuture() {
		return finishConnectFuture;
	}

	@Override
	public ChannelProcessorChain chain() {
		return chain;
	}

	protected DefaultChannelProcessorChain newProcessorChain() {
		return new DefaultChannelProcessorChain(this);
	}


}
