package com.employee.service;

import com.employee.request.ExportDataRequest;
import org.springframework.http.ResponseEntity;
import com.employee.request.EmployeeRequest;

import javax.mail.MessagingException;
import java.io.File;

public interface EmployeeService {

	public ResponseEntity save(EmployeeRequest employeeRequest);

	public ResponseEntity findById(Integer id);

	public ResponseEntity update(Integer id, EmployeeRequest employeeRequest);

	void sendMailWithAttachment(ExportDataRequest exportDataRequest, File csvFile) throws MessagingException;
}
