package ru.mail.projects.account.database.impl_test;

import java.util.Queue;

import junit.framework.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.mail.projects.account.database.impl.DatabaseServiceCap;
import ru.mail.projects.base.Address;
import ru.mail.projects.base.MessageSystem;
import ru.mail.projects.base.Msg;
import ru.mail.projects.message.system.impl.MessageSystemImpl;
import ru.mail.projects.messages.MsgUpdateUserId;
import ru.mail.projects.utils.Context;
import ru.mail.projects.utils.LongId;
import ru.mail.projects.utils.SessionId;

public class DataBaseServiceCapTest {
  
	Context context;
	MessageSystem MsgSystem;
	
	@BeforeClass
	public void InitMsgSystem() {
		
		context = new Context();
		MsgSystem = new MessageSystemImpl();
		context.add(MessageSystem.class, MsgSystem);
	}
	
	@Test
    public void CreateServicesTest() {
		
		DatabaseServiceCap dbcap1 = new DatabaseServiceCap(context); 
		DatabaseServiceCap dbcap2 = new DatabaseServiceCap(context);
				
		Assert.assertEquals(dbcap1.getId(), 0);
		Assert.assertEquals(dbcap2.getId(), 1);
		Assert.assertEquals(dbcap1.getCount(), 2);
		Assert.assertEquals(dbcap2.getCount(), 2);
		Assert.assertEquals(dbcap1.getName(), "DatabaseService0");
		Assert.assertEquals(dbcap2.getName(), "DatabaseService1");
		
		dbcap1.setName("AnotherService");
		Assert.assertEquals(dbcap1.getName(), "AnotherService");
    }
	
	@Test(dependsOnMethods = { "CreateServicesTest" })
	public void FindUserTest() {
		
		DatabaseServiceCap dbcap1 = new DatabaseServiceCap(context);
		MsgSystem.addService(dbcap1);
		
		Address addrTo = dbcap1.getAddress();
		
		LongId<SessionId> sId = new LongId<SessionId>(10);
		String name1 = new String("Roma");
		String name2 = new String("10101");
		
		dbcap1.FindUser(sId, name1, addrTo);
		dbcap1.FindUser(sId, name2, addrTo);
		
		Queue<Msg> qMsg = MsgSystem.getQueueMsg(addrTo);
		Msg msgUpd1 = qMsg.poll();
		
		Assert.assertTrue(msgUpd1 instanceof MsgUpdateUserId); 
		MsgUpdateUserId msgUpdUserId1 = (MsgUpdateUserId)msgUpd1;
		
		Assert.assertEquals(addrTo, msgUpdUserId1.getTo());
		Assert.assertEquals(sId, msgUpdUserId1.getSession());
		Assert.assertEquals(dbcap1.getIdByName(name1), msgUpdUserId1.getUserId().getLong());
		
		Msg msgUpd2 = qMsg.poll();
		
		Assert.assertTrue(msgUpd2 instanceof MsgUpdateUserId); 
		MsgUpdateUserId msgUpdUserId2 = (MsgUpdateUserId)msgUpd2;
		
		Assert.assertEquals(addrTo, msgUpdUserId2.getTo());
		Assert.assertEquals(sId, msgUpdUserId2.getSession());
		Assert.assertEquals(dbcap1.getIdByName(name2), msgUpdUserId2.getUserId());
	}
	
	@Test(dependsOnMethods = { "CreateServicesTest" })
	public void AddUserTest() {
		
		DatabaseServiceCap dbcap1 = new DatabaseServiceCap(context);
		MsgSystem.addService(dbcap1);
		
		Address addrTo = dbcap1.getAddress();
		
		LongId<SessionId> sId = new LongId<SessionId>(10);
		String name1 = new String("Roma");
		String name2 = new String("10101");
		
		dbcap1.AddUser(sId, name1, addrTo);
		dbcap1.AddUser(sId, name2, addrTo);
		
		Queue<Msg> qMsg = MsgSystem.getQueueMsg(addrTo);
		Msg msgUpd1 = qMsg.poll();
		
		Assert.assertTrue(msgUpd1 instanceof MsgUpdateUserId); 
		MsgUpdateUserId msgUpdUserId1 = (MsgUpdateUserId)msgUpd1;
		
		Assert.assertEquals(addrTo, msgUpdUserId1.getTo());
		Assert.assertEquals(sId, msgUpdUserId1.getSession());
		Assert.assertEquals(null, msgUpdUserId1.getUserId());
		
		Msg msgUpd2 = qMsg.poll();
		
		Assert.assertTrue(msgUpd2 instanceof MsgUpdateUserId); 
		MsgUpdateUserId msgUpdUserId2 = (MsgUpdateUserId)msgUpd2;
		
		Assert.assertEquals(addrTo, msgUpdUserId2.getTo());
		Assert.assertEquals(sId, msgUpdUserId2.getSession());
		Assert.assertEquals(dbcap1.getCurUserId(), msgUpdUserId2.getUserId().getLong());
	}
	
	
	
	
	
}
