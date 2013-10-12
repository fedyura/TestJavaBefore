package ru.mail.projects.game_mechanics_test;

import junit.framework.Assert;

import org.testng.annotations.Test;

import ru.mail.projects.game.mechanics.impl.GameState;

public class GameStateTest {
  
	@Test
	public void ConstrGameStateTest() {
		
		GameState gs = GameState.started;
		Assert.assertEquals("Game started!", gs.toString());
	}
}
