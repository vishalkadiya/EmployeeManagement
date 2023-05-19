package com.employee.request;

import javax.validation.constraints.NotBlank;

public class DepartmentRequest {

	@NotBlank(message = "Department name is required")
	private String dname;

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

		
	
}
