package com.appleyk.entity;

import org.springframework.data.annotation.Id;

public class User {

	@Id
	private Long id;
	private String name;
	private int age;
	private String sex;

	
	public User(Long id,String name,int age,String sex){
		this.id = id;
		this.name = name ;
		this.age  = age  ;
		this.sex  = sex  ;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}
