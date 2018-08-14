package com.mimo.channel;

import com.mimo.channel.event.ChannelEvent;
import com.mimo.processor.ChannelProcessor;

/**
 * Channel处理链路
 * 
 * @author wukai
 *
 */
public interface ChannelProcessorChain {

	void addLast(String name, ChannelProcessor processor);

	void addFirst(String name, ChannelProcessor processor);

	void addLast(ChannelProcessor processor);

	void addFirst(ChannelProcessor processor);

	/**
	 * 发送输入流事件
	 * 
	 * @param event
	 */
	void sendInbound(ChannelEvent event);

	/**
	 * 发送输出流事件
	 * 
	 * @param event
	 */
	void sendOutbound(ChannelEvent event);

	/**
	 * 发送异常
	 * 
	 * @param inbound
	 * @param etx
	 */
	void sendException(boolean inbound, Throwable etx);

	/**
	 * 发送连接完成
	 * 
	 * @param event
	 */
	void sendConnected(ChannelEvent event);

	/**
	 * 发送接受完成
	 * 
	 * @param event
	 */
	void sendAccepted(ChannelEvent event);

}
