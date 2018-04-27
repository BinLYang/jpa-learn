package com.kinghorse.first;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

@NamedQuery(name="testNamedQuery", query="SELECT c FROM Customer c WHERE c.id = ?")
@Cacheable(true)
@Table(name="JPA_CUSTOMERS")
@Entity
public class Customer {

	private Integer id;
	
	private String lastName;
	
	private String email;
	
	private int age;

	private Date createDate;
	
	private Date birth;
	
	private Set<Order> orders = new HashSet<>();
	
	public Customer() {
		super();
	}

	public Customer(String lastName, String email, int age, Date createDate, Date birth) {
		super();
		this.lastName = lastName;
		this.email = email;
		this.age = age;
		this.createDate = createDate;
		this.birth = birth;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="LAST_NAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	//映射单向1-n的关联关系,使用@OneToMany来映射1-n的关联关系，使用@JoinColumn来映射外键列的名称
	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(name="CUSTOMER_ID")
	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", lastName=" + lastName + ", email=" + email + ", age=" + age + ", createDate="
				+ createDate + ", birth=" + birth + "]";
	}
	
	
}
