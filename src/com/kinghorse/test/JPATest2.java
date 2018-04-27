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
	
	//	Find���� ʹ��ά��������ϵ  �� ��ά��������ϵ  ��һ��
	//	persist ���� ��Ϊ�м��Ĵ��� û���� http://ygbx.cdterp.com/erpApp/HttpsControl
	@Test
	public void testManyToMany(){
		
	}
	
	
	//	2.Ĭ�����������ȡ  �� ά��������ϵ��һ������ͨ���������ӻ�ȡ�������ϵ�Ķ��󡣿���ͨ��@OneToOne��fetch������ �޸ļ��ز��ԣ������أ����ǣ�ûʲô���ã���Ȼ�ᷢ��SQL
	//	����ʼ�������������˵���ڲ�ά��������ϵ��һ�����������޸�fetch����
	@Test
	public void testOneToOneFind2(){
		Manager manager = entityManager.find(Manager.class, 1);
		
		System.out.println(manager.getMgrName());
		System.out.println(manager.getDept().getClass().getName());
	}
	
	//	1.Ĭ�����������ȡά��������ϵ��һ�������ͨ���������ӻ�ȡ�������ϵ�Ķ��󡣿���ͨ��@OneToOne��fetch������ �޸ļ��ز��ԣ������أ�
	@Test
	public void testOneToOneFind(){
		Department department = entityManager.find(Department.class, 1);
		
		System.out.println(department.getDeptName());
		
		System.out.println(department.getManager().getClass().getName());
	}
	
	
	//˫��1-1�������ȱ��治ά��������ϵ��һ������û�������һ��������ദupdate���
	@Test
	public void testOneToOnePersistence() {
		Manager manager = new Manager();
		manager.setMgrName("Mark");
		
		Department department = new Department();
		department.setDeptName("������");
		
		//���ù�����ϵ
		manager.setDept(department);
		department.setManager(manager);
		
		//ִ�б������
		entityManager.persist(manager);
		entityManager.persist(department);
		
		
	}

}
