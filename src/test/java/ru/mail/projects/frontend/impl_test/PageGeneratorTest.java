package ru.mail.projects.frontend.impl_test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import ru.mail.projects.frontend.impl.PageGeneratorImpl;
import ru.mail.projects.frontend.impl.UserSession;
import ru.mail.projects.utils.LongId;
import ru.mail.projects.utils.SessionId;
import ru.mail.projects.utils.UserId;

public class PageGeneratorTest {
	
	Request mockedBaseRequest;
	HttpServletRequest mockedHttpRequestAutWas;
	HttpServletResponse mockedResponse;
	PageGeneratorImpl pg;
	
	@BeforeTest
	public void prepareObjects() {
		
		mockedBaseRequest = mock(Request.class);
		mockedHttpRequestAutWas = mock(HttpServletRequest.class);
		mockedResponse = mock(HttpServletResponse.class);
		
		OutputStream os = System.out;
		PrintWriter pr = new PrintWriter(os);
		try { when(mockedResponse.getWriter()).thenReturn(pr); }
		catch (Exception e) { System.out.println("Error in assigning writer"); }
		
		pg = new PageGeneratorImpl();
	}
	
	@Test(timeOut = 1000)
	public void generateResponsePageTest() {
		
		LongId<SessionId> sessionId1 = new LongId<SessionId>(10);
		UserSession mockedUsSess = mock(UserSession.class);
		mockedUsSess.sessionId = sessionId1;
		
		String Login = new String("Yura");
		LongId<UserId> userId_test = new LongId<UserId>(-1);
		LongId<UserId> userIdNotNull = new LongId<UserId>(5);
		
		try
		{
			pg.generateResponsePage(mockedUsSess, mockedResponse, mockedBaseRequest, Login);
			mockedUsSess.userId = userId_test;
			
			pg.generateResponsePage(mockedUsSess, mockedResponse, mockedBaseRequest, Login);
			mockedUsSess.userId = userIdNotNull;
			pg.generateResponsePage(mockedUsSess, mockedResponse, mockedBaseRequest, Login);
		}
		catch (Exception e)
		{
		}
	}
	
	@Test(timeOut = 1000)
	public void generateResponseRegistrTest() {
		
		LongId<SessionId> sessionId1 = new LongId<SessionId>(10);
		UserSession mockedUsSess = mock(UserSession.class);
		mockedUsSess.userId = null;
		mockedUsSess.sessionId = sessionId1;
		
		String Login = new String("Yura");
		LongId<UserId> userId_test = new LongId<UserId>(-1);
		LongId<UserId> userIdNotNull = new LongId<UserId>(5);
		mockedUsSess.userName = new String("F_yura");
		
		try
		{
			pg.generateResponseRegistrPage(mockedUsSess, mockedResponse, mockedBaseRequest, Login, "1");
			mockedUsSess.userId = userId_test;
			
			pg.generateResponseRegistrPage(mockedUsSess, mockedResponse, mockedBaseRequest, Login, "1");
			mockedUsSess.userId = userIdNotNull;
			pg.generateResponseRegistrPage(mockedUsSess, mockedResponse, mockedBaseRequest, Login, "1");
		}
		catch (Exception e)
		{
		}
	}
	
	@Test(timeOut = 1000)
	public void generateGamePageTest() {
		
		LongId<SessionId> sessionIdTest = new LongId<SessionId>(10);
		LongId<UserId> userIdNotNull = new LongId<UserId>(5);
		
		UserSession mockedUsSess = mock(UserSession.class);
		mockedUsSess.sessionId = sessionIdTest;
		mockedUsSess.timeToFinish = 5;
		mockedUsSess.userId = userIdNotNull;
		
		int clickUser = 7;
		try
		{
			pg.generateGamePage(null, mockedResponse, mockedBaseRequest);
			
			pg.generateGamePage(mockedUsSess, mockedResponse, mockedBaseRequest);
			
			mockedUsSess.clickByUser = clickUser;
			mockedUsSess.clickedByEnemy = clickUser - 1;
			mockedUsSess.timeToFinish = 0;
			pg.generateGamePage(mockedUsSess, mockedResponse, mockedBaseRequest);
			
			mockedUsSess.clickedByEnemy = clickUser + 1;
			pg.generateGamePage(mockedUsSess, mockedResponse, mockedBaseRequest);
			
			mockedUsSess.clickedByEnemy = clickUser;
			pg.generateGamePage(mockedUsSess, mockedResponse, mockedBaseRequest);
		}
		catch (Exception e)
		{
		}
	}
}
