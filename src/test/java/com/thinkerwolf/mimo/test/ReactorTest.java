package com.thinkerwolf.mimo.test;

import com.thinkerwolf.reactor.Reactor;

import java.io.IOException;

public class ReactorTest {
    public static void main(String[] args) {
        try {
            Reactor reactor = new Reactor();
            reactor.bind(8088);
            reactor.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
