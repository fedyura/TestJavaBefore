package ru.mail.projects.frontend.impl_test;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.mail.projects.base.MessageSystem;
import ru.mail.projects.base.ResourceSystem;
import ru.mail.projects.base.VFS;
import ru.mail.projects.frontend.impl.FrontendImpl;
import ru.mail.projects.frontend.impl.UserSession;
import ru.mail.projects.message.system.impl.MessageSystemImpl;
import ru.mail.projects.resource.system.impl.ResourceSystemImpl;
import ru.mail.projects.utils.Context;
import ru.mail.projects.utils.LongId;
import ru.mail.projects.utils.SessionId;
import ru.mail.projects.utils.UserId;
import ru.mail.projects.vfs.impl.VFSImpl;

public class FrontEndImplTest {
  
	Context context;
	
	@BeforeClass
	public void PrepareObjects() {
		
		context = new Context();
		VFS vfs = new VFSImpl("./resources");
    	MessageSystem MsgSys = new MessageSystemImpl();

    	context.add(VFS.class, vfs);
    	context.add(MessageSystem.class, MsgSys);
    	ResourceSystem rsSystem = new ResourceSystemImpl(vfs);
    	rsSystem.loadResources();
    	context.add(ResourceSystem.class, rsSystem);
	}
	
	@Test
	public void FrontEndImplConstruct() {
		
		FrontendImpl frEnd1 = new FrontendImpl(context);
		FrontendImpl frEnd2 = new FrontendImpl(context);
		
		Assert.assertEquals("Frontend1", frEnd1.getName());		
		Assert.assertEquals("Frontend2", frEnd2.getName());
		
		//Assert.assertEquals(1, frEnd1.getAddress().hashCode());
	}
	
	@Test(dependsOnMethods = { "FrontEndImplConstruct" })
	public void updateUserIdTest() {
		
		FrontendImpl frEnd1 = new FrontendImpl(context);
		for (int i = 0; i < 5; i++)
		{
			UserSession UsSess = new UserSession();
			frEnd1.sessions.put(UsSess.sessionId.getLong(), UsSess);
		}
		
		LongId<SessionId> sessionId = new LongId<SessionId>(3);
		LongId<UserId> userId = new LongId<UserId>(10);
		
		frEnd1.updateUserId(sessionId, userId);
		Assert.assertEquals(userId, frEnd1.sessions.get(sessionId.getLong()).userId);
		
		LongId<SessionId> sessionId1 = new LongId<SessionId>(4);
		LongId<UserId> userId1 = null;
		
		frEnd1.updateUserId(sessionId1, userId1);
		Assert.assertEquals(new Integer(-1), frEnd1.sessions.get(sessionId1.getLong()).userId.getLong());
		
		frEnd1.replicateFromGM();
	}
	
	@Test(dependsOnMethods = { "FrontEndImplConstruct", "updateUserIdTest" })
	public void showStartGameTest() {
		
		FrontendImpl frEnd1 = new FrontendImpl(context);
		
		UserSession UsSess1 = new UserSession();
		UserSession UsSess2 = new UserSession();
		UsSess1.userName = "Yura";
		UsSess2.userName = "Roma";
		int idGameSession = 7;
		int wUs = FrontendImpl.waitUsersNumber.get();
		frEnd1.showStartedGame(UsSess1, UsSess2, idGameSession);
		
		Assert.assertEquals(true, UsSess1.game_state);
		Assert.assertEquals(true, UsSess2.game_state);
		Assert.assertEquals(false, UsSess1.is_wait);
		Assert.assertEquals(false, UsSess2.is_wait);
		
		Assert.assertEquals(UsSess2.userName, UsSess1.enemyName);
		Assert.assertEquals(UsSess1.userName, UsSess2.enemyName);
		Assert.assertEquals(UsSess1.IdGameSession, idGameSession);
		Assert.assertEquals(FrontendImpl.waitUsersNumber.get(), wUs - 2);
	}
}
