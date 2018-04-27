package com.kinghorse.test;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.QueryHint;

import org.hibernate.ejb.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TestName;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.kinghorse.first.Customer;
import com.kinghorse.first.Order;

public class JPATest {
	
	private EntityManagerFactory factory;
	
	private EntityManager entityMager;
	
	private EntityTransaction entityTransaction;
	
	@Before
	public void init(){
		factory = Persistence.createEntityManagerFactory("jpa-learn");
		entityMager = factory.createEntityManager();
		entityTransaction = entityMager.getTransaction();
		entityTransaction.begin();
	}

	@After
	public void destory(){
		entityTransaction.commit();
		entityMager.close();
		factory.close();
	}
	
	//��ѯorder��������2 ����ЩCustomer
	@Test
	public void testGroupBy(){
		String jpql = "select o.customer from Order o group by o.customer having count(o.id) >=2";
		
		List<Customer> customers = entityMager.createQuery(jpql).getResultList();
		
		System.out.println(customers);
	}
	
	
	//	ʹ��hibernate�Ĳ�ѯ���棬 ͬʱ�����ļ������������ò�ѯ����
	@Test
	public void testQueryCache(){
		String jpql = "FROM Customer c WHERE c.age > ?";
		Query query = entityMager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		query.setParameter(1, 20);
		List<Customer> customers = query.getResultList();
		
		System.out.println(customers);
		
		query = entityMager.createQuery(jpql).setHint(QueryHints.HINT_CACHEABLE, true);
		
		query.setParameter(1, 20);
		customers = query.getResultList();
		
		System.out.println(customers);
	}
	
	//	createNativeQuery�����ڱ���sql
	@Test
	public void testNativeQuery(){
		String sql = "SELECT age FROM JPA_CUSTOMERS WHERE id = ?";
		Query query = entityMager.createNativeQuery(sql).setParameter(1, 1);
		
		Object customer = query.getSingleResult();
		System.out.println(customer);
	}
	
	//	createNamedQuery������ ��ʵ����ǰ��ʹ��@NamedQuery ��ǵĲ�ѯ���
	@Test
	public void TestNamedQuery(){
		Query query = entityMager.createNamedQuery("testNamedQuery").setParameter(1, 3);
		Customer customer = (Customer) query.getSingleResult();
		
		System.out.println(customer);
	}
	
	
	//	Ĭ������£�����ѯ�������ԣ��򷵻ص�Object[]���ͽ��������Object[]���͵�List
	//	���뷵�ض��� ��Ҫ�ж�Ӧ�����Ĺ����������Ҳ��ͬ
	@Test
	public void testPartlyProperty(){
		String jpql = "SELECT c.lastName , c.age FROM Customer c WHERE c.id > ?";
		//String jpql = "SELECT new Customer( c.lastName , c.age) FROM Customer c WHERE c.id > ?";
		List lists = entityMager.createQuery(jpql).setParameter(1, 1).getResultList();
		
		System.out.println(lists);
	}
	
	//	ռλ����1��ʼ�� sql����� ������� Ҫ���Ӧ����һ��
	@Test
	public void testJPAQL(){
		String jpql = "FROM Customer c WHERE c.age > ?";
		Query query = entityMager.createQuery(jpql);
		
		query.setParameter(1, 20);
		List<Customer> customers = query.getResultList();
		
		System.out.println(customers);
	}
	
	
	@Test
	public void testOneToMany(){
		Customer customer = entityMager.find(Customer.class, 1);
		
		Order order1 = new Order("Tom-11-1");
		Order order2 = new Order("Tom-11-2");
		
		customer.getOrders().add(order1);
		customer.getOrders().add(order2);
		
		entityMager.persist(order1);
		entityMager.persist(order2);
	}
	
	
	//ͨ����ȡ Orderʵ����ͨ����ʵ��set Customer ������ �����������  ͨ���ύ�Ϳ��� ���
	@Test
	public void testManyToOne(){
		Order order = entityMager.find(Order.class, 1);
		order.setOrderName("Joinsen-0-1");
		
		//order.getCustomer().setEmail("Joinsen@163.com");
	}
	
	//����ɾ��1 ��һ�ˣ���Ϊ�����Լ��
	@Test
	public void testManyToOneRemove(){
		Customer customer = entityMager.find(Customer.class, 3);
		
		entityMager.remove(customer);//	ʧ��
	}
	
	
	@Test
	public void testManyToOneFind(){
		Order order = entityMager.find(Order.class, 1);
		
		System.out.println(order.getOrderName());
	}
	
	//	������һʱ�������ȱ��� 1 ��һ�ˣ��󱣴� n��һ��
	@Test
	public void testManyToOnePersist() {
		Customer customer = new Customer("Joinsen", "Joinsen@Hirisun.com", 27, new Date(), 
				Date.from(LocalDate.of(1991, 1, 28).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		/*Order order1 = new Order("O-FF-1", customer);
		Order order2 = new Order("O-FF-2", customer);
		
		entityMager.persist(customer);
		entityMager.persist(order1);
		entityMager.persist(order2);*/
	}
	
	
	//	ͬhibernate Session��refresh����
	@Test
	public void testRefresh() {
		Customer customer = entityMager.find(Customer.class, 1);
		customer = entityMager.find(Customer.class, 1);
		
		entityMager.refresh(customer);
		
		System.out.println(customer);
		
	}
	
	
	//	ͬhibernate Session��flush����
	@Test
	public void testFlush() {
		Customer customer = entityMager.find(Customer.class, 1);
		customer.setEmail("Tom@hirisun.com");
		
		entityMager.flush();
	}
	
	
	/**
	 * �ܵ���˵��������hibernate Session�� saveOrUpdate ����
	 */
	
	//	������һ��������󣬼�����Ķ�����OID
	// 1.��EntityManager�������иö���
	// 2.JPA��������������Ը��Ƶ���ѯ��EntityManager�����еĶ�����
	// 3.EntityManager�����еĶ���ִ��update����
	@Test
	public void testMerge4(){
		Customer customer = new Customer("David", "David@hirisun.com", 26, new Date(), 
				Date.from(LocalDate.of(1992, 6, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		customer.setId(2);
		
		Customer customer2 = entityMager.find(Customer.class, 2);	//	ȡ����������
		
		entityMager.merge(customer);
		
		System.out.println("customer#id " + customer.getId());
		System.out.println("customer2#id " + customer2.getId());
	}
	
	//	������һ��������󣬼�����Ķ�����OID
	// 1.��EntityManager������û�иö���
	// 2.�������ݿ����ж�Ӧ�ļ�¼
	// 3.JPA���ѯ��Ӧ�ļ�¼��Ȼ�󷵻ظü�¼�Ķ���Ȼ��������������Ը��Ƶ���ѯ���ĸö�����
	// 4.�Բ�ѯ���Ķ���ִ��update����
	@Test
	public void testMerge3(){
		Customer customer = new Customer("David", "David@hirisun.com", 26, new Date(), 
				Date.from(LocalDate.of(1992, 6, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		customer.setId(2);
		
		Customer customer2 = entityMager.merge(customer);
		
		System.out.println("customer#id " + customer.getId());
		System.out.println("customer2#id " + customer2.getId());
	}
	
	//	������һ��������󣬼�����Ķ�����OID
	// 1.��EntityManager������û�иö���
	// 2.�������ݿ���Ҳû�ж�Ӧ�ļ�¼
	// 3.JPA�ᴴ��һ���¶���Ȼ��ѵ�ǰ����Ķ�������Ը��Ƶ�һ���´����Ķ����У�ͬ testMerge1()��
	// 4.���´����Ķ���ִ��insert����
	public void testMerge2(){
		Customer customer = new Customer("David", "David@163.com", 26, new Date(), 
				Date.from(LocalDate.of(1992, 6, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		customer.setId(100);
		
		Customer customer2 = entityMager.merge(customer);
		
		System.out.println("customer#id " + customer.getId());
		System.out.println("customer2#id " + customer2.getId());
	}
	
	
	//�������һ����ʱ����
	@Test
	public void testMerge1(){
		Customer customer = new Customer("Bob", "Bob@163.com", 26, new Date(), 
				Date.from(LocalDate.of(1992, 6, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		Customer customer2 = entityMager.merge(customer);
		
		System.out.println("customer#id " + customer.getId());
		System.out.println("customer2#id " + customer2.getId());
	}
	
	//������Hibernate�� delete ������ע�⣺�÷���ֻ��ɾ���־û��Ķ��󣬶�hibernate����ɾ���������
	@Test
	public void testRemove() {
		//	�������
		/*Customer customer = new Customer();
		customer.setId(1);*/
		Customer customer = entityMager.find(Customer.class, 1);
		
		entityMager.remove(customer);
	}
	
	
	//	������Hibernate ��Session ��load ����
	@Test
	public void testGetReference(){
		Customer customer = entityMager.getReference(Customer.class	, 1);
		
		System.out.println(customer.getClass().getName());
		
		System.out.println("------------------------------------");
		
		System.out.println(customer);
	}
	
	
	//	������Hibernate�� Session��get����
	@Test
	public void testFind() {

		Customer customer = entityMager.find(Customer.class	, 1);
		
		System.out.println(customer.getClass().getName());
		
		System.out.println("------------------------------------");
		
		System.out.println(customer);
	}

}
