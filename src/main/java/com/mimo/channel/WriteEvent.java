package com.mimo.channel;

public interface WriteEvent extends ChannelEvent {
	
	Object writeObject();

	boolean isFlush();


}
