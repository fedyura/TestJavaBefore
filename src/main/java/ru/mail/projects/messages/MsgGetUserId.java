package ru.mail.projects.messages;

import ru.mail.projects.base.Address;
import ru.mail.projects.base.DatabaseService;
import ru.mail.projects.utils.LongId;
import ru.mail.projects.utils.SessionId;

public class MsgGetUserId extends MsgToAccountServ {
	
	final private LongId<SessionId> sessionId;
	final private String userName;
	final private Address from;
	
	public MsgGetUserId (Address from, Address to, LongId<SessionId> sessionId, String userName) {
		
		super (from, to);
		this.sessionId = sessionId;
		this.userName = userName;
		this.from = from;
	}
	
	public void exec(DatabaseService accserv) {
		accserv.FindUser(sessionId, userName, from);
	}
	
	public String getName() {
		
		return userName;
	}
	
	public LongId<SessionId> getSession() {
		
		return sessionId;
	}
	
	public Address getAddress() {
		
		return from;
	}

}
