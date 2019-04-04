package com.thinkerwolf.mimo.channel;

import com.thinkerwolf.mimo.concurrent.Future;

public interface ChannelFuture extends Future<Void> {

	Channel channel();
	
}
