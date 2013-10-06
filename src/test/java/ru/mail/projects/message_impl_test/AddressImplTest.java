package ru.mail.projects.message_impl_test;

import junit.framework.Assert;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ru.mail.projects.account.database.impl.DatabaseServiceCap;
import ru.mail.projects.base.Address;
import ru.mail.projects.base.MessageSystem;
import ru.mail.projects.base.ResourceSystem;
import ru.mail.projects.base.VFS;
import ru.mail.projects.frontend.impl.FrontendImpl;
import ru.mail.projects.game.mechanics.impl.GameMechanicsImpl;
import ru.mail.projects.message.system.impl.AddressServiceImpl;
import ru.mail.projects.message.system.impl.MessageSystemImpl;
import ru.mail.projects.resource.system.impl.ResourceSystemImpl;
import ru.mail.projects.utils.Context;
import ru.mail.projects.utils.LongId;
import ru.mail.projects.utils.SessionId;
import ru.mail.projects.vfs.impl.VFSImpl;

public class AddressImplTest {
  
	@Test
	public void AssignAddresses() {
		
		AddressServiceImpl ai = new AddressServiceImpl();
		LongId<SessionId> sessionId = new LongId<SessionId>(10); 
		
		Address addr = ai.getAddress("Frontend", sessionId);
		Assert.assertEquals("Frontend" + (sessionId.getLong() % FrontendImpl.count), ai.getName());
		
		addr = ai.getAddress("GameMechanics", sessionId);
		Assert.assertEquals("GameMechanics" + (sessionId.getLong() % GameMechanicsImpl.count), ai.getName());
	}
	
	@BeforeTest
	public void ConstructObjects() {
		
		Context context = new Context();
    	VFS vfs = new VFSImpl("./resources");
    	MessageSystem MsgSys = new MessageSystemImpl();

    	context.add(VFS.class, vfs);
    	context.add(MessageSystem.class, MsgSys);
    	ResourceSystem rsSystem = new ResourceSystemImpl(vfs);
    	rsSystem.loadResources();
    	context.add(ResourceSystem.class, rsSystem);
    	
    	FrontendImpl frEnd = new FrontendImpl (context);
    	DatabaseServiceCap AccSer  = new DatabaseServiceCap(context);
    	DatabaseServiceCap AccSer2 = new DatabaseServiceCap(context);	
    	GameMechanicsImpl Gm = new GameMechanicsImpl (context); 
	}
}
