package ru.mail.projects.resources_test;

import junit.framework.Assert;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ru.mail.projects.resources.DbConnectionResource;

public class DBConnectionResourceTest {
	
	DbConnectionResource res;
	
	@BeforeTest
	public void CreateObj() {
		
		res = new DbConnectionResource();
	}
	
	@Test
	public void TestName() {
		
		String name = "Yura";
		res.setUsername(name);
		Assert.assertEquals(name, res.getUsername());
	}
	
	@Test
	public void TestPassword() {
		
		String password = "qwerty";
		res.setPassword(password);
		Assert.assertEquals(password, res.getPassword());
	}
	
	@Test
	public void TestUrl() {
		
		String url = "www.mail.ru";
		res.setUrl(url);
		Assert.assertEquals(url, res.getUrl());
	}
	
	@Test
	public void TestDriver() {
		
		String driver = "jdbc";
		res.setDriver(driver);
		Assert.assertEquals(driver, res.getDriver());
	}
	
	@Test
	public void TestHibernateDialect() {
		
		String hibernate = "hibernate";
		res.setHibernateDialect(hibernate);
		Assert.assertEquals(hibernate, res.getHibernateDialect());
	}
	
	@Test
	public void TestHibernateHbm2ddlAuto() {
		
		String HibernateHbm2ddlAuto = "hib_auto";
		res.setHibernateHbm2ddlAuto(HibernateHbm2ddlAuto);
		Assert.assertEquals(HibernateHbm2ddlAuto, res.getHibernateHbm2ddlAuto());
	}
}
