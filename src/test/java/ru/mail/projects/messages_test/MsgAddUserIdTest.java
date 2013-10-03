package ru.mail.projects.messages_test;

import junit.framework.Assert;

import org.testng.annotations.Test;

import ru.mail.projects.base.Address;
import ru.mail.projects.messages.MsgAddUserId;
import ru.mail.projects.utils.LongId;
import ru.mail.projects.utils.SessionId;

public class MsgAddUserIdTest {
	
	@Test
	public void ConstructMsgTest() {
		
		Address addrFr = new Address();
		Address addrTo = new Address();
		LongId<SessionId> sessionId = new LongId<SessionId>(10);
		
		MsgAddUserId msgUpd = new MsgAddUserId(addrFr, addrTo, sessionId, "Yura");
		Assert.assertEquals(msgUpd.getAddrFrom(), addrFr);
		Assert.assertEquals(msgUpd.getUserName(), "Yura");
		Assert.assertEquals(msgUpd.getSessionId(), sessionId);
	}
}
