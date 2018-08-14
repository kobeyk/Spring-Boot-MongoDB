package com.appleyk.runnable;

import com.appleyk.entity.Tile;
import com.appleyk.repository.TileReponsitory;
import com.appleyk.store.TaskQueueManager;
import com.appleyk.utils.IdWorker;
import com.appleyk.utils.TilesSpiderUtils;

/**
 * 消费者线程 -- 处理url
 * 
 * @author yukun24@126.com
 * @blob http://blog.csdn.net/appleyk
 * @date 2018年4月3日-上午11:35:11
 */
public class Consumer implements Runnable {

	private String name;
	private TaskQueueManager taskQueueManager;
	private IdWorker idWorker;
	private TileReponsitory tileReponsitory;

	public Consumer(String name, TaskQueueManager taskQueueManager, IdWorker idWorker,
			TileReponsitory tileReponsitory) {
		this.name = name;
		this.taskQueueManager = taskQueueManager;
		this.idWorker = idWorker;
		this.tileReponsitory = tileReponsitory;
	}

	public void run() {
		try {
			while (true) {
				
				
				String url = taskQueueManager.consume();
				SaveToMongoDB(url);
				// 消费url
				// System.err.println("消费者[" + name + "]：消费url --" +
				// taskQueueManager.consume()+"--
				// 剩余url容量："+taskQueueManager.size());
				// 休眠1000ms -- 假设生产的快，消费的慢
				// Thread.sleep(100);
			}
		} catch (InterruptedException ex) {
			System.err.println("Consumer Interrupted ：" + ex.getMessage());
		}
	}

	/**
	 * 下载保存到文件
	 * 
	 * @param url
	 * @param fileName
	 * @param level
	 */
	public void SaveToFile(String url, String fileName, String level) {
		try {
			TilesSpiderUtils.downLoadFromUrl(url, fileName, "E://tiles//hn//zz/" + level);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void SaveToMongoDB(String url) {
		try {

			Long id = idWorker.nextId();
			Integer row = TilesSpiderUtils.getRow(url);
			Integer col = TilesSpiderUtils.getCol(url);
			Integer level = TilesSpiderUtils.getLevel(url);
			Long version = TilesSpiderUtils.getVersion(url);
			byte[] data = TilesSpiderUtils.downLoadFromUrl(url);
			Tile tile = new Tile(id, row, col, level, version, "google");
			tile.setData(data);
			tileReponsitory.insert(tile);
			System.err.println(TilesSpiderUtils.getFileName(url) + "-- id: " + id + ",存储DB成功！");

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
