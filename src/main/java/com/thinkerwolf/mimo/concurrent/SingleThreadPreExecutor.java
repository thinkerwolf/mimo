package com.thinkerwolf.mimo.concurrent;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

public abstract class SingleThreadPreExecutor implements Executor, Runnable {

	private Thread thread;
	protected boolean running;
	private Queue<Runnable> taskQueue;

	public SingleThreadPreExecutor(ThreadFactory threadFactory) {
		this.running = false;
		this.thread = threadFactory.newThread(this);
		this.taskQueue = newTaskQueue();
	}

	public Queue<Runnable> newTaskQueue() {
		return new LinkedBlockingQueue<Runnable>();
	}

	public void execute(Runnable command) {
		addTask(command);
		synchronized (this) {
			if (!running) {
				running = true;
				thread.start();
			}
		}
	}

	protected void runAllTasks() {
		for (Runnable task; (task = pollTask()) != null;) {
			task.run();
		}
	}

	public boolean hasTask() {
		return taskQueue.size() > 0;
	}

	public Runnable pollTask() {
		return taskQueue.poll();
	}

	public Runnable peekTask() {
		return taskQueue.peek();
	}

	public void addTask(Runnable command) {
		taskQueue.offer(command);
	}

}
