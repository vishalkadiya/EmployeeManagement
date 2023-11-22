package com.employee.controller;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.employee.request.ExportDataRequest;
import com.employee.request.Interest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.employee.entity.Employee;
import com.employee.repository.EmployeeRepository;
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
	@Autowired
	EmployeeRepository employeeRepository;
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
	@GetMapping("/create-servlet-cookie")
	public String setCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie servletCookie = createCookie("user-id", "c2FtLnNtaXRoQGV4YW1wbGUuY29t", 1 * 24 * 60 * 60, true, true,
				"/", request.getServerName());
		response.addCookie(servletCookie);
		return String.format("Cookie with name %s and value %s was created", servletCookie.getName(),
				servletCookie.getValue());
	}
	@GetMapping("/delete-servlet-cookie")
	public String deleteCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie deleteServletookie = createCookie("user-id", null, 0, true, true, "/single-create",
				request.getServerName());
		response.addCookie(deleteServletookie);
		return String.format("Cookie with name %s was deleted", deleteServletookie.getName());
	}
	@GetMapping("/all-servlet-cookies")
	public String readAllCookies(HttpServletRequest request) {
		return readCookie(request, "user-id").orElse("cookie with name \"user-id\" is missing");

	}
	@GetMapping("/create-cookie")
	public String setCookies(HttpServletRequest request, HttpServletResponse response, @RequestBody Interest interest)
			throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(interest);
		// System.out.println(jsonString);
		Cookie servletCookie = createCookie("demo-id", jsonString, 1 * 24 * 60 * 60, true, true, "/object-create",
				request.getServerName());
		response.addCookie(servletCookie);
		return String.format("Cookie created");
	}
	@GetMapping("/all-cookies")
	public String readCookies(HttpServletRequest request) {
		return readCookie(request, "demo-id").orElse("cookie with name \"demo-id\" is missing");
	}
	private Cookie createCookie(String name, String value, int expiry, boolean isSecure, boolean httpOnly, String path,
			String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expiry);
		cookie.setSecure(true);
		cookie.setHttpOnly(httpOnly);
		cookie.setPath(path);
		cookie.setDomain(domain);
		return cookie;
	}
	private Optional<String> readCookie(HttpServletRequest request, String name) {
		return Arrays.stream(request.getCookies())
				.filter(cookie -> name.equals(cookie.getName()))
				.map(Cookie::getValue)
				.findAny();
	}
	@Bean
	WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
		return new WebServerFactoryCustomizer<TomcatServletWebServerFactory>() {
			@Override
			public void customize(TomcatServletWebServerFactory tomcatServletWebServerFactory) {
				tomcatServletWebServerFactory.addContextCustomizers(new TomcatContextCustomizer() {
					@Override
					public void customize(org.apache.catalina.Context context) {
						context.setCookieProcessor(new LegacyCookieProcessor());
					}
				});
			}
		};
	}
	@GetMapping("/export")
	public String exportDataToCSV(@RequestBody @Valid ExportDataRequest exportDataRequest) throws MessagingException {
		Iterable<Employee> dataList = employeeRepository.findAll();
		// Generate file name with current date and time
		String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
		try (FileWriter fileWriter = new FileWriter(fileName);
		CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)){
			// Write data to CSV file
			for (Employee data : dataList) {
				csvPrinter.printRecord(data.getId(), data.getEmp_no(), data.getFirstName(), data.getLastName(),
						data.getAge());
			}
			csvPrinter.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed to export data to CSV.";
		}
		File csvFile = new File(fileName);
		employeeService.sendMailWithAttachment(exportDataRequest, csvFile);
		return "Data exported to CSV file: " + fileName;
	}
}