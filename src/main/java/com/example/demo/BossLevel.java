package com.example.demo;

import java.io.IOException;

import com.example.demo.BossPlane.BossBuilder;
import com.fasterxml.jackson.databind.JsonNode;

import javafx.scene.Scene;

/**
 * BossLevel spawns one boss and one player. The levelStage is raised after defeating a boss.
 */
public class BossLevel extends LevelParent {
	
	private static final String NEXT_LEVEL = "com.example.demo.EnemyLevel";
	private static final int FINAL_STAGE = 3;
	private final BossPlane boss;
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
	
	/**
	 * @return boss health as defined in bossLevel.json
	 */
	public int getBossHealth() {
		return bossNode.path("bossHealth").asInt();
	}
	
	/**
	 * Same as getBossHealth(), but used by instantiateLevelView() first to create the healthbar display.
	 * It is not reliant on BossLevel being constructed
	 * @return boss health as defined in bossLevel.json
	 * @throws IOException
	 */
	public int getInitialBossHealth() throws IOException {
		JsonNode node = game.getBossData().get(game.getLevelStage());
		return node.path("bossHealth").asInt();
	}

	/**
	 * @return fireRate of BossPlane as defined in bossLevel.json
	 */
	public double getBossFireRate() {
		return bossNode.path("fireRate").asDouble();
	}

	/**
	 * @return shieldProbability as defined in bossLevel.json
	 */
	public double getBossShieldProbability() {
		return bossNode.path("shieldProbability").asDouble();
	}

	/**
	 * @return maxShieldFrames as defined in bossLevel.json
	 */
	public int getBossMaxShieldFrames() {
		return bossNode.path("maxShieldFrames").asInt();
	}

	/**
	 * Reads current stage's background from bossLevel.json
	 */
	@Override
	public String getBackgroundFile(int levelStage) throws IOException {
		JsonNode levelNode = game.getBossData().get(levelStage);
		return levelNode.path("background").asText();
	}
	
	/**
	 * Adds boss healthbar to the LevelView
	 */
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

	/**
	 * When boss is defeated, raise stage level. If final stage is reached, wins the game.
	 */
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

	/**
	 *  Adds boss healthbar to levelView
	 */
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
	
	/**
	 * Updates boss healthbar display
	 */
	@Override
	protected void updateLevelView() {
		levelView.removeHearts(user.getHealth());
		levelView.updateBossHealth(boss.getHealth());
	}





}
