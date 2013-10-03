package ru.mail.projects.messages_test;

import junit.framework.Assert;

import org.testng.annotations.Test;

import ru.mail.projects.base.Address;
import ru.mail.projects.frontend.impl.UserSession;
import ru.mail.projects.messages.MsgClick;

public class MsgClickTest {
	
	@Test
	public void MsgClickConstructTest() {
		
		Address addrFr = new Address();
		Address addrTo = new Address();
		
		MsgClick msgCl = new MsgClick(addrFr, addrTo, null);
		Assert.assertEquals(msgCl.getFromAddr(), addrFr);
		Assert.assertEquals(msgCl.getUsSession(), null);
	}
}
