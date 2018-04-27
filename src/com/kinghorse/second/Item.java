package com.kinghorse.second;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="JPA_ITEM")

public class Item {

	private Integer id;
	
	private String itemName;
	
	private Set<Category> categories = new HashSet<>();

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	@Column(name="ITEM_NAME")
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * ʹ��@ManyToManyע����ʵ�ֶ�Զ������ϵ
	 * ʹ��@JoinTable��ӳ���м��
	 * 	name-�м�������
	 * 	joinColumns-ӳ�䵱ǰ�����ڱ���м���е���� name ָ������е�������referencedColumnNameָ������й�����ǰ�����һ��
	 * 	inverseJoinColumns-ӳ������������м�����Ϣ
	 * @return
	 */
	@ManyToMany
	@JoinTable(name="ITEM_CATEGORY",
			joinColumns={@JoinColumn(name="ITEM_ID",referencedColumnName="ID")},
			inverseJoinColumns={@JoinColumn(name="CATEGORY_ID",referencedColumnName="ID")})
	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	
}
