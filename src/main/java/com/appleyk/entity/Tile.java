package com.appleyk.entity;

import org.springframework.data.annotation.Id;

public class Tile {

	@Id
	private Long id;
	
	/**
	 * 切片行 -- X
	 */
	private Integer row;
	
	/**
	 * 切片列 -- Y
	 */
	private Integer col;
	
	/**
	 * 切片级别 -- Z
	 */
	private Integer level;
	
	/**
	 * 切片版本 -- V
	 */
	private Long version;
	
	/**
	 * 切片来源 -- S  -- 比如切片数据来源自Google、百度、天地图等
	 */
	private String source;
	
	/**
	 * 切片的二进制数据
	 */
	private byte[] data;
	
	
	public Tile(){
		
	}
	
	public Tile(Integer row,Integer col,Integer level){
		this.row = row;
		this.col = col;
		this.level = level;
	}
	
	public Tile(Long id,Integer row,Integer col,Integer level,Long version,String source){
		this.id = id;
		this.row = row;
		this.col = col;
		this.level = level;
		this.version = version;
		this.source = source;
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getCol() {
		return col;
	}
	public void setCol(Integer col) {
		this.col = col;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
