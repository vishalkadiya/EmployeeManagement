package com.employee.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.employee.entity.Department;

public interface DepartmentRepository extends CrudRepository<Department, Integer>{

	Optional<Department> findByDname(String dname);

}
