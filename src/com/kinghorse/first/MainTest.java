package com.kinghorse.first;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class MainTest {

	public static void main(String[] args) {
		//1.创建EntityManagerFactory
		String persistenceUnitName = "jpa-learn";
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistenceUnitName);
		
		//2.创建EntityManager
		EntityManager entityManager = factory.createEntityManager();
		
		//3.开启事务
		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		//4.进行持久化操作
		/*Student studnet = new Student("Davied", 20, new Date());
		
		entityManager.persist(studnet);*/
		Customer customer = new Customer("Tom", "Tom@163.com", 20, new Date(), new Date());
		
		//类似于Hibernate的save方法 ；与Hibernate的不同之处是，若对象有id ，则不能执行insert操作，而会抛出异常
		entityManager.persist(customer);
		
		//5.提交事务
		transaction.commit();
		
		//6.关闭EntityManager
		entityManager.close();
		
		//7.关闭EntityManagerFactory
		factory.close();
	}

}
