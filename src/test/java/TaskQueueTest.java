import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.appleyk.runnable.Consumer;
import com.appleyk.store.TaskQueueManager;


/**
 * 多线程模拟实现生产者／消费者模型
 * @author yukun24@126.com
 * @blob   http://blog.csdn.net/appleyk
 * @date   2018年4月3日-上午11:33:01
 */
public class TaskQueueTest {

	@Test
	public void TaskRun() throws Exception,InterruptedException{
		
		/**
		 * 来一个任务队列管理器
		 */
		TaskQueueManager mamager = TaskQueueManager.getIstance();
		
		
		/**
		 * 来一个Java的线程池
		 * CachedThreadPool会创建一个缓存区，将初始化的线程缓存起来 如果线程有可用的，就使用之前创建好的线程
		 * 如果没有可用的，就新创建线程 终止并且从缓存中移除已有60秒未被使用的线程
		 */
		ExecutorService service = Executors.newCachedThreadPool();
		
		/**
		 * 来一个生成者   --模拟url构建并放入队列管理器中,初始放入100个
		 */
		
		for(int i =0;i<200;i++){
			mamager.produce("http://www.baidu.com"+Integer.valueOf(i));
		}
		
		/**
		 * 来10打消费者  -- 模拟从队列管理器中取出url并进行资源下载
		 */
		for(int i = 0 ; i < 10 ; i++){
			String name = "消费者："+Integer.valueOf(i);
			/**
			 * 方法 submit(Runnable) 接收一个 Runnable 的实现作为参数 但是会返回一个 Future 对象 这個
			 * Future 对象可以用于判断 Runnable 是否结束执行
			 */
			service.submit(new Consumer(name, mamager,null,null));
		}
		
		while(mamager.size()!=0){
			
		}
		
		service.shutdown();
		System.err.println("下载任务完成！");

	}
}
