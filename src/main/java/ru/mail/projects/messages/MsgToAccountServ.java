package ru.mail.projects.messages;

import ru.mail.projects.account.database.impl.DatabaseServiceHibernate;
import ru.mail.projects.base.Abonent;
import ru.mail.projects.base.Address;
import ru.mail.projects.base.DatabaseService;
import ru.mail.projects.base.Msg;

public abstract class MsgToAccountServ extends Msg {
	
	public MsgToAccountServ (Address from, Address to) {
		super (from, to);
	}
	
	public void exec (Abonent abonent) {
		if (abonent instanceof DatabaseServiceHibernate) exec ((DatabaseServiceHibernate)abonent);
	}
	
	public abstract void exec (DatabaseService accser);
}
