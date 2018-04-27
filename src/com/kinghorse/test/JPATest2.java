package com.kinghorse.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kinghorse.first.Customer;
import com.kinghorse.second.Department;
import com.kinghorse.second.Manager;

public class JPATest2 {
	
	private EntityManagerFactory factory;
	
	private EntityManager entityManager;
	
	private EntityTransaction transation;
	
	@Before
	public void init(){
		
		factory = Persistence.createEntityManagerFactory("jpa-learn");
		entityManager = factory.createEntityManager();
		transation = entityManager.getTransaction();
		transation.begin();
	}

	@After
	public void destory(){
		
		transation.commit();
		entityManager.close();
		factory.close();
	}
	
	//	Find方法 使用维护关联关系  与 不维护关联关系  都一样
	//	persist 保存 因为中间表的存在 没区别 http://ygbx.cdterp.com/erpApp/HttpsControl
	@Test
	public void testManyToMany(){
		
	}
	
	
	//	2.默认情况下若获取  不 维护关联关系的一方，会通过左外链接获取其关联关系的对象。可以通过@OneToOne的fetch属性来 修改加载策略（懒加载）但是，没什么作用，依然会发送SQL
	//	来初始化其关联对象，这说明在不维护关联关系的一方，不建议修改fetch属性
	@Test
	public void testOneToOneFind2(){
		Manager manager = entityManager.find(Manager.class, 1);
		
		System.out.println(manager.getMgrName());
		System.out.println(manager.getDept().getClass().getName());
	}
	
	//	1.默认情况下若获取维护关联关系的一方，则会通过左外链接获取其关联关系的对象。可以通过@OneToOne的fetch属性来 修改加载策略（懒加载）
	@Test
	public void testOneToOneFind(){
		Department department = entityManager.find(Department.class, 1);
		
		System.out.println(department.getDeptName());
		
		System.out.println(department.getManager().getClass().getName());
	}
	
	
	//双向1-1，建议先保存不维护关联关系的一方，即没有外键的一方。不会多处update语句
	@Test
	public void testOneToOnePersistence() {
		Manager manager = new Manager();
		manager.setMgrName("Mark");
		
		Department department = new Department();
		department.setDeptName("生产部");
		
		//设置关联关系
		manager.setDept(department);
		department.setManager(manager);
		
		//执行保存操作
		entityManager.persist(manager);
		entityManager.persist(department);
		
		
	}

}
