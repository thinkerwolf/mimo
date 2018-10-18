package com.thinkerwolf.mimo.channel;

import java.util.concurrent.Future;

public interface ChannelFuture extends Future<Void>{
	
	Channel channel();
	
	
}
