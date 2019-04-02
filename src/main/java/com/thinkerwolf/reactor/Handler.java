package com.thinkerwolf.reactor;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Handler implements Runnable {

    static int READING = 0, SENDING = 1;
    static int PROCESSING = Runtime.getRuntime().availableProcessors() * 4;
    ExecutorService executor = Executors.newFixedThreadPool(1);


    final SocketChannel socket;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    int state = READING;

    public Handler(Selector selector, SocketChannel socket) throws ClosedChannelException {
        this.socket = socket;
        sk = socket.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            System.err.println("handler running " + sk.readyOps());
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    boolean isInputComplete() {
        return true;
    }

    boolean isOutputComplete() {
        return true;
    }

    void process() {
        System.err.println(input);
        output.clear();
        output.put("response".getBytes());
        output.flip();
        System.err.println(output);

    }

    synchronized void read() throws IOException {
        socket.read(input);
        input.flip();
        if (isInputComplete()) {
            state = SENDING;
            executor.execute(new Processor());
        }
    }


    void send() throws IOException {
        // output.flip();
        socket.write(output);
        if (isOutputComplete()) sk.cancel();
    }

    synchronized void processAndHandoff() throws IOException {
        process();
        sk.interestOps(SelectionKey.OP_WRITE);
        send();
    }


    class Processor implements Runnable {

        @Override
        public void run() {
            try {
                processAndHandoff();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
