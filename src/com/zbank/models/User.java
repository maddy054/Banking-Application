package com.zbank.models;

import com.zbank.enums.Gender;
import com.zbank.enums.Status;
import com.zbank.enums.UserType;

public class User{
	
	private int userId;
	private String password;
	private String name;
	private String email;
	private long mobile;
	private Status status = Status.ACTIVE;
	private int age;
	private Gender gender;
	private UserType role;
	
	public UserType getRole() {
		return role;
	}
	public void setRole(UserType role) {
		this.role = role;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getMobile() {
		return mobile;
	}
	public void setMobile(long mobile) {
		this.mobile = mobile;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}

}
