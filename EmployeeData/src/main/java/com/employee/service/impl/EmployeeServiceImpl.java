package com.employee.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.employee.entity.Employee;
import com.employee.entity.Department;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.EmployeeRepository;
import com.employee.request.EmployeeRequest;
import com.employee.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	private static final int Employee = 0;
	
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	DepartmentRepository departmentRepository;
//	@Autowired
//	EmployeeResponse employeeResponse;
	
	@Override
	public ResponseEntity save(EmployeeRequest emp) {
		
		Department department = departmentRepository.findById(emp.getDepartmentId()).get();
		if(department == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid department");
		}
		
		Employee employee = new Employee(emp.getEmp_no(), emp.getFirstName(), emp.getLastName(), emp.getAge(), department);
		
		return ResponseEntity.ok(employeeRepository.save(employee));
	}

	@Override
	public ResponseEntity findById(Integer id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if(!employee.isEmpty()) {
			return ResponseEntity.ok(employee.get());
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not found");
		}
	}


	@Override
	public ResponseEntity update(Integer id, EmployeeRequest employeeRequest) {
		Optional<Employee> empployee = employeeRepository.findById(id);
		if(!empployee.isEmpty()) {
			Employee emp = empployee.get();
			emp.setEmp_no(employeeRequest.getEmp_no());
			emp.setFirstName(employeeRequest.getFirstName());
			emp.setLastName(employeeRequest.getLastName());
			emp.setAge(employeeRequest.getAge());			
			return ResponseEntity.ok(employeeRepository.save(emp));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not found");
		}
	}
	
//	@Override
//	public List<Employee> getAllData() {
//		List<Employee> list = (List<Employee>)employeeRepository.findAll();
//		return list;
//	}
	
	
}
