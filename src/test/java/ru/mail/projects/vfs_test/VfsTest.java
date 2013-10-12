package ru.mail.projects.vfs_test;

import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.testng.annotations.Test;

import ru.mail.projects.base.VFS;
import ru.mail.projects.vfs.impl.VFSImpl;

public class VfsTest {
	
	public VFS vfs = new VFSImpl("./resources");
	
	@Test
	public void BaseVFSFunctions() {
		
		assertTrue(vfs.isExist("./src"));
		assertTrue(vfs.isDirectory("./src"));
		Assert.assertEquals(vfs.getRoot(), "./resources");
		Assert.assertNotSame(vfs.getAbsolutePath("./src"), "./src");
	}
	
	@Test 
	public void GetBytesFunctionTest() {
		
		vfs.getBytes("./src");
		vfs.getBytes("Unknownfile");
		Assert.assertNotNull(vfs.getBytes("./resources/userSess.xml"));
	}
	
	@Test 
	public void GetgetUTF8TextFunctionTest() {
		
		vfs.getUTF8Text("./src");
		vfs.getUTF8Text("Unknownfile");
		Assert.assertNotNull(vfs.getUTF8Text("./resources/userSess.xml"));
	}
	
	@Test
	public void FileIteratorTest() {
		
		Assert.assertNotNull(vfs.getIterator("./src").next());
		Assert.assertTrue(vfs.getIterator("./src").hasNext());
		vfs.getIterator("./src").remove();
		Assert.assertTrue(vfs.getIterator("./resources/userSess.xml").hasNext());
		Assert.assertNotNull(vfs.getIterator("./src/main").next());
	}
	
	
		
}
