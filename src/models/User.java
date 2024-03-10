package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User{
	
	private int userId;
	private String password;
	private String name;
	private String email;
	private long mobile;
	private String status = "ACTIVE";
	private int age;
	private String gender;
	private String role;
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<Object> getAll() {
		List<Object> customer = new ArrayList<Object>();
		
		customer.add(userId);
		customer.add(password);
		customer.add(name);
		customer.add(mobile);
		customer.add(email);
		customer.add(age);
		customer.add(gender);
		customer.add(status);
		customer.add(role);
		return customer; 
	}
	public void setAllUser(Map<String,Object> user) {
		
		setUserId((int) user.get("USER_ID"));
		setName((String) user.get("NAME"));
		setMobile((long) user.get("MOBILE"));
		setEmail((String) user.get("EMAIL"));
		setAge((int) user.get("AGE"));
		setGender((String) user.get("GENDER"));
		setStatus((String) user.get("STATUS"));
	}

}
