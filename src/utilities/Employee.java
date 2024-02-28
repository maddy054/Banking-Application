package utilities;

public class Employee extends User{
	private String name;
	private String email;
	private long mobile;
	private String status;
	private int age;
	private String gender;
	private String role;
	private int branchId;
	
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
	public void setRole(String role) {
		this.role = role;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
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
		return this.role;
	}
	public int getBranchId() {
		return this.branchId;
	}
}
