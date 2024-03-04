package utilities;

public class Customer extends User {
	private int userId;
	private String name;
	private String email;
	private long mobile;
	private String status;
	private int age;
	private String gender;
	private long aadhar;
	private String pan;
	private String address;
	private String role = "CUSTOMER";
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setMobile(long mobile) {
		this.mobile = mobile;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setAadhar(long aadhar) {
		this.aadhar = aadhar;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getUserId() {
		return userId;
	}
	public String getName() {
		return this.name;
	}
	public String getEmail() {
		return this.email ;
	}
	public long getMobile() {
		return this.mobile;
	}
	public String getStatus() {
	 return	this.status;
	}
	public int getAge() {
		return this.age ;
	}
	public String getGender() {
		return this.gender;
	}
	public String getRole() {
		return role;
	}
	public long getAadhar() {
		return this.aadhar ;
	}
	public String getPan() {
		return this.pan ;
	}
	public String getAddress() {
		return this.address;
	}
}
