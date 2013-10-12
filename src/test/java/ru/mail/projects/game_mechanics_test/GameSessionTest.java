package ru.mail.projects.game_mechanics_test;

import junit.framework.Assert;

import org.testng.annotations.Test;

import ru.mail.projects.game.mechanics.impl.GameSession;

public class GameSessionTest {
	
	@Test
	public void ConstructGameSessionObjTest() {
		
		int id_game = 5;
		GameSession gs1 = new GameSession(id_game);
		Assert.assertEquals(id_game, gs1.getGameId().hashCode());
		Assert.assertEquals(0, gs1.getStartTime());
		
		GameSession gs2 = new GameSession(5);
		Assert.assertEquals(gs2.getStartTime(), 0);
		int time = 10;
		gs2.setStartTime(time);
		Assert.assertEquals(gs2.getStartTime(), 10);
		Assert.assertNotNull(gs2.getGameId());
	}
}
