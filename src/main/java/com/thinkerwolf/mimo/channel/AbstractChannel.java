package com.thinkerwolf.mimo.channel;

public abstract class AbstractChannel implements Channel {

	protected ChannelPromise finishConnectPromise;

	protected boolean server;

	private final DefaultChannelProcessorChain chain;

	private UnderLayer underLayer;

	protected AbstractChannel(ChannelProcessorChain chain) {
		this.chain = chain == null ? newProcessorChain() : (DefaultChannelProcessorChain) chain;
		this.underLayer = newUnderLayer();
	}

	public AbstractChannel() {
		this(null);
	}

	public ChannelPromise getFinishConnectFuture() {
		return finishConnectPromise;
	}

	@Override
	public ChannelProcessorChain chain() {
		return chain;
	}

	protected DefaultChannelProcessorChain newProcessorChain() {
		return new DefaultChannelProcessorChain(this);
	}

	@Override
	public UnderLayer underLayer() {
		return underLayer;
	}

	protected abstract UnderLayer newUnderLayer();

	protected abstract class AbstractUnderLayer implements UnderLayer {

	}

}
