package com.kinghorse.first;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="JPA_STUDENT")
public class Student {

	private Integer Id;
	
	private String  name;
	
	private int age;
	
	private Date birth;

	public Student() {
		super();
	}

	public Student(String name, int age, Date birth) {
		super();
		this.name = name;
		this.age = age;
		this.birth = birth;
	}

	@Id
	@TableGenerator(name="ID_GENERATOR",
					table="jpa_id_generators",
					pkColumnName="PK_NAME",
					pkColumnValue="STUDENT_ID",
					valueColumnName="PK_VALUE",
					allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE,generator="ID_GENERATOR")
	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
	
}
