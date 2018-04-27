package com.kinghorse.first;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="JPA_ORDERS")
public class Order {

	private Integer id;
	
	private String orderName;
	
	private Customer customer;
	
	public Order() {

	}

	public Order(String orderName) {
		super();
		this.orderName = orderName;
	}

	/*public Order(String orderName, Customer customer) {
		super();
		this.orderName = orderName;
		this.customer = customer;
	}*/

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="ORDER_NAME")
	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	//映射单向 n-1的关联关系,使用@ManyToOne来映射多对一的关联关系，使用@JoinColumn 来映射外键
	@ManyToOne(fetch=FetchType.LAZY)//懒加载设置
	@JoinColumn(name="CUSTOMER_ID")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", orderName=" + orderName + ", customer=" + customer + "]";
	}
	
	
}
