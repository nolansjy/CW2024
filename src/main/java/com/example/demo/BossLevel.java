package com.example.demo;

import javafx.scene.Scene;

public class LevelTwo extends LevelParent {
	
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final int BOSS_HEALTH = 100;
	private final Boss boss;
	private LevelViewLevelTwo levelView;

	public LevelTwo(final GameScreen game) {
		super(game, BACKGROUND_IMAGE_NAME,  PLAYER_INITIAL_HEALTH);
		boss = new Boss.BossBuilder().setHealth(BOSS_HEALTH).load().build();
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
			game.winGame();
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
		levelView = new LevelViewLevelTwo(game.getRoot(), PLAYER_INITIAL_HEALTH, BOSS_HEALTH);
		return levelView;
	}
	
	@Override
	protected void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		levelView.updateBossHealth(boss.getHealth());
	}

}
