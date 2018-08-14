package com.appleyk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.appleyk.entity.Tile;

public interface TileReponsitory extends MongoRepository<Tile, Long>{
	
	/**
	 * 其中value是查询的条件，？0这个是占位符，对应着方法中参数中的第一个参数
	 * fields是我们指定的返回字段，其中id是自动返回的，不用我们指定
	 * bson中{'data':1}的1代表true，也就是代表返回的意思
	 * @param row
	 * @param col
	 * @param level
	 * @return
	 */
	
	//db.tile.find({"level":12,"col":351,"row":220,"version":758});
	/**
	 * 只显示data
	 *  db.tile.find({"level":11,"col":814,"row":1665,"version":781},{"data":1});
	 */
	
	/**
	 * 根据row + col + level 查询 二进制
	 * @param row
	 * @param col
	 * @param level
	 * @return
	 */
	 @Query(value="{'row':?0}",fields="{'data':1}")
	 List<Tile> findData(Integer row);
			
}
