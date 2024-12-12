package com.example.demo;

import com.example.demo.Boss.BossBuilder;

import javafx.scene.Scene;

public class BossLevel extends LevelParent {
	
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.EnemyLevel";
	private static final int FINAL_LEVEL_DIFFICULTY = 5;
	private final Boss boss;
	private BossLevelView levelView;
	
	protected final int bossHealth;
	protected final double bossFireRate;
	protected final double bossShieldProbability;
	protected final int bossMaxShieldFrames;

	public BossLevel(final GameScreen game) {
		super(game, BACKGROUND_IMAGE_NAME);
		this.bossHealth = 50*(difficulty+1);
		this.bossFireRate = difficulty/100.0*2.0;
		this.bossShieldProbability = difficulty/100.0;
		this.bossMaxShieldFrames = 50*(difficulty+1);				
		boss = createBoss().load().build();
	}
	
	private BossBuilder createBoss() {
		return new BossBuilder(bossHealth, bossFireRate, bossShieldProbability, bossMaxShieldFrames);
	}
	
	@Override
	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		levelView.showBossHealth();
		return scene;
	}

	@Override
	protected void initializeFriendlyUnits() {
		game.getRoot().getChildren().add(getUser());
	}

	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			game.loseGame();
		}
		else if (boss.isDestroyed()) {
			game.raiseDifficulty();
			goToNextLevel(NEXT_LEVEL);
			if(game.getDifficulty() > FINAL_LEVEL_DIFFICULTY) {
				game.winGame();
			}
		}
	}

	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
		}
	}

	@Override
	protected LevelView instantiateLevelView() {
		int bossHealth = 50*(difficulty+1);
		levelView = new BossLevelView(game.getRoot(), playerHealth, bossHealth);
		return levelView;
	}
	
	@Override
	protected void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		levelView.updateBossHealth(boss.getHealth());
	}

}
