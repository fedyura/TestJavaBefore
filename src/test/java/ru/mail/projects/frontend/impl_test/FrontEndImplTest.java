package ru.mail.projects.frontend.impl_test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.eclipse.jetty.server.Request;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.mail.projects.base.AddressService;
import ru.mail.projects.base.MessageSystem;
import ru.mail.projects.base.ResourceSystem;
import ru.mail.projects.frontend.impl.FrontendImpl;
import ru.mail.projects.frontend.impl.UserSession;
import ru.mail.projects.utils.Context;
import ru.mail.projects.utils.LongId;
import ru.mail.projects.utils.SessionId;
import ru.mail.projects.utils.UserId;

public class FrontEndImplTest {
  
	Context mockedContext;
		
	@BeforeClass
	public void PrepareObjects() {
		
		mockedContext = mock(Context.class);
		//VFS mockedVfs = mock(VFS.class);
		ResourceSystem mockedRsSystem = mock(ResourceSystem.class);
		MessageSystem mockedMsgSystem = mock(MessageSystem.class);
		
		AddressService mockedAddressService = mock(AddressService.class);
		when(mockedMsgSystem.getAddressService()).thenReturn(mockedAddressService);
		
		when(mockedContext.get(ResourceSystem.class)).thenReturn(mockedRsSystem);
		when(mockedContext.get(MessageSystem.class)).thenReturn(mockedMsgSystem);
		
		/*context = new Context();
		VFS vfs = new VFSImpl("./resources");
    	MessageSystem MsgSys = new MessageSystemImpl();

    	context.add(VFS.class, vfs);
    	context.add(MessageSystem.class, MsgSys);
    	ResourceSystem rsSystem = new ResourceSystemImpl(vfs);
    	rsSystem.loadResources();
    	context.add(ResourceSystem.class, rsSystem);*/
	}
	
	@Test
	public void FrontEndImplConstruct() {
		
		FrontendImpl frEnd1 = new FrontendImpl(mockedContext);
		FrontendImpl frEnd2 = new FrontendImpl(mockedContext);
		
		Assert.assertEquals("Frontend1", frEnd1.getName()); 
		Assert.assertEquals("Frontend2", frEnd2.getName()); 
		
		Assert.assertEquals(frEnd1.getAddress(), frEnd2.getAddress());
	}
	
	@Test(dependsOnMethods = { "FrontEndImplConstruct" })
	public void updateUserIdTest() {
		
		FrontendImpl frEnd1 = new FrontendImpl(mockedContext);
		int sessNumber = 10;
		UserSession mockedUsSess = mock(UserSession.class);
		frEnd1.sessions.put(new Integer(sessNumber), mockedUsSess);
		
		LongId<SessionId> sessionId = new LongId<SessionId>(sessNumber);
		LongId<UserId> userId = new LongId<UserId>(3);
		
		frEnd1.updateUserId(sessionId, userId);
		Assert.assertEquals(userId, frEnd1.sessions.get(sessionId.getLong()).userId);
		
		LongId<SessionId> sessionId1 = new LongId<SessionId>(sessNumber);
		LongId<UserId> userId1 = null;
		
		frEnd1.updateUserId(sessionId1, userId1);
		Assert.assertEquals(new Integer(-1), frEnd1.sessions.get(sessionId1.getLong()).userId.getLong());
		
		LongId<SessionId> sessionId2 = new LongId<SessionId>(sessNumber+2);
		frEnd1.updateUserId(sessionId2, userId1);
		Assert.assertEquals(null, frEnd1.sessions.get(sessionId2.getLong()));
		
		frEnd1.replicateFromGM();
	}
	
	@Test(dependsOnMethods = { "FrontEndImplConstruct", "updateUserIdTest" })
	public void showStartGameTest() {
		
		FrontendImpl frEnd1 = new FrontendImpl(mockedContext);
		
		UserSession mockedSession1 = mock(UserSession.class);
		UserSession mockedSession2 = mock(UserSession.class);
		
		mockedSession1.userName = "Yura";
		mockedSession2.userName = "Roma";
		int idGameSession = 7;
		int wUs = FrontendImpl.waitUsersNumber.get();
		frEnd1.showStartedGame(mockedSession1, mockedSession2, idGameSession);
		
		Assert.assertEquals(true, mockedSession1.game_state);
		Assert.assertEquals(true, mockedSession2.game_state);
		Assert.assertEquals(false, mockedSession1.is_wait);
		Assert.assertEquals(false, mockedSession2.is_wait);
		
		Assert.assertEquals(mockedSession2.userName, mockedSession1.enemyName);
		Assert.assertEquals(mockedSession1.userName, mockedSession2.enemyName);
		Assert.assertEquals(mockedSession1.IdGameSession, idGameSession);
		Assert.assertEquals(FrontendImpl.waitUsersNumber.get(), wUs - 2);
	}
	
	@Test(dependsOnMethods = { "FrontEndImplConstruct", "updateUserIdTest" })
	public void doAuthentificationTest() {
		
		int sessNumber = 10;
		FrontendImpl frEnd = new FrontendImpl(mockedContext);
		LongId<SessionId> sessionId1 = new LongId<SessionId>(sessNumber);
		
		UserSession mockedUsSess = mock(UserSession.class);
		mockedUsSess.userId = null;
		mockedUsSess.sessionId = sessionId1;
		
		frEnd.sessions.put(new Integer(sessNumber), mockedUsSess);
		
		Request mockedBaseRequest = mock(Request.class);
		
		HttpServletRequest mockedHttpRequestAutWas = mock(HttpServletRequest.class);
		when(mockedHttpRequestAutWas.getParameter("StGame")).thenReturn("1");
		
		HttpServletRequest mockedHttpRequestRegNotNull = mock(HttpServletRequest.class);
		when(mockedHttpRequestRegNotNull.getParameter("Reg")).thenReturn("1");
		
		HttpServletRequest mockedHttpRequestLoginNotNull = mock(HttpServletRequest.class);
		when(mockedHttpRequestLoginNotNull.getParameter("login")).thenReturn("Yura");
		when(mockedHttpRequestLoginNotNull.getParameter("id")).thenReturn(new Integer(sessNumber).toString());
		
		HttpServletRequest mockedHttpRequestRegLoginNotNull = mock(HttpServletRequest.class);
		when(mockedHttpRequestRegLoginNotNull.getParameter("Reglogin")).thenReturn("0");
		when(mockedHttpRequestRegLoginNotNull.getParameter("id")).thenReturn(new Integer(sessNumber).toString());
		
		HttpServletRequest mockedHttpRequestAutNo = mock(HttpServletRequest.class);
		
		HttpServletResponse mockedResponse = mock(HttpServletResponse.class);
		
		OutputStream os = System.out;
		PrintWriter pr = new PrintWriter(os);
		try { when(mockedResponse.getWriter()).thenReturn(pr); }
		catch (Exception e) { System.out.println("Error in assigning writer"); }
		
		try 
		{
			FrontendImpl.AuthorState as = frEnd.doAuthentification(mockedBaseRequest, mockedHttpRequestAutWas, mockedResponse);
			Assert.assertEquals(FrontendImpl.AuthorState.End, as);
			
			FrontendImpl.AuthorState asNo = frEnd.doAuthentification(mockedBaseRequest, mockedHttpRequestAutNo, mockedResponse);
			Assert.assertEquals(FrontendImpl.AuthorState.NotEnd, asNo);
			
			asNo = frEnd.doAuthentification(mockedBaseRequest, mockedHttpRequestRegNotNull, mockedResponse);
			Assert.assertEquals(FrontendImpl.AuthorState.NotEnd, asNo);
			
			asNo = frEnd.doAuthentification(mockedBaseRequest, mockedHttpRequestLoginNotNull, mockedResponse);
			Assert.assertEquals(FrontendImpl.AuthorState.NotEnd, asNo);
			
			asNo = frEnd.doAuthentification(mockedBaseRequest, mockedHttpRequestRegLoginNotNull, mockedResponse);
			Assert.assertEquals(FrontendImpl.AuthorState.NotEnd, asNo);
		}
		catch (Exception e)
		{
		}
	}
	
	@Test(dependsOnMethods = { "FrontEndImplConstruct", "updateUserIdTest" })
	public void startGameTest() {
		
		int sessNumber = 10;
		FrontendImpl frEnd = new FrontendImpl(mockedContext);
		LongId<SessionId> sessionId1 = new LongId<SessionId>(sessNumber);
		
		UserSession mockedUsSess = mock(UserSession.class);
		mockedUsSess.userId = null;
		mockedUsSess.sessionId = sessionId1;
		mockedUsSess.is_wait = false;
		
		frEnd.sessions.put(new Integer(sessNumber), mockedUsSess);
		
		Request mockedBaseRequest = mock(Request.class);
		HttpServletRequest mockedHttpRequestPlayGameNotNull = mock(HttpServletRequest.class);
		when(mockedHttpRequestPlayGameNotNull.getParameter("Id")).thenReturn(new Integer(sessNumber).toString());
		when(mockedHttpRequestPlayGameNotNull.getParameter("PlayGame")).thenReturn("1");
		
		HttpServletRequest mockedHttpRequestPlayGameSec = mock(HttpServletRequest.class);
		when(mockedHttpRequestPlayGameSec.getParameter("Id")).thenReturn(new Integer(sessNumber).toString());
		when(mockedHttpRequestPlayGameSec.getParameter("StGame")).thenReturn("2");
		
		HttpServletRequest mockedHttpRequestIsGame1 = mock(HttpServletRequest.class);
		when(mockedHttpRequestIsGame1.getParameter("Id")).thenReturn(new Integer(sessNumber).toString());
		when(mockedHttpRequestIsGame1.getParameter("StGame")).thenReturn("1");
		
		HttpServletResponse mockedResponse = mock(HttpServletResponse.class);
		OutputStream os = System.out;
		PrintWriter pr = new PrintWriter(os);
		try { when(mockedResponse.getWriter()).thenReturn(pr); }
		catch (Exception e) { System.out.println("Error in assigning writer"); }
		
		try 
		{
			frEnd.startGame(mockedBaseRequest, mockedHttpRequestPlayGameNotNull, mockedResponse);
			
			int waitUsNumber = FrontendImpl.waitUsersNumber.get();
			frEnd.startGame(mockedBaseRequest, mockedHttpRequestIsGame1, mockedResponse);
			
			Assert.assertEquals(waitUsNumber + 1, FrontendImpl.waitUsersNumber.get());
			Assert.assertEquals(true, mockedUsSess.is_wait);
			Assert.assertEquals(0, mockedUsSess.clickByUser);
			Assert.assertEquals(0, mockedUsSess.clickedByEnemy);
			
			mockedUsSess.is_wait = false;
			frEnd.startGame(mockedBaseRequest, mockedHttpRequestIsGame1, mockedResponse);
			mockedUsSess.is_wait = false;
			frEnd.startGame(mockedBaseRequest, mockedHttpRequestIsGame1, mockedResponse);
			Assert.assertEquals(new Integer(sessNumber), frEnd.getWaitSess().getLong());
			mockedUsSess.is_wait = false;
			frEnd.startGame(mockedBaseRequest, mockedHttpRequestIsGame1, mockedResponse);
			frEnd.startGame(mockedBaseRequest, mockedHttpRequestIsGame1, mockedResponse);
			
			frEnd.startGame(mockedBaseRequest, mockedHttpRequestPlayGameSec, mockedResponse);
			mockedUsSess.game_state = true;
			frEnd.startGame(mockedBaseRequest, mockedHttpRequestPlayGameSec, mockedResponse);
		}
		catch (Exception e)
		{
		}
	}
	
	@Test(dependsOnMethods = { "FrontEndImplConstruct", "updateUserIdTest" })
	public void handleTest() {
		
		int sessNumber = 10;
		FrontendImpl frEnd = new FrontendImpl(mockedContext);
		LongId<SessionId> sessionId1 = new LongId<SessionId>(sessNumber);
		
		UserSession mockedUsSess = mock(UserSession.class);
		mockedUsSess.userId = null;
		mockedUsSess.sessionId = sessionId1;
		
		frEnd.sessions.put(new Integer(sessNumber), mockedUsSess);
		
		Request mockedBaseRequest = mock(Request.class);
		HttpServletRequest mockedHttpRequestAutNo = mock(HttpServletRequest.class);
		
		HttpServletRequest mockedHttpRequestIsGame1 = mock(HttpServletRequest.class);
		when(mockedHttpRequestIsGame1.getParameter("Id")).thenReturn(new Integer(sessNumber).toString());
		when(mockedHttpRequestIsGame1.getParameter("StGame")).thenReturn("1");
		
		HttpServletResponse mockedResponse = mock(HttpServletResponse.class);
		
		OutputStream os = System.out;
		PrintWriter pr = new PrintWriter(os);
		try { when(mockedResponse.getWriter()).thenReturn(pr); }
		catch (Exception e) { System.out.println("Error in assigning writer"); }
		
		try 
		{
			frEnd.handle("", mockedBaseRequest, mockedHttpRequestAutNo, mockedResponse);
			
			int waitUsNumber = FrontendImpl.waitUsersNumber.get();
			frEnd.handle("", mockedBaseRequest, mockedHttpRequestIsGame1, mockedResponse);
			
			Assert.assertEquals(waitUsNumber + 1, FrontendImpl.waitUsersNumber.get());
		}
		catch (Exception e) 
		{
		}
	}
	
	
}
