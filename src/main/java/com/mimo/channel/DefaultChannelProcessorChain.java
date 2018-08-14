package com.mimo.channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mimo.processor.ChannelInboundProcessor;
import com.mimo.processor.ChannelOutboundProcessor;
import com.mimo.processor.ChannelProcessor;

public class DefaultChannelProcessorChain implements ChannelProcessorChain {
	/** Inbounds */
	private DefaultChannelProcessorContext headContext;
	/** Outbounds */
	private DefaultChannelProcessorContext tailContext;
	/**  */
	private Map<String, ChannelProcessor> name2Processor = new ConcurrentHashMap<String, ChannelProcessor>();

	private Channel channel;

	public DefaultChannelProcessorChain(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void addLast(String name, ChannelProcessor processor) {
		if (name == null || processor == null) {
			throw new IllegalArgumentException();
		}
		if (name2Processor.isEmpty()) {
			init(name, processor);
		} else {
			checkDuplicateName(name, name2Processor);
			DefaultChannelProcessorContext ctx = new DefaultChannelProcessorContext(name, channel, this, processor);
			tailContext.next = ctx;
			ctx.pre = tailContext;
			tailContext = ctx;
		}
	}

	@Override
	public void addFirst(String name, ChannelProcessor processor) {
		if (name == null || processor == null) {
			throw new IllegalArgumentException();
		}
		if (name2Processor.isEmpty()) {
			init(name, processor);
		} else {
			checkDuplicateName(name, name2Processor);
			DefaultChannelProcessorContext ctx = new DefaultChannelProcessorContext(name, channel, this, processor);
			headContext.pre = ctx;
			ctx.next = headContext;
			headContext = ctx;
		}
	}

	@Override
	public void addLast(ChannelProcessor processor) {
		if (processor == null) {
			throw new IllegalArgumentException();
		}
		addLast(generateProcessorName(processor), processor);
	}

	@Override
	public void addFirst(ChannelProcessor processor) {
		if (processor == null) {
			throw new IllegalArgumentException();
		}
		addFirst(generateProcessorName(processor), processor);
	}

	public void init(String name, ChannelProcessor processor) {
		DefaultChannelProcessorContext ctx = new DefaultChannelProcessorContext(name, channel, this, processor);
		headContext = ctx;
		tailContext = ctx;
		name2Processor.put(name, processor);
	}

	private static void checkDuplicateName(String name, Map<String, ChannelProcessor> processors) {
		if (processors.containsKey(name)) {
			throw new IllegalArgumentException("duplicate processorname [" + name + "]");
		}
	}

	private static String generateProcessorName(ChannelProcessor processor) {
		int hash = processor.hashCode();
		return "Processor-context-" + hash;
	}

	@Override
	public void sendInbound(ChannelEvent ce) {
		ChannelProcessorContext ctx = findContext(true);
		if (ctx != null) {
			ChannelInboundProcessor p = (ChannelInboundProcessor) ctx.processor();
			p.handleInbound(ctx, ce);
		}
	}

	@Override
	public void sendOutbound(ChannelEvent ce) {
		ChannelProcessorContext ctx = findContext(false);
		if (ctx != null) {
			ChannelOutboundProcessor p = (ChannelOutboundProcessor) ctx.processor();
			p.handleOutbound(ctx, ce);
		}
	}

	@Override
	public void sendException(boolean inbound, Throwable etx) {
		ChannelProcessorContext ctx = findContext(inbound);
		if (ctx != null) {
			ctx.processor().exceptionCaught(ctx, etx);
		}
	}

	private DefaultChannelProcessorContext findContext(boolean inbound) {
		if (inbound) {
			if (headContext != null) {
				for (DefaultChannelProcessorContext ctx = headContext; ctx != null;) {
					if (ctx.processor() instanceof ChannelInboundProcessor) {
						return ctx;
					}
					ctx = ctx.next;
				}
			}
		} else {
			if (tailContext != null) {
				for (DefaultChannelProcessorContext ctx = tailContext; ctx != null;) {
					if (ctx.processor() instanceof ChannelOutboundProcessor) {
						return ctx;
					}
					ctx = ctx.pre;
				}
			}

		}
		return null;
	}



}
