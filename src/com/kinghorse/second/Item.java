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
	 * 使用@ManyToMany注解来实现多对多关联关系
	 * 使用@JoinTable来映射中间表
	 * 	name-中间表的名称
	 * 	joinColumns-映射当前类所在表的中间表中的外键 name 指定外键列的列名；referencedColumnName指定外键列关联当前表的那一列
	 * 	inverseJoinColumns-映射关联类所在中间表的信息
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
