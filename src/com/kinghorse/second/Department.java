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
	 * ʹ��@OneToOne ��ӳ��1-1������ϵ
	 * ����Ҫ�ڵ�ǰ���ݿ���������������Ҫʹ��@JoinColumn������ӳ��
	 * ע�⣺1-1������ϵ��������Ҫ���unique=true
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
