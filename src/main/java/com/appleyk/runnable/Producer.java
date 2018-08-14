package com.appleyk.runnable;

import com.appleyk.store.TaskQueueManager;

/**
 * 生成者线程 -- 生成url
 * 
 * @author yukun24@126.com
 * @blob http://blog.csdn.net/appleyk
 * @date 2018年4月3日-上午11:35:30
 */
public class Producer implements Runnable {

	private String name;
	private String url;

	private TaskQueueManager taskQueueManager;

	public Producer(String name, String url, TaskQueueManager taskQueueManager) {
		this.name = name;
		this.url = url;
		this.taskQueueManager = taskQueueManager;
	}

	public Producer(String name, TaskQueueManager taskQueueManager) {
		this.name = name;
		this.taskQueueManager = taskQueueManager;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void run() {
		try {
			while (true) {

				// 生产url
				System.err.println("生产者[" + name + "]：生产url --" + url);
				taskQueueManager.produce(url);

				// 休眠300ms -- 观看效果
				Thread.sleep(300);
			}
		} catch (InterruptedException ex) {
			System.err.println("Producer Interrupted：" + ex.getMessage());
		}
	}

}
