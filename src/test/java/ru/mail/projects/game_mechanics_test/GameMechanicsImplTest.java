package ru.mail.projects.game_mechanics_test;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.mail.projects.base.Address;
import ru.mail.projects.base.MessageSystem;
import ru.mail.projects.base.ResourceSystem;
import ru.mail.projects.base.VFS;
import ru.mail.projects.frontend.impl.FrontendImpl;
import ru.mail.projects.frontend.impl.UserSession;
import ru.mail.projects.game.mechanics.impl.GameMechanicsImpl;
import ru.mail.projects.game.mechanics.impl.GameSession;
import ru.mail.projects.message.system.impl.MessageSystemImpl;
import ru.mail.projects.resource.system.impl.ResourceSystemImpl;
import ru.mail.projects.utils.Context;
import ru.mail.projects.vfs.impl.VFSImpl;

public class GameMechanicsImplTest {
	
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
	public void GameMechanicsConstructTest() {
		
		GameMechanicsImpl gm1 = new GameMechanicsImpl(context);
		GameMechanicsImpl gm2 = new GameMechanicsImpl(context);
		
		//Assert.assertEquals("GameMechanics1", gm1.getName());		
		//Assert.assertEquals("GameMechanics2", gm2.getName());
		
		String newName = new String("TestMechanic"); 
		gm1.setName(newName);
		Assert.assertEquals(newName, gm1.getName());
		
		Assert.assertEquals(gm1.getAddress(), gm2.getAddress());
		gm1.endGame();
	}
	
	@Test(dependsOnMethods = { "GameMechanicsConstructTest" })
	public void DoProgressTest() {
		
		FrontendImpl frEnd1 = new FrontendImpl(context);
		GameMechanicsImpl gm1 = new GameMechanicsImpl(context);
			
		UserSession UsSess1 = new UserSession();
		UserSession UsSess2 = new UserSession();
		
		Address addr = new Address();
		//Integer gm_num = new Integer(GameMechanicsImpl.gameSessId.get());
		
		gm1.startGame(UsSess1, UsSess2, addr);
		Assert.assertNotNull(gm1.gameSessions.get(GameMechanicsImpl.gameSessId.get()));
		
		UsSess1.IdGameSession = 1;
		GameSession curGameSess = gm1.gameSessions.get(UsSess1.IdGameSession);
		int cl_enemy = curGameSess.getSecondUserId().clickedByEnemy;
		int UsClick = UsSess1.clickByUser;
		gm1.DoProgress(UsSess1);
		
		Assert.assertEquals(UsClick + 1, UsSess1.clickByUser);
		Assert.assertEquals(cl_enemy + 1, curGameSess.getSecondUserId().clickedByEnemy);
		
		gm1.replicateGamesToFrontEnd();
	}
}
