package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class BossLevelTest {

	static GameScreen game;
	static BossLevel level;
	@Start
	private void start(Stage stage) throws IOException {
	    game = new GameScreen(100,100);
	    game.setStage(0);
		level = new BossLevel(game);
	}
	
	@Test
	void testGetBackgroundFile() throws IOException {
		String bossBG = level.getBackgroundFile(game.getLevelStage());
		assertEquals("boss_bg1.jpg", bossBG, "Base Background is boss_bg1");
	}

	@Test
	void testGetBossHealth() {
		int health = level.getBossHealth();
		assertEquals(100, health, "Base boss health is 100");
	}

	@Test
	void testGetBossFireRate() {
		double fireRate = level.getBossFireRate();
		assertEquals(0.04, fireRate, "Base Fire Rate is 0.04");
	}

	@Test
	void testGetBossShieldProbability() {
		double shieldRate = level.getBossShieldProbability();
		assertEquals(0.002, shieldRate, "Base shield probability is 0.002");
	}

	@Test
	void testGetBossMaxShieldFrames() {
		int maxShieldFrames = level.getBossMaxShieldFrames();
		assertEquals(500, maxShieldFrames);
	}

}
