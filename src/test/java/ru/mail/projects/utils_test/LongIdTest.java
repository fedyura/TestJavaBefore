package ru.mail.projects.utils_test;

import junit.framework.Assert;

import org.testng.annotations.Test;

import ru.mail.projects.utils.LongId;

public class LongIdTest {
  
	@Test
    public void creationObjectTest() {
		
	    Integer id_int = new Integer(5);
	    Integer id_str = new Integer(8);
		LongId<Integer> lid_int = new LongId<Integer>(id_int);
	    LongId<String> lid_str = new LongId<String>(id_str);
	    
	    Assert.assertEquals(id_int, lid_int.getLong());
	    Assert.assertEquals(id_str, lid_str.getLong());
	    
	    Assert.assertEquals(id_int.intValue(), lid_int.hashCode());
	    Assert.assertEquals(id_str.intValue(), lid_str.hashCode());
    }
	
	@Test
    public void compareObjectTest() {
		
	    Integer id_int1 = new Integer(5);
	    Integer id_str1 = new Integer(8);
	    Integer id_str2 = new Integer(5);
	    
		LongId<Integer> lid_int1 = new LongId<Integer>(id_int1);
	    LongId<String> lid_str1 = new LongId<String>(id_str1);
	    LongId<String> lid_str2 = new LongId<String>(id_str2);
	    LongId<String> lid_str3 = new LongId<String>(id_str2);
	    
	    Assert.assertTrue(lid_int1.equals(lid_str2));
	    Assert.assertFalse(lid_str1.equals(lid_str2));
	    Assert.assertTrue(lid_str3.equals(lid_str2));
    }
}
