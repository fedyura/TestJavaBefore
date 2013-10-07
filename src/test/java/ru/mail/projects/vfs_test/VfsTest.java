package ru.mail.projects.vfs_test;

import static org.junit.Assert.assertTrue;

import org.testng.annotations.Test;

import ru.mail.projects.base.VFS;
import ru.mail.projects.vfs.impl.VFSImpl;

public class VfsTest {
	
	public VFS vfs = new VFSImpl("./resources");
	
	@Test
	public void BaseVFSFunctions() {
		
		assertTrue(vfs.isExist("./src"));
		assertTrue(vfs.isDirectory("./src"));
	}
}
