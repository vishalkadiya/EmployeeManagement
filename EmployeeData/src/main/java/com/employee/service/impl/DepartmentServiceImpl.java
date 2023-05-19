package com.employee.service.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.employee.entity.Department;
import com.employee.entity.Employee;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.EmployeeRepository;
import com.employee.request.DepartmentRequest;
import com.employee.response.EmployeeResponse;
import com.employee.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService{

	@Autowired
	DepartmentRepository departmentRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;

	
	@Override
	public ResponseEntity save(@Valid DepartmentRequest departmentRequest) {
		
		Department department = new Department(0,departmentRequest.getDname());
		
		return ResponseEntity.ok(departmentRepository.save(department));
	}


	@Override
	public List<Employee> findByDname(String dname) {
		Optional<Department> emOptional = departmentRepository.findByDname(dname);
			
			Department department = emOptional.get();
			
			EmployeeResponse employeeResponse = new EmployeeResponse();
			employeeResponse.setDepatmentname(department.getDname());
			
			List<Employee> list = employeeRepository.findByDepartment(department.getDid());
			return list;
	}

}
