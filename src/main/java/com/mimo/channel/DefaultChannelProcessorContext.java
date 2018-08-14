package com.mimo.channel;

import com.mimo.processor.ChannelInboundProcessor;
import com.mimo.processor.ChannelOutboundProcessor;
import com.mimo.processor.ChannelProcessor;

class DefaultChannelProcessorContext implements ChannelProcessorContext {

	private ChannelProcessor processor;

	private Channel channel;

	private ChannelProcessorChain chain;

	private String name;

	DefaultChannelProcessorContext next;

	DefaultChannelProcessorContext pre;

	// private boolean inbound;

	// private boolean outbound;

	DefaultChannelProcessorContext(String name, Channel channel, ChannelProcessorChain chain,
			ChannelProcessor processor) {
		this.processor = processor;
		this.channel = channel;
		this.chain = chain;
		this.name = name;
	}

	@Override
	public ChannelProcessorChain chain() {
		return chain;
	}

	@Override
	public Channel channel() {
		return channel;
	}

	@Override
	public ChannelProcessor processor() {
		return processor;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void sendInbound(ChannelEvent ce) {
		if (next != null) {
			for (DefaultChannelProcessorContext ctx = next; ctx != null;) {
				if (ctx.processor() instanceof ChannelInboundProcessor) {
					ChannelInboundProcessor cip = (ChannelInboundProcessor) ctx.processor();
					cip.handleInbound(ctx, ce);
					break;
				}
				ctx = ctx.next;
			}
		}
	}

	@Override
	public void sendOutbound(ChannelEvent ce) {
		if (pre != null) {
			for (DefaultChannelProcessorContext ctx = pre; ctx != null;) {
				if (ctx.processor() instanceof ChannelOutboundProcessor) {
					ChannelOutboundProcessor cip = (ChannelOutboundProcessor) ctx.processor();
					cip.handleOutbound(ctx, ce);
					break;
				}
				ctx = ctx.pre;
			}
		}
	}

	@Override
	public void write(Object msg, ChannelEvent ce) {
		Channel channel = ce.channel();
		if (pre == null) {
			channel.writeInner(msg, true);
		} else {
			for (DefaultChannelProcessorContext ctx = pre; ctx != null;) {
				if (ctx.processor() instanceof ChannelOutboundProcessor) {
					ctx.write(msg, ce);
					break;
				}
				ctx = ctx.pre;
				
				if (ctx == null) {
					channel.writeInner(msg, true);
				}
				
			}
			
		}
		
		
		
	}

}
