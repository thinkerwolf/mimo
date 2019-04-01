package com.thinkerwolf.mimo.channel.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkerwolf.mimo.channel.Channel;
import com.thinkerwolf.mimo.channel.RunLoop;
import com.thinkerwolf.mimo.channel.RunLoopGroup;
import com.thinkerwolf.mimo.channel.Channel.UnderLayer;
import com.thinkerwolf.mimo.concurrent.ChannelFuture;
import com.thinkerwolf.mimo.concurrent.SingleThreadPreExecutor;
import com.thinkerwolf.mimo.util.ObjectUtil;
import com.thinkerwolf.mimo.util.PrintUtil;

/**
 * 处理Selection和IO事件
 * 
 * @author wukai
 *
 */
public class NioRunLoop extends SingleThreadPreExecutor implements RunLoop {

	private static final Logger logger = LoggerFactory.getLogger(NioRunLoop.class);

	private Selector selector;
	
	private NioRunLoopGroup parent;
	
	public NioRunLoop(ThreadFactory threadFactory, SelectorProvider provider, NioRunLoopGroup parent) {
		super(threadFactory);
		ObjectUtil.isNotNull(provider, "SelectorProvider can't be null");
		try {
			this.selector = provider.openSelector();
			this.parent = parent;
		} catch (IOException e) {
			logger.error("openSelector", e);
		}
	}

	@Override
	public void register(Channel channel) {
		register(channel, null);
	}

	@Override
	public void register(Channel channel, ChannelFuture future) {
		try {
			channel.register(this, future);
		} catch (Throwable e) {
			logger.debug("NioRunLoop register error", e);
		}
	}

	@Override
	public void run() {
		for (;;) {
			try {
				select();
				// selectionKey处理
				// logger.debug("handle selected keys");
				processSelectedKeys();
				// IO任务
				runAllTasks();

			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				
			}
		}
	}

	/**
	 * 处理阻塞
	 * 
	 * @throws IOException
	 */
	private void select() throws IOException {
		long currentTimeNanos = System.nanoTime();
		long selectDeadLineNanos = currentTimeNanos + TimeUnit.SECONDS.toNanos(1);
		long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
		if (hasTask()) {
			selector.selectNow();
		} else {
			selector.select(timeoutMillis);
		}
	}

	private void processSelectedKeys() throws Throwable {
		// if (selectedKeySet == null) {
		processSelectedKeysPlain(selector.selectedKeys());
		// } else {
		// processSelectedKeysPlain(selectedKeySet);
		// }
	}

	private void processSelectedKeysPlain(Set<SelectionKey> keySet) throws Throwable {
		Iterator<SelectionKey> iter = keySet.iterator();
		if (keySet.size() > 0) {
			PrintUtil.printSelectedKeys(keySet);
		}

		for (; iter.hasNext();) {
			final SelectionKey sk = iter.next();
			final Object obj = sk.attachment();
			iter.remove();
			if (obj instanceof AbstractNioChannel) {
				processSelectedKey(sk, (AbstractNioChannel) obj);
			} else {
				// TODO

			}
		}

	}

	private void processSelectedKey(SelectionKey sk, AbstractNioChannel ch) throws Throwable {
		if (!sk.isValid()) {
			return;
		}
		final UnderLayer underLayer = ch.underLayer();
		final int readyOps = sk.readyOps();
		if ((readyOps & SelectionKey.OP_CONNECT) != 0) {
			int ops = sk.interestOps();
			ops &= ~SelectionKey.OP_CONNECT;
			ops |= SelectionKey.OP_READ;
			sk.interestOps(ops);
			underLayer.connect();
		}

		if ((readyOps & SelectionKey.OP_ACCEPT) != 0) {
			AbstractNioChannel channel = (AbstractNioChannel) underLayer.accept();
			// modify by wukai on 2019.4.1
			//if (channel != null) {
				//channel.underLayer().accept();
			//	channel.nioChannel().register(sk.selector(), SelectionKey.OP_READ, channel);
			//}	
		}

		if ((readyOps & SelectionKey.OP_READ) != 0) {
			underLayer.read();
		}

		if ((readyOps & SelectionKey.OP_WRITE) != 0) {
			// ch.write(null);
		}
	}

	public Selector getNioSelector() {
		return this.selector;
	}
	
	
	
	@Override
	public RunLoopGroup parent() {
		return parent;
	}

}
