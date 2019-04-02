package com.thinkerwolf.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {

    //final Selector selector;

    final Selector[] selectors;

    final ServerSocketChannel serverSocket;

    //final ExecutorService executor = Executors.newFixedThreadPool(3);

    public Reactor() throws IOException {

        serverSocket = ServerSocketChannel.open();
//        serverSocket.socket().bind(new InetSocketAddress(port));
//        serverSocket.configureBlocking(false);

        selectors = new Selector[3];
        for (int i = 0; i < selectors.length; i++) {
            selectors[i] = Selector.open();
        }
    }

    public void bind(int port) throws IOException {
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        for (int i = 0; i < selectors.length; i++) {
            SelectionKey sk = serverSocket.register(selectors[i], SelectionKey.OP_ACCEPT);
            sk.attach(new Acceptor());
        }
    }


    @Override
    public void run() {
        for (int i = 0; i < selectors.length; i++) {
            new Thread(new ReactorAcceptor(selectors[i])).start();
        }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) k.attachment();
        if (r != null)
            r.run();
    }

    class ReactorAcceptor implements Runnable {
        Selector selector;

        public ReactorAcceptor(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    selector.select();
                    Set selected = selector.selectedKeys();
                    Iterator it = selected.iterator();
                    while (it.hasNext()) {
                        dispatch((SelectionKey) it.next());
                    }
                    selected.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    class Acceptor implements Runnable {

        Selector[] selectors;
        int next = 0;

        public Acceptor() throws IOException {
            selectors = new Selector[8];
            for (int i = 0; i < selectors.length; i++) {
                selectors[i] = Selector.open();
            }
        }

        @Override
        public void run() {
            try {
                SocketChannel socket = serverSocket.accept();
                socket.configureBlocking(false);
                if (socket != null) {
                    new Handler(selectors[next], socket).run();
                }
                if (++next >= selectors.length) next = 0;
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

}
