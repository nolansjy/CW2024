package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Nested;
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
		game.setStageType(0);
		level = new EnemyLevel(game);
	}


	@Test
	void testGetEnemyHealth_BaseDifficulty() {
		int health = level.getEnemyHealth();
		assertEquals(1,health, "Base Enemy Health is 1");
	}
	

	@Test
	void testGetEnemyFireRate_BaseDifficulty() {
		double fireRate = level.getEnemyFireRate();
		assertEquals(0.01, fireRate, "Base Fire Rate is 0.01");
	}

	@Test
	void testGetTotalEnemies_BaseDifficulty() {
		int totalEnemies = level.getTotalEnemies();
		assertEquals(5, totalEnemies, "Base Enemy Total is 5");
	}

	@Test
	void testGetKillsToAdvance_BaseDifficulty() {
		int killsToAdvance = level.getKillsToAdvance();
		assertEquals(10, killsToAdvance, "Base Kills to Advance is 10");
	}

	@Test
	void testGetEnemySpawnProbability_BaseDifficulty() {
		double spawnRate = level.getEnemySpawnProbability();
		assertEquals(0.2, spawnRate, "Base Spawn Rate is 0.2");
	}

	@Test
	void testGetBackgroundString() throws IOException {
		String bg = level.getBackgroundFile(game.getStageType());
		assertEquals("enemy_bg1.jpg", bg, "Base Background is enemy_bg1.jpg");
	}
	
	@Nested
	class EnemyLevelTestDifficulty{
		@Start
		private void start(Stage stage) throws IOException {
		    game = new GameScreen(100,100);
			game.setStageType(0);
			game.raiseDifficulty();
			level = new EnemyLevel(game);
		}
		
		
		@Test
		void testGetEnemyHealth_RaisedDifficulty() {
			int health = level.getEnemyHealth();
			assertTrue(health > 1, "New Enemy Health is more than 1");
		}
		

		@Test
		void testGetEnemyFireRate_RaisedDifficulty() {
			double fireRate = level.getEnemyFireRate();
			assertTrue(fireRate > 0.01, "New Fire Rate is more than 0.01");
		}

		@Test
		void testGetTotalEnemies_RaisedDifficulty() {
			int totalEnemies = level.getTotalEnemies();
			assertTrue(totalEnemies > 5, "New Enemy Total is more than 5");
		}

		@Test
		void testGetKillsToAdvance_RaisedDifficulty() {
			int killsToAdvance = level.getKillsToAdvance();
			assertTrue(killsToAdvance > 8, "New Kills to Advance is more than 8");
		}

		@Test
		void testGetEnemySpawnProbability_RaisedDifficulty() {
			double spawnRate = level.getEnemySpawnProbability();
			assertTrue(spawnRate > 0.2, "New Spawn Rate is more than 0.2");
		}
		
	}
	
}
