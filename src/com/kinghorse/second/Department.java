package com.kinghorse.second;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="JPA_DEPARTMENT")
public class Department {

	private  Integer id;
	
	private String deptName;
	
	private Manager manager;

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="DEPART_NAME")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * 使用@OneToOne 来映射1-1关联关系
	 * 若需要在当前数据库表中添加主键则需要使用@JoinColumn来进行映射
	 * 注意：1-1关联关系，所以需要添加unique=true
	 * @return
	 */
	@JoinColumn(name="MAG_ID",unique=true)
	@OneToOne
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
	
	
}
