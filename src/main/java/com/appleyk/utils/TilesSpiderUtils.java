package com.appleyk.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TilesSpiderUtils {

	/**
	 * 根据url获取输入流，并写入文件保存
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoadFromUrl(String urlStr, String fileName, String savePath) {
		try {

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置超时间为3秒
			conn.setConnectTimeout(3 * 1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 得到输入流
			InputStream inputStream = conn.getInputStream();
			// 获取自己数组
			byte[] getData = readInputStream(inputStream);
			// 文件保存位置
			File saveDir = new File(savePath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}
			File file = new File(saveDir + File.separator + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(getData);
			if (fos != null) {
				fos.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			System.out.println("info:" + url + "--download success");
		} catch (Exception ex) {

		}

	}

	/**
	 * 根据url获得输入流，不保存到文件，保存到数据库
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public static byte[] downLoadFromUrl(String urlStr) throws Exception, IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] data = readInputStream(inputStream);
		return data;
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * 根据url -- 推出filename
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
		int indexX = url.indexOf("&x=");
		int indexY = url.indexOf("&y=");
		int indexZ = url.indexOf("&z=");

		String xStr = url.substring(indexX + 3, indexY);
		String yStr = url.substring(indexY + 3, indexZ);
		String zStr = url.substring(indexZ + 3, url.length());
		return "l_" + zStr + "_x_" + xStr + "_y_" + yStr + ".jpg";
	}

	
	/**
	 * 根据Url获取切片x
	 * 
	 * @param url
	 * @return
	 */
	public static Integer getRow(String url) {
		int indexX = url.indexOf("&x=");
		int indexY = url.indexOf("&y=");
		String xStr = url.substring(indexX + 3, indexY);
		return Integer.valueOf(xStr);
	}
	
	
	/**
	 * 根据Url获取切片y
	 * 
	 * @param url
	 * @return
	 */
	public static Integer getCol(String url) {
		int indexY = url.indexOf("&y=");
		int indexZ = url.indexOf("&z=");
		String yStr = url.substring(indexY + 3, indexZ);
		return Integer.valueOf(yStr);
	}
	
	
	/**
	 * 根据Url获取切片级别
	 * 
	 * @param url
	 * @return
	 */
	public static Integer getLevel(String url) {
		int indexZ = url.indexOf("&z=");
		String zStr = url.substring(indexZ + 3, url.length());
		return Integer.valueOf(zStr);
	}
	
	/**
	 * 根据Url获取切片版本
	 * 
	 * @param url
	 * @return
	 */
	public static Long getVersion(String url) {
		int indexS  = url.indexOf("s@");
		int indexGL = url.indexOf("&gl");
		String vStr = url.substring(indexS + 2, indexGL);
		return Long.parseLong(vStr);
	}


	public static void main(String[] args) {
		System.err.println("filename = "+getFileName("http://www.google.cn/maps/vt?lyrs=s@781&gl=cn&x=207&y=96&z=8"));
		System.err.println("version ="+getVersion("http://www.google.cn/maps/vt?lyrs=s@781&gl=cn&x=207&y=96&z=8"));
		System.err.println("level = "+getLevel("http://www.google.cn/maps/vt?lyrs=s@781&gl=cn&x=207&y=96&z=8"));
		System.err.println("row ="+getRow("http://www.google.cn/maps/vt?lyrs=s@781&gl=cn&x=207&y=96&z=8"));
		System.err.println("col = "+getCol("http://www.google.cn/maps/vt?lyrs=s@781&gl=cn&x=207&y=96&z=8"));
		
	}
}
