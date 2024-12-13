package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class EnemyLevelTest {
	
	static EnemyLevel level;
	static GameScreen game;
	
	@Start
	private void start(Stage stage) throws IOException {
	    game = new GameScreen(100,100);
	    game.setStage(0);
		level = new EnemyLevel(game);
	}


	@Test
	void testGetEnemyHealth() {
		int health = level.getEnemyHealth();
		assertEquals(1,health, "Base Enemy Health is 1");
	}

	@Test
	void testGetEnemyFireRate() {
		double fireRate = level.getEnemyFireRate();
		assertEquals(0.01, fireRate, "Base Fire Rate is 0.01");
	}

	@Test
	void testGetTotalEnemies() {
		int totalEnemies = level.getTotalEnemies();
		assertEquals(5, totalEnemies, "Base Enemy Total is 5");
	}

	@Test
	void testGetKillsToAdvance() {
		int killsToAdvance = level.getKillsToAdvance();
		assertEquals(10, killsToAdvance, "Base Kills to Advance is 10");
	}

	@Test
	void testGetEnemySpawnProbability() {
		double spawnRate = level.getEnemySpawnProbability();
		assertEquals(0.2, spawnRate, "Base Spawn Rate is 0.2");
	}

	@Test
	void testGetBackgroundString() throws IOException {
		String bg = level.getBackgroundFile(game.getLevelStage());
		assertEquals("enemy_bg1.jpg", bg, "Base Background is enemy_bg1");
	}

}
