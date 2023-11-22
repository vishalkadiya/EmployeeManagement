package com.employee.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.employee.entity.Employee;
import com.employee.request.DepartmentRequest;

public interface DepartmentService {

	ResponseEntity save(@Valid DepartmentRequest departmentRequest);

	List<Employee> findByDname(String dname);

//	ResponseEntity findByDname(String dname);

}
