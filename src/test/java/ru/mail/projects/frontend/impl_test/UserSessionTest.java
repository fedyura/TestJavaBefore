package ru.mail.projects.frontend.impl_test;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ru.mail.projects.base.MessageSystem;
import ru.mail.projects.base.ResourceSystem;
import ru.mail.projects.base.VFS;
import ru.mail.projects.frontend.impl.FrontendImpl;
import ru.mail.projects.frontend.impl.UserSession;
import ru.mail.projects.message.system.impl.MessageSystemImpl;
import ru.mail.projects.resource.system.impl.ResourceSystemImpl;
import ru.mail.projects.utils.Context;
import ru.mail.projects.vfs.impl.VFSImpl;

public class UserSessionTest {
  
	//UserSession usSess; //= new UserSession();
	//UserSession usSess2; //= new UserSession();
	
	@BeforeTest
	public void PrepareObjects() {
		
	}
	
	@Test
	public void ConstructUserSessionObjTest() {
		
		Context context = new Context();
    	VFS vfs = new VFSImpl("./resources");
    	MessageSystem MsgSys = new MessageSystemImpl();

    	context.add(VFS.class, vfs);
    	context.add(MessageSystem.class, MsgSys);
    	ResourceSystem rsSystem = new ResourceSystemImpl(vfs);
    	rsSystem.loadResources();
    	context.add(ResourceSystem.class, rsSystem);
    	
    	FrontendImpl frEnd = new FrontendImpl (context);
    	
    	UserSession usSess = new UserSession();
		UserSession usSess2 = new UserSession();
		
		Assert.assertNotSame(usSess.sessionId.hashCode(), null);
		Assert.assertNotSame(usSess2.sessionId.hashCode(), null);
			
		Assert.assertEquals(usSess.timeToFinish, 10000);
		Assert.assertEquals(usSess2.timeToFinish, 10000);
		
		Assert.assertEquals(usSess.clickByUser, 0);
		Assert.assertEquals(usSess.clickedByEnemy, 0);
		
		Assert.assertEquals(usSess.timeToFinish, UserSession.getPlayTime());
	}
}
