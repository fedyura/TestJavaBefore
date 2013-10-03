package ru.mail.projects.base_test;

import junit.framework.Assert;

import org.testng.annotations.Test;

import ru.mail.projects.base.Address;

public class AddressTest {
  
  @Test
  public void ObjectsNumberTest() {
	  
	  Address addr1 = new Address();
	  Address addr2 = new Address();
	  	  
	  Assert.assertNotSame(0, addr1.hashCode());
	  Assert.assertNotSame(1, addr2.hashCode());
  }
}
