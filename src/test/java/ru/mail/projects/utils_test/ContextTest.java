package ru.mail.projects.utils_test;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.mail.projects.utils.Context;

public class ContextTest {
  
	private String str1, str2;
	private Integer num;
	private Double fract;
	
	@BeforeClass
	public void InitializeObjects() {
		
		str1 = new String("aaa");
		str2 = new String("bbb");
		num = new Integer(10);
		fract = new Double(3.45);
	}
	
	@Test
    public void StoreObjectTest() {
		
		Context context = new Context();
		context.add(String.class, str1);
		context.add(Integer.class, num);
		context.add(Double.class, fract);
		
		Assert.assertEquals(str1, context.get(String.class));
		Assert.assertEquals(num, context.get(Integer.class));
		Assert.assertEquals(fract, context.get(Double.class));
    }
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void IncorrectInsertObject() {
		
		Context context = new Context();
		
		context.add(String.class, str1);
		context.add(String.class, str2);
	}
	
	
}
