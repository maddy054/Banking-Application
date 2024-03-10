package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Branch {
	private int branchId;
	private String branchName;
	private long ifsc;
	private String address;
	
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public long getIfsc() {
		return ifsc;
	}
	public void setIfsc(long ifsc) {
		this.ifsc = ifsc;
		
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setAll(Map<String,Object> branch) {
		setBranchId((int) branch.get("BRANCH_ID"));
		setBranchName((String) branch.get("BRANCH_NAME"));
		setIfsc((long) branch.get("IFSC_CODE"));
		setAddress((String) branch.get("ADDRESS"));
	}
	public List<Object> getAll(){
		
		List<Object> list = new ArrayList<Object>();
		list.add(branchName);
		list.add(ifsc);
		list.add(address);
		return list;
	}
}
