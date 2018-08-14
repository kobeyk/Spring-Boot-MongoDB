package com.appleyk.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * 
 * @author yukun24@126.com
 * @blob http://blog.csdn.net/appleyk
 * @date 2018年4月3日-上午9:34:52
 */
public class ImagesUtils {

	static MongoClient mongoClient = new MongoClient("localhost", 27017);
	@SuppressWarnings("deprecation")
	static DB db = mongoClient.getDB("tiles");
	static GridFS gfsPhoto = new GridFS(db, "photo");

	/**
	 * 保存文件 定义文件名称，指定文件路径
	 * 
	 * @throws Exception
	 */
	public static void saveOne(String fileName, String filePath) throws Exception {
		File imageFile = new File(filePath);
		GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
		gfsFile.setFilename(fileName);
		gfsFile.save();
	}

	/**
	 * 保存文件的二进制 
	 * @param fileName 文件名
	 * @param data     二进制
	 */
	public static void saveOne(String fileName, byte[] data) {
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSInputFile gfsFile = gfsPhoto.createFile(data);
		gfsFile.setFilename(fileName);
		gfsFile.save();
		System.err.println("文件名: " + fileName + "---保存成功！");
	}

	/**
	 * 根据文件名称查询相应文件信息
	 */
	public static void findOne(String fileName) {

		GridFSDBFile imageForOutput = gfsPhoto.findOne(fileName);
		System.err.println(imageForOutput);
	}

	/**
	 * 根据文件名称查询相应文件 并返回文件的二进制数据
	 * 由于Java方法不支持缺省参数，因此，随便给个参数，以满足方法的重载
	 */
	public static byte[] findOne(String fileName, boolean bFlag) throws IOException {

		GridFSDBFile imageForOutput = gfsPhoto.findOne(fileName);
		System.err.println(imageForOutput);
		InputStream inputStream = imageForOutput.getInputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		if(inputStream!=null){
			inputStream.close();
		}
		return bos.toByteArray();
	}

	/**
	 * 打印所有图片 根据DBCursor遍历所有 图片
	 */
	public static void printAll() {

		DBCursor cursor = gfsPhoto.getFileList();
		while (cursor.hasNext()) {
			System.err.println(cursor.next());
		}
	}

	/**
	 * 根据文件名删除 对象
	 * 
	 * @param fileName
	 */
	public static void deleteOne(String fileName) {
		gfsPhoto.remove(gfsPhoto.findOne(fileName));
	}

	/**
	 * 遍历删除所有文件
	 * 
	 * @param fileName
	 */
	public static void deleteAll() {
		DBCursor cursor = gfsPhoto.getFileList();
		int count = 0;
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			String fileName = (String) object.get("filename");
			gfsPhoto.remove(gfsPhoto.findOne(fileName));
			count++;
		}

		System.err.println("数据清空：done! -- 共计：" + count + "条");
	}

	/**
	 * 保存二进制 -- 磁盘文件
	 * @param data
	 * @param fileName
	 * @param saveDir
	 * @throws Exception
	 * @throws IOException
	 */
	public static void saveAsFile(byte[] data, String fileName, String saveDir) throws Exception, IOException {

		File dirFile = new File(saveDir);
		// 不存在 创建级联目录
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		String newFilePath = saveDir + File.separator + fileName;
		File file = new File(newFilePath);
		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		if (fos != null) {
			fos.close();
		}

		System.err.println("文件："+newFilePath+" -- 保存成功！");
	}

	public static void main(String[] args) {
		try {
			saveOne("z_18_x_213356_y_104718","E:/tiles/hn/zz/18/z_18_x_213356_y_104718.jpg");
			byte[] data = findOne("z_18_x_213356_y_104718", true);
            saveAsFile(data, "z_18_x_213356_y_104718.jpg", "C:/tile/zz");
			// printAll();
			// deleteAll();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
