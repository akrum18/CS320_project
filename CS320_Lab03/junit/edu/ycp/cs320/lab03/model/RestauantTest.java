package edu.ycp.cs320.lab03.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;



public class RestauantTest {
	private Restaurant r;
	//private ArrayList<Order> o;
	
	private Order TestOrder; 
	private Order TestOrder2;
	private Order TestOrder3;

	private Menu menu; 
	
	private Double price1 = (Double) 14.99;
	private Double price2 = (Double) 9.99; 
	private Double price3 = (Double) 6.99;
	
	
	@Before
	public void setUp() throws Exception {

		r = new Restaurant();
		r.setName("Yorktown");
		r.setAddress("123 4th street");
		r.setCity("york");
		r.setZipCode("12356");
		menu = new Menu(); 
		r.setMenu(menu);
		
		r.getMenu().addToMenu("Large Pizza", price1);
		r.getMenu().addToMenu("Medium Pizza", price2);
		r.getMenu().addToMenu("Small Pizza", price3);
		
		
		
		TestOrder = new Order(); 
		TestOrder2 = new Order();
		TestOrder3 = new Order();
		
		r.ordersList.add(TestOrder);
		r.ordersList.add(TestOrder2);
		r.ordersList.add(TestOrder3);
	}
	
	
	
	@Test
	public void getOrder() {
		assertEquals(3, r.ordersList.size());
	}
	
	@Test
	public void findOrder() {
		r.getOrder(0);
		
	}
}
