package com.employee.service.impl;

import java.io.File;
import java.util.Optional;

import com.employee.request.ExportDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.employee.entity.Employee;
import com.employee.entity.Department;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.EmployeeRepository;
import com.employee.request.EmployeeRequest;
import com.employee.service.EmployeeService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	private static final int Employee = 0;
	
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	DepartmentRepository departmentRepository;
	@Autowired
	JavaMailSender mailSender;

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

	@Override
	public void sendMailWithAttachment(ExportDataRequest exportDataRequest, File csvFile) throws MessagingException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom("vishalkadiya5989@gmail.com");
			mimeMessageHelper.setTo(exportDataRequest.getEmail());
			mimeMessageHelper.setText(exportDataRequest.getMessage());
			mimeMessageHelper.setSubject(exportDataRequest.getSubject());

			FileSystemResource fileResource = new FileSystemResource(csvFile);
			mimeMessageHelper.addAttachment(csvFile.getName(), fileResource);
			mailSender.send(mimeMessage);

			System.out.println("Attachment mail sent successfully");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


//	@Override
//	public List<Employee> getAllData() {
//		List<Employee> list = (List<Employee>)employeeRepository.findAll();
//		return list;
//	}
	
	
}
