package models;

public class Employee extends User{
	
	private String role;
	private int branchId;
	
	
	public void setRole(String role) {
		this.role = role;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
   public int getBranchId() {
	   return branchId;
   }
	public String getRole() {
		return this.role;
	}

	
	
}
