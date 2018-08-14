package com.appleyk.store;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 单例模式 -- 任务队列管理器  -- 实例在多线程下启用同步块synchronized保护实例有效的被创建
 * @author yukun24@126.com
 * @blob   http://blog.csdn.net/appleyk
 * @date   2018年4月3日-上午10:49:24
 */
public class TaskQueueManager {

	/**
	 * BlockingQueue -- 阻塞队列 -- 存放url字符串
	 * 队列的特点先进先出 -- 对应先进来的url，先进行下载处理 
	 * 不指定容器大小，默认Integer.MAX_VALUE
	 */
	public static BlockingQueue<String> queue;
	private TaskQueueManager() {
		
		/**
		 * 由于LinkedBlockingQueue实现是线程安全的，实现了先进先出等特性，是作为生产者消费者的首选
		 * LinkedBlockingQueue 可以指定容量，也可以不指定，不指定的话，默认最大是Integer.MAX_VALUE
		 * 其中主要用到put和take方法
		 * put方法在队列满的时候会阻塞直到有队列成员被消费
		 * take方法在队列空的时候会阻塞，直到有队列成员被放进来
		 */
		queue = new LinkedBlockingQueue<String>();
	}

	/**
	 * Java 语言提供了 volatile 和 synchronized 两个关键字来保证线程之间操作的有序性
	 */
	private static volatile TaskQueueManager instance;

	public static TaskQueueManager getIstance() {
		if (instance == null) {
			synchronized (TaskQueueManager.class) {
				if (instance == null) {
					instance = new TaskQueueManager();
				}
			}
		}
		return instance;
	}

	/**
	 * put  -- 模拟任务生产  -- （场景应用在单线程下，put进有效的url地址）
	 * @param url
	 * @throws InterruptedException
	 */
	public static void produce(String url) throws InterruptedException{
		queue.put(url);
	}

	/**
	 * take  -- 模拟任务消费 -- （场景应用在多线程下，take取出有效的url并进行后期处理）
	 * @return
	 * @throws InterruptedException
	 */
	public static String consume() throws InterruptedException{
		return queue.take();
	}
	
	/**
	 * 获取队列的url数量  -- (如果这个值在60s内，等于0的话，将会终止所有任务 -- 生成线程 和 消费线程)
	 * @return
	 */
	public Integer size(){
		return queue.size();
	}
	
}
