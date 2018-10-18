package com.thinkerwolf.mimo.channel;
/**
 * 服务端SocketChannel
 * @author wukai
 *
 */

import java.net.SocketAddress;

public interface ServerSocketChannel extends Channel {
	
	SocketAddress getLocalAddress();
	
	SocketAddress getRemoteAddress();
	
	
	
}
