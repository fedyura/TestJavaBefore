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
		
	}
}
