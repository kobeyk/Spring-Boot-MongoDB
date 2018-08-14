import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.appleyk.Application;
import com.appleyk.entity.Tile;
import com.appleyk.repository.TileReponsitory;
import com.appleyk.runnable.Consumer;
import com.appleyk.service.impl.TileServiceImpl;
import com.appleyk.store.TaskQueueManager;
import com.appleyk.utils.IdWorker;
import com.appleyk.utils.WebMercatorUtils;

/**
 * 谷歌全球范围内的墨卡托切片 -- 数据爬取
 * 
 * @author yukun24@126.com
 * @blob http://blog.csdn.net/appleyk
 * @date 2018年4月2日-下午5:13:46
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类 
public class TilesDownLoadTest {

	/**
	 * 郑州范围
	 */
	static double minX = 112.729502;
	static double maxX = 114.210982;
	static double minY = 34.914062;
	static double maxY = 34.351984;

	
	@Autowired
	private TileReponsitory tileReponsitory;
	
	@Autowired
	private TileServiceImpl tileService;
	
	@Value("${IdWorker.workerId}")
	private Integer workID;

	@Value("${IdWorker.datacenterId}")
	private Integer datacenterID;
	
	
	
	/**
	 * 只拿一个谷歌切片url做测试
	 */
	@Test
	public void Test01() {

		int level = 18;

		Map<String, List<Integer>> map = WebMercatorUtils.GetTileXYRange(level, minX, maxX, minY, maxY);
		System.err.println(map);

		Integer minTileX = 0;
		Integer maxTileX = 0;
		Integer minTileY = 0;
		Integer maxTileY = 0;

		if (map != null && map.size() == 2) {
			minTileX = map.get("X").get(0);
			maxTileX = map.get("X").get(1);
			minTileY = map.get("Y").get(0);
			maxTileY = map.get("Y").get(1);
		}

		/**
		 * 谷歌切片服务器访问地址前缀，核心在后面进行拼接
		 */
		String url = "http://www.google.cn/maps/vt?lyrs=s@781&gl=cn&";
		long count = GetTiles(minTileX, maxTileX, minTileY, maxTileY);
		System.err.println("切片数量：" + count);

		Map<String, Integer> mapTileXY = GetTileByIndex(count - 1, minTileX, maxTileX, minTileY, maxTileY);

		Integer tileX = mapTileXY.get("X");
		Integer tileY = mapTileXY.get("Y");

		url = buildUrl(url, tileX, tileY, level);
		System.err.println("谷歌切片url地址：" + url);
		System.err.println("======准备开始下载....");
		String fileName = "z_" + level + "_x_" + tileX + "_y_" + tileY;// +".jpg";
		System.err.println("切片名称：" + fileName);
		String savePath = "E://tiles//hn//zz/" + level;
		System.err.println("存储路径：" + savePath);
//		try {
//			// TilesSpiderUtils.downLoadFromUrl(url, fileName, savePath);
//			byte[] data = TilesSpiderUtils.downLoadFromUrl(url);
//			ImagesUtils.saveOne(fileName, data);
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//		}

	}

	/**
	 * 利用多线程 消费拼接的谷歌切片url
	 */
	@Test
	public void Test02() throws Exception, InterruptedException {

		List<Integer> levs = new ArrayList<>();
		levs.add(8);
		levs.add(11);

		String url = "http://www.google.cn/maps/vt?lyrs=s@781&gl=cn";
		IdWorker idWorker = new IdWorker(workID,datacenterID);
		TaskQueueManager manager = TaskQueueManager.getIstance();
		ExecutorService service = Executors.newCachedThreadPool();
		int count = 0;
		for (int z = levs.get(0); z <= levs.get(1); z++) {
			Map<String, List<Integer>> map = WebMercatorUtils.GetTileXYRange(z, minX, maxX, minY, maxY);

			Integer minTileX = 0;
			Integer maxTileX = 0;
			Integer minTileY = 0;
			Integer maxTileY = 0;

			if (map != null && map.size() == 2) {
				minTileX = map.get("X").get(0);
				maxTileX = map.get("X").get(1);
				minTileY = map.get("Y").get(0);
				maxTileY = map.get("Y").get(1);
			}

			/**
			 * 谷歌切片服务器访问地址前缀，核心在后面进行拼接
			 */
			for (int i = minTileX; i <= maxTileX; i++) {
				for (int j = minTileY; j <= maxTileY; j++) {
					manager.produce(buildUrl(url, i, j, z));
					count++;
					if (count < 1000) {
						String name = "下载线程：" + Integer.valueOf(count + 1);
						service.submit(new Consumer(name, manager,idWorker,tileReponsitory));
					}

				}
			}
		}

		while (true) {
             Thread.sleep(1000*30);
             if(manager.size()==0){
            	 service.shutdown();
            	 break;
             }
		}
		System.err.println("下载完成！");
	}

	/**
	 * 获取某一级别下的切片数量有多少
	 */
	@Test
	public void Test03() {

		int level = 11;
		Map<String, List<Integer>> map = WebMercatorUtils.GetTileXYRange(level, minX, maxX, minY, maxY);
		System.err.println(map);
		Integer minTileX = 0;
		Integer maxTileX = 0;
		Integer minTileY = 0;
		Integer maxTileY = 0;
		// x = 213810 y = 104210
		if (map != null && map.size() == 2) {
			minTileX = map.get("X").get(0);
			maxTileX = map.get("X").get(1);
			minTileY = map.get("Y").get(0);
			maxTileY = map.get("Y").get(1);
		}
		long count = GetTiles(minTileX, maxTileX, minTileY, maxTileY);
		System.err.println("级别：" + level + " -- 切片总数量：" + count);
	}

	public long GetTiles(Integer minTileX, Integer maxTileX, Integer minTileY, Integer maxTileY) {
		long count = 0L;
		for (int i = minTileX; i <= maxTileX; i++) {
			for (int j = minTileY; j <= maxTileY; j++) {
				count++;
			}
		}
		return count;
	}

	public Map<String, Integer> GetTileByIndex(long index, Integer minTileX, Integer maxTileX, Integer minTileY,
			Integer maxTileY) {

		Map<String, Integer> result = new HashMap<>();
		long count = 0L;
		for (int i = minTileX; i <= maxTileX; i++) {
			for (int j = minTileY; j <= maxTileY; j++) {
				if (count == index) {
					result.put("X", i);
					result.put("Y", j);
					return result;
				}
				count++;
			}
		}
		return result;
	}

	public String buildUrl(String url, Integer x, Integer y, Integer z) {
		return url + "&x=" + x + "&y=" + y + "&z=" + z;
	}

	@Test
	public void CreateDir() {

		String path = "E://tiles//hn//zz/18";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * 根据Tile 查询 data（二进制数据）
	 */
	@Test
	public void Query(){
		
		List<Tile> tiles = tileService.findData(new Tile(1665, 814, 11));
		Tile tile= tiles.get(0);
		byte[] data = tile.getData();
		
		System.err.println(data.length);
		
	}

	
	/**
	 * 只拿一个谷歌切片url做测试
	 */
	@Test
	public void Test04() {

		int level = 18;

		Map<String, List<Integer>> map = WebMercatorUtils.GetTileXYRange(level, minX, maxX, minY, maxY);
		System.err.println(map);

		Integer minTileX = 0;
		Integer maxTileX = 0;
		Integer minTileY = 0;
		Integer maxTileY = 0;

		if (map != null && map.size() == 2) {
			minTileX = map.get("X").get(0);
			maxTileX = map.get("X").get(1);
			minTileY = map.get("Y").get(0);
			maxTileY = map.get("Y").get(1);
		}

		/**
		 * 谷歌切片服务器访问地址前缀，核心在后面进行拼接
		 */
		String url = "http://www.google.cn/maps/vt?lyrs=s@781&gl=cn&";
		long count = GetTiles(minTileX, maxTileX, minTileY, maxTileY);
		System.err.println("切片数量：" + count);

		Map<String, Integer> mapTileXY = GetTileByIndex(count - 2, minTileX, maxTileX, minTileY, maxTileY);

		Integer tileX = mapTileXY.get("X");
		Integer tileY = mapTileXY.get("Y");

		url = buildUrl(url, tileX, tileY, level);
		System.err.println("谷歌切片url地址：" + url);
	}
}
