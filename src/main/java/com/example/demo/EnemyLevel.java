package com.example.demo;

import java.io.IOException;

import com.example.demo.EnemyPlane.EnemyPlaneBuilder;
import com.fasterxml.jackson.databind.JsonNode;


public class EnemyLevel extends LevelParent {
	
	private static final String NEXT_LEVEL = "com.example.demo.BossLevel";

	private final JsonNode levelNode;
	private final int enemyHealth;
	private final double enemyFireRate;
	private final int totalEnemies;
	private final int killsToAdvance;
	private final double enemySpawnProbability;

	public EnemyLevel(final GameScreen game) throws IOException {
		super(game);
		this.levelNode = game.getLevelData().get(game.getLevelStage());
		this.enemyHealth = getEnemyHealth();
		this.enemyFireRate = getEnemyFireRate();
		this.totalEnemies = getTotalEnemies();
		this.killsToAdvance = getKillsToAdvance();
		this.enemySpawnProbability = getEnemySpawnProbability();
	}
	
	@Override
	protected String getBackgroundFile(int levelStage) throws IOException {
		JsonNode levelNode = game.getLevelData().get(levelStage);
		return levelNode.path("background").asText();
	}
		

	public int getEnemyHealth() {
		return levelNode.path("enemyHealth").asInt();
	}


	public double getEnemyFireRate() {
		return levelNode.path("fireRate").asDouble();
	}


	public int getTotalEnemies() {
		return levelNode.path("totalEnemies").asInt();
	}


	public int getKillsToAdvance() {
		return levelNode.path("killsToAdvance").asInt();
	}


	public double getEnemySpawnProbability() {
		return levelNode.path("spawnRate").asDouble();

	}


	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			game.loseGame();
		}
		else if (userHasReachedKillTarget()) {
			goToNextLevel(NEXT_LEVEL);
		}
	}

	@Override
	protected void initializeFriendlyUnits() {
		game.getRoot().getChildren().add(getUser());
	}

	@Override
	protected void spawnEnemyUnits() {
		int currentNumberOfEnemies = getCurrentNumberOfEnemies();
		for (int i = 0; i < totalEnemies - currentNumberOfEnemies; i++) {
			if (Math.random() < enemySpawnProbability) {
				double newEnemyInitialYPosition = Math.random() * game.getEnemyMaximumYPosition();
				SpriteDestructible newEnemy = getEnemyPlane()
								.setImagePos(game.getScreenWidth(), newEnemyInitialYPosition)
								.load().build();
				addEnemyUnit(newEnemy);
			}
		}
	}
	
	private EnemyPlaneBuilder getEnemyPlane() {
		return new EnemyPlaneBuilder(enemyHealth, enemyFireRate);		
	}

	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(game.getRoot(), playerHealth);
	}

	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= killsToAdvance;
	}






	

}
