package com.employee.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="department")
public class Department {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int department_id;
	
	@Column(name="depatment_name")
	private String dname;
	
	@OneToMany(mappedBy="department")
	private Set<Employee> employees;
	
	public Department() {
		super();
	}

	public Department(int did, String dname) {
		super();
		this.department_id = did;
		this.dname = dname;
	}

	public int getDid() {
		return department_id;
	}

	public void setDid(int did) {
		this.department_id = did;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

}
