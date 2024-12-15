package com.example.demo;

import java.io.IOException;

import com.example.demo.BossPlane.BossBuilder;
import com.fasterxml.jackson.databind.JsonNode;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.text.TextAlignment;

/**
 * BossLevel spawns one boss and one player. The levelStage is raised after defeating a boss.
 */
public class BossLevel extends LevelParent {
	
	private static final String ENEMY_LEVEL = "com.example.demo.EnemyLevel";
	private final BossPlane boss;
	private BossLevelView levelView;
	
	private final JsonNode bossNode;
	private final int difficulty;
	
	private final int bossHealth;
	private final double bossFireRate;
	private final double bossShieldProbability;
	private final int bossMaxShieldFrames;

	/**
	 * Reads from bossLevel.json and sets level field configuration, then creates Boss
	 * @param game GameScreen instance
	 * @throws IOException While reading from bossLevel.json
	 */
	public BossLevel(final GameScreen game) throws IOException {
		super(game);
		this.bossNode = game.getBossData().get(game.getStageType());
		this.difficulty = game.getDifficulty();
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
	 * Reads bossLevel.json and gets health set in the current levelStage configuration
	 * @return boss health by level
	 */
	public int getBossHealth() {
		int health = bossNode.path("bossHealth").asInt();
		return health+50*difficulty;
	}
	
	/**
	 * Same as getBossHealth(), but used by instantiateLevelView() first to create the healthbar display.
	 * It is not reliant on BossLevel being constructed
	 * @return boss health as defined in bossLevel.json
	 * @throws IOException
	 */
	public int getInitialBossHealth() throws IOException {
		JsonNode node = game.getBossData().get(game.getStageType());
		int health = node.path("bossHealth").asInt();
		return health+50*difficulty;
	}

	/**
	 * Reads bossLevel.json and gets fireRate set in the current levelStage configuration
	 * @return fireRate of BossPlane
	 */
	public double getBossFireRate() {
		double fireRate = bossNode.path("fireRate").asDouble();
		return fireRate + 0.005*difficulty;
	}

	/**
	 * Reads bossLevel.json and gets shieldProbability set in the current levelStage configuration
	 * @return shieldProbability
	 */
	public double getBossShieldProbability() {
		double shieldRate = bossNode.path("shieldProbability").asDouble();
		return shieldRate + 0.001*difficulty;
	}

	/**
	 * Reads bossLevel.json and gets maxShieldFrames set in the current levelStage configuration
	 * @return maxShieldFrames
	 */
	public int getBossMaxShieldFrames() {
		int shieldFrames = bossNode.path("maxShieldFrames").asInt();
		return shieldFrames + 50*difficulty;
	}

	/**
	 * Reads current stage's background from bossLevel.json
	 */
	@Override
	public String getBackgroundFile(int stageType) throws IOException {
		JsonNode levelNode = game.getBossData().get(stageType);
		return levelNode.path("background").asText();
	}
	
	@Override
	protected String getLevelAlert(int stageType) throws IOException{
		JsonNode levelNode = game.getBossData().get(stageType);
		return levelNode.path("alert").asText();
	}
	
	/**
	 * Adds boss healthbar to the LevelView
	 * @throws IOException 
	 */
	@Override
	public Scene initializeScene() throws IOException {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		levelView.showBossHealth();
		if(difficulty == 0) game.addAlert(getLevelAlert(stageType));
		return scene;
	}

	@Override
	protected void initializeFriendlyUnits() {
		game.getRoot().getChildren().add(getUser());
	}
	

	/**
	 * When boss is defeated, raise stage level. If final stage is reached, prompt to go to next round or stop game.
	 */
	@Override
	protected void checkIfGameOver() {
		if (userIsDestroyed()) {
			game.loseGame();
		}
		else if (boss.isDestroyed()) {
			if(stageType < 3) {
				game.nextStageType();
				goToNextLevel(ENEMY_LEVEL);
			}else {
				promptNextRound();
			}
		}
	}
	
	/**
	 * Creates a dialog to go to the next round or exit the game after three stages have been cleared.
	 */
	protected void promptNextRound() {
		game.getTimeline().stop();
		Label prompt = new Label();
		prompt.getStyleClass().add("alert-bg");
		prompt.setTextAlignment(TextAlignment.CENTER);
		prompt.setText("Proceed To Next Round? \n (ENTER for YES, ESC for NO)");
		prompt.setLayoutX(400);
		prompt.setLayoutY(350);		
		prompt.setOnKeyPressed(event -> {
			KeyCode kc = event.getCode();
			if(kc == KeyCode.ESCAPE) {
				prompt.setVisible(false);
				game.winGame();
			}
			if(kc == KeyCode.ENTER) {
				game.setStageType(1);
				game.raiseDifficulty();
				goToNextLevel(ENEMY_LEVEL);
			}
		});		
		game.getRoot().getChildren().add(prompt);
		prompt.requestFocus();

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
