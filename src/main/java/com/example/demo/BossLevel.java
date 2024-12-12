package com.example.demo;

import java.io.IOException;

import com.example.demo.Boss.BossBuilder;
import com.fasterxml.jackson.databind.JsonNode;

import javafx.scene.Scene;

public class BossLevel extends LevelParent {
	
	private static final String BACKGROUND_IMAGE_NAME = "boss_bg1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.EnemyLevel";
	private static final int FINAL_STAGE = 3;
	private final Boss boss;
	private BossLevelView levelView;
	
	private final JsonNode bossNode;
	protected final int bossHealth;
	protected final double bossFireRate;
	protected final double bossShieldProbability;
	protected final int bossMaxShieldFrames;

	public BossLevel(final GameScreen game) throws IOException {
		super(game);
		this.bossNode = game.getBossData().get(game.getLevelStage());
		this.bossHealth = getBossHealth();
		this.bossFireRate = getBossFireRate();
		this.bossShieldProbability = getBossShieldProbability();
		this.bossMaxShieldFrames = getBossMaxShieldFrames();				
		boss = createBoss().load().build();
	}
	
	private BossBuilder createBoss() {
		return new BossBuilder(bossHealth, bossFireRate, bossShieldProbability, bossMaxShieldFrames);
	}
	
	public int getBossHealth() {
		return bossNode.path("bossHealth").asInt();
	}
	
	public int getInitialBossHealth() throws IOException {
		JsonNode node = game.getBossData().get(game.getLevelStage());
		return node.path("bossHealth").asInt();
	}

	public double getBossFireRate() {
		return bossNode.path("fireRate").asDouble();
	}

	public double getBossShieldProbability() {
		return bossNode.path("shieldProbability").asDouble();
	}

	public int getBossMaxShieldFrames() {
		return bossNode.path("maxShieldFrames").asInt();
	}

	@Override
	public String getBackgroundFile(int levelStage) throws IOException {
		JsonNode levelNode = game.getBossData().get(levelStage);
		return levelNode.path("background").asText();
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
			game.raiseStage();
			if(game.getLevelStage() > FINAL_STAGE) {
				game.winGame();
			}else {
				goToNextLevel(NEXT_LEVEL);
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
		int bossHealth = 0;
		try {
			bossHealth = getInitialBossHealth();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		levelView = new BossLevelView(game.getRoot(), playerHealth, bossHealth);
		return levelView;
	}
	
	@Override
	protected void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		levelView.updateBossHealth(boss.getHealth());
	}





}
