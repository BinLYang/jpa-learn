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
	
	//查询order数量大于2 的哪些Customer
	@Test
	public void testGroupBy(){
		String jpql = "select o.customer from Order o group by o.customer having count(o.id) >=2";
		
		List<Customer> customers = entityMager.createQuery(jpql).getResultList();
		
		System.out.println(customers);
	}
	
	
	//	使用hibernate的查询缓存， 同时配置文件中设置了启用查询缓存
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
	
	//	createNativeQuery适用于本地sql
	@Test
	public void testNativeQuery(){
		String sql = "SELECT age FROM JPA_CUSTOMERS WHERE id = ?";
		Query query = entityMager.createNativeQuery(sql).setParameter(1, 1);
		
		Object customer = query.getSingleResult();
		System.out.println(customer);
	}
	
	//	createNamedQuery适用于 在实体类前边使用@NamedQuery 标记的查询语句
	@Test
	public void TestNamedQuery(){
		Query query = entityMager.createNamedQuery("testNamedQuery").setParameter(1, 3);
		Customer customer = (Customer) query.getSingleResult();
		
		System.out.println(customer);
	}
	
	
	//	默认情况下，若查询部分属性，则返回的Object[]类型结果，或者Object[]类型的List
	//	若想返回对象， 需要有对应参数的构造器，语句也不同
	@Test
	public void testPartlyProperty(){
		String jpql = "SELECT c.lastName , c.age FROM Customer c WHERE c.id > ?";
		//String jpql = "SELECT new Customer( c.lastName , c.age) FROM Customer c WHERE c.id > ?";
		List lists = entityMager.createQuery(jpql).setParameter(1, 1).getResultList();
		
		System.out.println(lists);
	}
	
	//	占位符从1开始， sql语句中 标的名字 要与对应的类一致
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
	
	
	//通过获取 Order实例，通过该实例set Customer 的属性 不用其他语句  通过提交就可以 变更
	@Test
	public void testManyToOne(){
		Order order = entityMager.find(Order.class, 1);
		order.setOrderName("Joinsen-0-1");
		
		//order.getCustomer().setEmail("Joinsen@163.com");
	}
	
	//不能删除1 的一端，因为有外键约束
	@Test
	public void testManyToOneRemove(){
		Customer customer = entityMager.find(Customer.class, 3);
		
		entityMager.remove(customer);//	失败
	}
	
	
	@Test
	public void testManyToOneFind(){
		Order order = entityMager.find(Order.class, 1);
		
		System.out.println(order.getOrderName());
	}
	
	//	保存多对一时，建议先保存 1 的一端，后保存 n的一端
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
	
	
	//	同hibernate Session的refresh方法
	@Test
	public void testRefresh() {
		Customer customer = entityMager.find(Customer.class, 1);
		customer = entityMager.find(Customer.class, 1);
		
		entityMager.refresh(customer);
		
		System.out.println(customer);
		
	}
	
	
	//	同hibernate Session的flush方法
	@Test
	public void testFlush() {
		Customer customer = entityMager.find(Customer.class, 1);
		customer.setEmail("Tom@hirisun.com");
		
		entityMager.flush();
	}
	
	
	/**
	 * 总的来说：类似于hibernate Session的 saveOrUpdate 方法
	 */
	
	//	若传入一个游离对象，即传入的对象有OID
	// 1.若EntityManager缓存中有该对象
	// 2.JPA会把游离对象的属性复制到查询到EntityManager缓存中的对象中
	// 3.EntityManager缓存中的对象执行update操作
	@Test
	public void testMerge4(){
		Customer customer = new Customer("David", "David@hirisun.com", 26, new Date(), 
				Date.from(LocalDate.of(1992, 6, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		customer.setId(2);
		
		Customer customer2 = entityMager.find(Customer.class, 2);	//	取到缓存中来
		
		entityMager.merge(customer);
		
		System.out.println("customer#id " + customer.getId());
		System.out.println("customer2#id " + customer2.getId());
	}
	
	//	若传入一个游离对象，即传入的对象有OID
	// 1.若EntityManager缓存中没有该对象
	// 2.若在数据库中有对应的记录
	// 3.JPA会查询对应的记录，然后返回该记录的对象，然后把游离对象的属性复制到查询到的该对象中
	// 4.对查询到的对象执行update操作
	@Test
	public void testMerge3(){
		Customer customer = new Customer("David", "David@hirisun.com", 26, new Date(), 
				Date.from(LocalDate.of(1992, 6, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		customer.setId(2);
		
		Customer customer2 = entityMager.merge(customer);
		
		System.out.println("customer#id " + customer.getId());
		System.out.println("customer2#id " + customer2.getId());
	}
	
	//	若传入一个游离对象，即传入的对象有OID
	// 1.若EntityManager缓存中没有该对象
	// 2.若在数据库中也没有对应的记录
	// 3.JPA会创建一个新对象，然后把当前游离的对象的属性复制到一个新创建的对象中（同 testMerge1()）
	// 4.对新创建的对象执行insert操作
	public void testMerge2(){
		Customer customer = new Customer("David", "David@163.com", 26, new Date(), 
				Date.from(LocalDate.of(1992, 6, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		customer.setId(100);
		
		Customer customer2 = entityMager.merge(customer);
		
		System.out.println("customer#id " + customer.getId());
		System.out.println("customer2#id " + customer2.getId());
	}
	
	
	//传入的是一个临时对象
	@Test
	public void testMerge1(){
		Customer customer = new Customer("Bob", "Bob@163.com", 26, new Date(), 
				Date.from(LocalDate.of(1992, 6, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		Customer customer2 = entityMager.merge(customer);
		
		System.out.println("customer#id " + customer.getId());
		System.out.println("customer2#id " + customer2.getId());
	}
	
	//类似于Hibernate的 delete 方法，注意：该方法只能删除持久化的对象，而hibernate可以删除游离对象
	@Test
	public void testRemove() {
		//	游离对象
		/*Customer customer = new Customer();
		customer.setId(1);*/
		Customer customer = entityMager.find(Customer.class, 1);
		
		entityMager.remove(customer);
	}
	
	
	//	类似于Hibernate 中Session 的load 方法
	@Test
	public void testGetReference(){
		Customer customer = entityMager.getReference(Customer.class	, 1);
		
		System.out.println(customer.getClass().getName());
		
		System.out.println("------------------------------------");
		
		System.out.println(customer);
	}
	
	
	//	类似于Hibernate中 Session的get方法
	@Test
	public void testFind() {

		Customer customer = entityMager.find(Customer.class	, 1);
		
		System.out.println(customer.getClass().getName());
		
		System.out.println("------------------------------------");
		
		System.out.println(customer);
	}

}
