package com.kinghorse.second;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="JPA_MANAGER")
public class Manager {

	private Integer id;
	
	private String mgrName;
	
	private Department dept;

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="MANAGER_NAME")
	public String getMgrName() {
		return mgrName;
	}

	public void setMgrName(String mgrName) {
		this.mgrName = mgrName;
	}

	//	对于不维护关联关系，没有外键的一方，使用@OneToOne来进行映射，建议设置mappedBy
	@OneToOne(mappedBy="manager")
	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}
	
	
}
