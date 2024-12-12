package com.example.demo;

import com.example.demo.EnemyPlane.EnemyPlaneBuilder;

public class EnemyLevel extends LevelParent {
	
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.BossLevel";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 10;
	private static final double ENEMY_SPAWN_PROBABILITY = .20;
	
	private final int enemyHealth;
	private final double enemyFireRate;
	private final int totalEnemies;
	private final int killsToAdvance;
	private final double enemySpawnProbability;

	public EnemyLevel(final GameScreen game) {
		super(game, BACKGROUND_IMAGE_NAME);
		this.enemyHealth = Math.round(0.5f*difficulty);
		this.enemyFireRate = difficulty/100.0;
		this.totalEnemies = 2*difficulty+1;
		this.killsToAdvance = 2*(difficulty+1);
		this.enemySpawnProbability = 0.05*(difficulty+1);
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
