package com.thinkerwolf.mimo.channel;

public interface ChannelFactory<C extends Channel> {

    C newChannel();
}
