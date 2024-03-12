package com.zbank.models;

public class Employee extends User{
	
	private int branchId;
	
	
	
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
   public int getBranchId() {
	   return branchId;
   }
	
}
