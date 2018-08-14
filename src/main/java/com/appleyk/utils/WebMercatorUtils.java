package com.appleyk.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * 地图瓦片(切片) -- 墨卡托坐标切片工具
 * 
 * @author yukun24@126.com
 * @blob http://blog.csdn.net/appleyk
 * @date 2018年4月4日-下午1:04:50
 */
public class WebMercatorUtils {

	/**
	 * Web墨卡托 -- 互联网地图通用的地图投影方式，将椭圆形地图投影成平面上的正方形
	 * Bounds(地图范围)[minx,miny,maxx,maxy]
	 * 全世界墨卡托范围是：-20037580.3427892,-20037508.3427892,20037580.3427892,20037580.
	 * 3427892 从第四象限 --- 第一象限
	 */

	public static double minx = -20037508.3427892;
	public static double maxx = 20037508.3427892;

	/**
	 * 切片转范围
	 * 
	 * @param x
	 * @param y
	 * @param level
	 * @return
	 */
	public static Envelope tileXYToNativeRectangle(int x, int y, int level) {
		int xTiles = getNumberOfXTilesAtLevel(level);
		int yTiles = getNumberOfYTilesAtLevel(level);

		double xTileWidth = (maxx - minx) / xTiles;
		double west = minx + x * xTileWidth;
		double east = minx + (x + 1) * xTileWidth;

		double yTileHeight = (maxx - minx) / yTiles;
		double north = maxx - y * yTileHeight;
		double south = maxx - (y + 1) * yTileHeight;
		Envelope envelope = new Envelope(west, east, north, south);

		return envelope;
	}

	/**
	 * 根据经纬度 + 切片级别 --- 计算 x:? y:?
	 * 
	 * @param level
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public static Map<String, Integer> positionToTileXY(int level, double longitude, double latitude) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		Coordinate coordinate = new Coordinate(longitude, latitude);
		Coordinate c = lonLat2Mercator(coordinate);

		// 计算在该级别下X方向切片数量
		int xTiles = getNumberOfXTilesAtLevel(level);
		// 计算在该级别下Y方向切片数量
		int yTiles = getNumberOfYTilesAtLevel(level);

		double overallWidth = maxx - minx;
		double xTileWidth = overallWidth / xTiles;
		double overallHeight = maxx - minx;
		double yTileHeight = overallHeight / yTiles;

		double distanceFromWest = c.x - minx;
		double distanceFromNorth = maxx - c.y;

		double xTileCoordinate = distanceFromWest / xTileWidth;
		if (xTileCoordinate >= xTiles) {
			xTileCoordinate = xTiles - 1;
		}
		map.put("X", (int) xTileCoordinate);

		double yTileCoordinate = distanceFromNorth / yTileHeight;
		if (yTileCoordinate >= yTiles) {
			yTileCoordinate = yTiles - 1;
		}
		map.put("Y", (int) yTileCoordinate);

		return map;
	}

	/**
	 * level级别下，X(瓦片的横向索引，起始位置为最左边，数值为0)方向的切片数量 -- 2的level次幂
	 * 
	 * @param level
	 * @return
	 */
	private static Integer getNumberOfXTilesAtLevel(int level) {
		return 1 << level;
	}

	/**
	 * level级别下，Y(瓦片的纵向索引，起始位置为最上面，数值为0)方向的切片数量 -- 2的level次幂
	 * 
	 * @param level
	 * @return
	 */
	private static Integer getNumberOfYTilesAtLevel(int level) {
		return 1 << level;
	}

	// ---- 瓦片总数量 就是 4的level次幂

	/**
	 * 经纬度转墨卡托
	 * 
	 * @param lonLat
	 * @return
	 */
	public static Coordinate lonLat2Mercator(Coordinate lonLat) {

		double x = lonLat.x * 20037508.34 / 180;
		double y = Math.log(Math.tan((90 + lonLat.y) * Math.PI / 360)) / (Math.PI / 180);
		y = y * maxx / 180;

		return new Coordinate(x, y);
	}


	/**
	 * 墨卡托转经纬度
	 * 
	 * @param mercator
	 * @return
	 */
	public static Coordinate Mercator2lonLat(Coordinate mercator) {

		double x = mercator.x / 20037508.34 * 180;
		double y = mercator.y / 20037508.34 * 180;
		y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);

		return new Coordinate(x, y);
	}

	/**
	 * 获得某个范围下某个级别下的切片XY的范围 ,比如X:[10,20], Y:[20,30]
	 * 
	 * @param level
	 *            -- 缩放级别
	 * @param minX
	 *            -- 最小X == 西经（west）
	 * @param maxX
	 *            -- 最大X == 东经（east）
	 * @param minY
	 *            -- 最小Y == 北纬（north）
	 * @param maxY
	 *            -- 最大Y == 南维（south）
	 * @return
	 */
	public static Map<String, List<Integer>> GetTileXYRange(int level, double minX, double maxX, double minY,
			double maxY) {

		Map<String, Integer> minXY = positionToTileXY(level, minX, minY);
		Map<String, Integer> maxXY = positionToTileXY(level, maxX, maxY);

		Integer minTileX = minXY.get("X");
		Integer maxTileX = maxXY.get("X");
		Integer minTileY = minXY.get("Y");
		Integer maxTileY = maxXY.get("Y");

		List<Integer> tileXrange = new ArrayList<>();
		tileXrange.add(minTileX);
		tileXrange.add(maxTileX);

		List<Integer> tileYrange = new ArrayList<>();
		tileYrange.add(minTileY);
		tileYrange.add(maxTileY);

		Map<String, List<Integer>> mapResult = new HashMap<>();
		mapResult.put("X", tileXrange);
		mapResult.put("Y", tileYrange);

		return mapResult;
	}

	public static void main(String[] args)  {

		/**
		 * 郑州范围
		 */
		 double minX = 112.729502;
		 double maxX = 114.210982;
		 double minY = 34.914062;
		 double maxY = 34.351984;


		Map<String, Integer> minXY = positionToTileXY(15, minX, minY);
		Map<String, Integer> maxXY = positionToTileXY(15, maxX, maxY);

		Integer minTileX = minXY.get("X");
		Integer maxTileX = maxXY.get("X");
		Integer minTileY = minXY.get("Y");
		Integer maxTileY = maxXY.get("Y");
		System.err.println("x切片范围：[" + minTileX + "," + maxTileX + "]");
		System.err.println("Y切片范围：[" + minTileY + "," + maxTileY + "]");

		String url = "http://www.google.cn/maps/vt?lyrs=s@781&gl=cn&";

		// x=46057&y=32219&z=16
		int n = 0;
		for (int i = minTileX; i <= maxTileX; i++) {
			for (int j = minTileY; j <= maxTileY; j++) {
				System.err.println(url+"x="+i+"&y="+j+"&z=15");
				n++;
			}
		}

		System.err.println("郑州缩放级别15下的切片总量：" + n);
	}

}
