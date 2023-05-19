package com.employee.service;

import org.springframework.http.ResponseEntity;
import com.employee.request.EmployeeRequest;

public interface EmployeeService {

	public ResponseEntity save(EmployeeRequest employeeRequest);

	public ResponseEntity findById(Integer id);

	public ResponseEntity update(Integer id, EmployeeRequest employeeRequest);
	
}
