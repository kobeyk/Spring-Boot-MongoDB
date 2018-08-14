package com.appleyk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.appleyk.entity.Tile;
import com.appleyk.service.TileService;

@Service
@Primary
public class TileServiceImpl implements TileService {

	@Autowired
	private MongoTemplate mongo;

	
	/**
	 * 根据Tile切片对象过滤查询  -- and...and...and 多条件和
	 */
	@Override
	public List<Tile> findData(Tile tile) {

		
		Criteria criteria = new Criteria();
	
		if(tile.getRow()!=null){
			criteria.and("row").is(tile.getRow());
		}
		
		if(tile.getCol()!=null){
			criteria.and("col").is(tile.getCol());
		} 
		
		if(tile.getLevel()!=null){
			criteria.and("level").is(tile.getLevel());
		}
      
		Query query = new Query(criteria);
		//query.limit(pagesize);  //-- 限定返回多少行记录
		Field fields = query.fields();
		fields.include("data"); //-- 只显示data这个字段
		
		query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "id"))); //按id进行 升序
		//Query: { "row" : 1665 , "col" : 814 , "level" : 11}, Fields: { "data" : 1 }, Sort: { "id" : 1}
		List<Tile> tiles =mongo.find(query, Tile.class);
        
		return tiles;
	}

}
