package com.example.demo;

import java.io.IOException;

import com.example.demo.EnemyPlane.EnemyPlaneBuilder;
import com.fasterxml.jackson.databind.JsonNode;


/**
 * Enemy Level stage where enemies spawn and a kill target needs to be reached before facing BossLevel
 */
public class EnemyLevel extends LevelParent {
	
	private static final String NEXT_LEVEL = "com.example.demo.BossLevel";

	private final JsonNode levelNode;
	private final int enemyHealth;
	private final double enemyFireRate;
	private final int totalEnemies;
	private final int killsToAdvance;
	private final double enemySpawnProbability;

	/**
	 * Runs getters which read from the enemyLevel.json file to set the enemy details of this level
	 * @param game initial GameScreen instance
	 * @throws IOException
	 */
	public EnemyLevel(final GameScreen game) throws IOException {
		super(game);
		this.levelNode = game.getLevelData().get(game.getLevelStage());
		this.enemyHealth = getEnemyHealth();
		this.enemyFireRate = getEnemyFireRate();
		this.totalEnemies = getTotalEnemies();
		this.killsToAdvance = getKillsToAdvance();
		this.enemySpawnProbability = getEnemySpawnProbability();
	}
	
	/**
	 * Reads background from enemyLevel.json
	 */
	@Override
	protected String getBackgroundFile(int levelStage) throws IOException {
		JsonNode levelNode = game.getLevelData().get(levelStage);
		return levelNode.path("background").asText();
	}
		

	/**
	 * Gets health of EnemyPlane as set in enemyLevel.json configuration
	 * @return health of each EnemyPlane
	 */
	public int getEnemyHealth() {
		return levelNode.path("enemyHealth").asInt();
	}


	/**
	 * Gets fireRate of EnemyPlane as set in enemyLevel.json configuration
	 * @return fire rate of each EnemyPlane
	 */
	public double getEnemyFireRate() {
		return levelNode.path("fireRate").asDouble();
	}


	/**
	 * Gets total EnemyPlanes as set in enemyLevel.json configuration
	 * @return total enemies spawned at once in a level
	 */
	public int getTotalEnemies() {
		return levelNode.path("totalEnemies").asInt();
	}


	/**
	 * Gets kill target as set in enemyLevel.json configuration
	 * @return kill target
	 */
	public int getKillsToAdvance() {
		return levelNode.path("killsToAdvance").asInt();
	}


	/**
	 * Gets enemy spawn rate as set in enemyLevel.json configuration
	 * @return probability of enemy spawning
	 */
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

	/**
	 * Changed to use the new SpriteDestructible builder to create the EnemyPlane object
	 */
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
