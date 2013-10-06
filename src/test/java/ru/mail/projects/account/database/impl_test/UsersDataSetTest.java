package ru.mail.projects.account.database.impl_test;

import junit.framework.Assert;

import org.testng.annotations.Test;

import ru.mail.projects.account.database.impl.UsersDataSet;

public class UsersDataSetTest {
	
	
	
	@Test
	public void ConstructObj() {
		
		UsersDataSet Uds = new UsersDataSet(5, "Yura");
		UsersDataSet Uds_name = new UsersDataSet("Roma");
		
		Assert.assertEquals(5, Uds.getId());
		Assert.assertEquals(-1, Uds_name.getId());
		Assert.assertEquals("Yura", Uds.getName());
		Assert.assertEquals("Roma", Uds_name.getName());
	}
}
