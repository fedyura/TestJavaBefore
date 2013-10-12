package ru.mail.projects.account.database.impl;

import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import ru.mail.projects.base.Address;
import ru.mail.projects.base.DatabaseService;
import ru.mail.projects.base.MessageSystem;
import ru.mail.projects.base.ResourceSystem;
import ru.mail.projects.frontend.impl.FrontendImpl;
import ru.mail.projects.messages.MsgUpdateUserId;
import ru.mail.projects.resources.DbConnectionResource;
import ru.mail.projects.utils.Context;
import ru.mail.projects.utils.LongId;
import ru.mail.projects.utils.SessionId;
import ru.mail.projects.utils.ThreadHelper;
import ru.mail.projects.utils.UserId;

public class DatabaseServiceHibernate implements Runnable, DatabaseService{

	public  static int count = 0; //рефакторинг как dataserviceImpl +  не работает
	private int id = 0;
	private  String name = new String();
	private static  int TICK_TIME = 100;
	private static Logger log = Logger.getLogger (FrontendImpl.class.getName ());
	private static Address AcsAddr = new Address ();
	private MessageSystem MsgSystem;
	private ResourceSystem rsSystem;
	private DbConnectionResource dbConnInfo;
	private ServiceRegistryBuilder builder = new ServiceRegistryBuilder();
	private ServiceRegistry serviceRegistry = builder.buildServiceRegistry();
	private SessionFactory sessionFactory;
	
	private Configuration configuration = new Configuration();
	public DatabaseServiceHibernate (Context context) {
	
		MsgSystem = (MessageSystem)context.get(MessageSystem.class);
		rsSystem = (ResourceSystem)context.get(ResourceSystem.class);
		dbConnInfo = (DbConnectionResource)rsSystem.getResource("./resources/DbConnection.xml");
		
		configuration.setProperty("hibernate.connection.username", dbConnInfo.getUsername());
		configuration.setProperty("hibernate.connection.password", dbConnInfo.getPassword());
		configuration.setProperty("hibernate.connection.url", dbConnInfo.getUrl());
		configuration.setProperty("hibernate.connection.driver_class", dbConnInfo.getDriver());
		configuration.setProperty("hibernate.dialect", dbConnInfo.getHibernateDialect());
		configuration.setProperty("hibernate.show_sql", dbConnInfo.getHibernateShowSql());
		configuration.setProperty("hibernate.hbm2ddl.auto", dbConnInfo.getHibernateHbm2ddlAuto());
		
		builder.applySettings(configuration.getProperties());
		serviceRegistry = builder.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		id = count;
		setName("DatabaseService" + id);
		count++;
		
	}
	
	public void run () {
		while (true) {
			long startTime = System.currentTimeMillis();
			MsgSystem.execForAbonent(this);
			long deltaTime = System.currentTimeMillis() - startTime;
			double load = deltaTime/TICK_TIME;
			if(load < 1)
				ThreadHelper.Sleep(TICK_TIME - deltaTime, log);
		}
	}
	
	public Address getAddress () {
		return AcsAddr;
	}
	
	public void FindUser (LongId<SessionId> sessionId, String userName, Address to) {
		
		LongId<UserId> Id = null;		
		//System.out.println("i was here");
		org.hibernate.Session session  = sessionFactory.openSession();
		UsersDataSet uDataSet = (UsersDataSet)session.createCriteria
				(UsersDataSet.class).add(Restrictions.eq("Name", userName)).uniqueResult();
		//UsersDataSet uDataSet = (UsersDataSet)session.load(UsersDataSet.class, 1);
		//System.out.println("i was here");
		
		if (uDataSet != null) Id = new LongId<UserId> (uDataSet.getId());

		ThreadHelper.Sleep(1000, log);
		MsgSystem.sendMessage 
			(new MsgUpdateUserId (getAddress(), to, sessionId, Id));
	}
	
	public void AddUser (LongId<SessionId> sessionId, String userName, Address to) {
		
		LongId<UserId> Id = null;
		org.hibernate.Session session  = sessionFactory.openSession();
		UsersDataSet uDataSet = (UsersDataSet)session.createCriteria
				(UsersDataSet.class).add(Restrictions.eq("Name", userName)).uniqueResult();
		session.close();
		if (uDataSet == null){
			uDataSet = new UsersDataSet (-1, userName);
			session  = sessionFactory.openSession();
			org.hibernate.Transaction trx = session.beginTransaction();
			trx.commit();
			session.close();
		}
		
		MsgSystem.sendMessage 
			(new MsgUpdateUserId (AcsAddr, to, sessionId, Id));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
}