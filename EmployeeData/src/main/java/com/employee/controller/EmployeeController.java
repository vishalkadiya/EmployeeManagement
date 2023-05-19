package com.employee.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.entity.Department;
import com.employee.entity.Employee;
import com.employee.request.DepartmentRequest;
import com.employee.request.EmployeeRequest;
import com.employee.service.DepartmentService;
import com.employee.service.EmployeeService;

@RestController
@RequestMapping("/employee/data")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	DepartmentService departmentService;

	@PostMapping
	public ResponseEntity addEmployee(@RequestBody @Valid EmployeeRequest employeeRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage())
					.collect(Collectors.toList());
			return ResponseEntity.badRequest().body(errors);
		
		}

		return employeeService.save(employeeRequest);

	}

	@PostMapping("/department")
	public ResponseEntity addDepartment(@RequestBody @Valid DepartmentRequest departmentRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage())
					.collect(Collectors.toList());
			return ResponseEntity.badRequest().body(errors);

		}
		return departmentService.save(departmentRequest);
	}

	@GetMapping("/{id}")
	public ResponseEntity findById(@PathVariable("id") Integer id) {
		return employeeService.findById(id);
	}
	
	@GetMapping("/emp-dept-name/{dname}")
	public ResponseEntity<List<Employee>> findByDepartmentName(@PathVariable("dname") String dname) {
		List<Employee> list = departmentService.findByDname(dname);
		return ResponseEntity.status(HttpStatus.CREATED).body(list);
	}

	@PutMapping("/{id}")
	public ResponseEntity update(@RequestBody @Valid EmployeeRequest employeeRequest,
			@PathVariable("id") @Valid Integer id) {
		return employeeService.update(id, employeeRequest);
	}

}