package com.brainmentors.los.operation;

import java.util.ArrayList;
import com.brainmentors.los.customer.Customer;
import com.brainmentors.los.customer.PersonalInformation;


public interface DB {
public static ArrayList<Customer> getNegativeCustomers(){
	ArrayList<Customer> negativeCustomers= new ArrayList();
	Customer customer=new Customer();
	customer.setId(1010);
	PersonalInformation pd =new PersonalInformation();
	pd.setFirstName("Tim");
	pd.setLastName("jackson");
	pd.setPhone("22222");
	pd.setPanCard("BW1000");
	pd.setVoterId("A1111");
	pd.setEmail("tim@gamil.com");
	customer.setPersonal(pd);
	negativeCustomers.add(customer);
	customer.setId(1010);
	pd =new PersonalInformation();
	pd.setFirstName("Tom");
	pd.setLastName("dahl");
	pd.setPhone("3333");
	pd.setPanCard("BW2000");
	pd.setVoterId("A222");
	pd.setEmail("tom@gamil.com");
	return negativeCustomers;
	
}
}
