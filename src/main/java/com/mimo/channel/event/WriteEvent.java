package com.mimo.channel.event;

public interface WriteEvent extends ChannelEvent {
	
	Object writeObject();

	boolean isFlush();


}
