package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;


/**
 * The GameScreen represents the game instance shared between all levels.<br>
 * It is responsible for initializing level timelines and provides access to the root/scene node so levels 
 * can add objects to it. It can start/stop the timeline to show win/lose screens.<br>
 * It also provides access to the game data JSONs so the files do not have to be read again by each individual level.
 */
public class GameScreen {
	
	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	
	private final Group root;
	private Timeline timeline;
	private final Scene scene;	
	private ImageView background;
	
	private static final String DEFAULT_BACKGROUND = "/com/example/demo/images/enemy_bg1.jpg";
	private static final String BOSS_LEVEL_FILE = "/com/example/demo/data/bossLevel.json";
	private static final String LEVEL_DATA_FILE = "/com/example/demo/data/enemyLevel.json";	
	
	private final double enemyMaximumYPosition;
	private final ImageView winImage;
	private final ImageView gameOverImage;
	private LevelParent currentLevel;
	private int levelStage;
	private final ObjectMapper mapper;
	private final File enemyLevelFile;
	private final File bossLevelFile;


	/**
	 * Constructed by the Controller
	 * @param screenHeight used to create Scene
	 * @param screenWidth used to create Scene
	 */
	public GameScreen(double screenHeight, double screenWidth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.background = new ImageView(new Image(getClass().getResourceAsStream(DEFAULT_BACKGROUND)));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.winImage = loadWinImage();
		this.gameOverImage = loadLoseImage();
		this.levelStage=1;
		
		this.mapper = new ObjectMapper();
		this.enemyLevelFile = new File(getClass().getResource(LEVEL_DATA_FILE).getPath());
		this.bossLevelFile = new File(getClass().getResource(BOSS_LEVEL_FILE).getPath());
	}

	
	/**
	 * Sets focus to background and plays the level timeline
	 */
	public void startGame() {
		background.requestFocus();
		timeline.play();
	}
	
	/**
	 * Sets the level instance, background and timeline. Is called before startGame().
	 * @param level Either EnemyLevel or BossLevel class
	 */
	public void loadLevel(LevelParent level) {
		this.currentLevel = level;
		background = currentLevel.getBackground();
		initializeTimeline();
	}

	/**
	 * Creates new timeline for the current level
	 */
	private void initializeTimeline() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> currentLevel.updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}
	
	/**
	 * Not to be confused with JavaFX's stage, stage refers to levelStage, a number
	 * @param level levelStage number (starts at 1 by default)
	 */
	protected void setStage(int level) {
		this.levelStage = level;
	}
	
	/**
	 * Raises levelStage by 1.
	 */
	protected void raiseStage() {
		this.levelStage = levelStage+1;
	}
	
	
	/**
	 * Stops level and shows win image
	 */
	protected void winGame() {
		timeline.stop();
		root.getChildren().add(winImage);
		winImage.setVisible(true);
	}

	/**
	 * Stops level and shows lose image
	 */
	protected void loseGame() {
		timeline.stop();
		root.getChildren().add(gameOverImage);
		gameOverImage.setVisible(true);
	}
	
	/**
	 * @return enemyLevel.json file as a Jackson Tree node array
	 * @throws IOException
	 */
	protected JsonNode getLevelData() throws IOException {
		return mapper.readTree(enemyLevelFile);		
	}
	
	
	/**
	 * @return bossLevel.json file as a Jackson Tree node array
	 * @throws IOException
	 */
	protected JsonNode getBossData() throws IOException {
		return mapper.readTree(bossLevelFile);		
	}
	
	private ImageView loadWinImage() {
		ImageView winImage = new ImageView(new Image(getClass().getResourceAsStream("/com/example/demo/images/youwin.png")));
		winImage.setVisible(false);
		winImage.setFitHeight(500);
		winImage.setFitWidth(600);
		winImage.setLayoutX(355);
		winImage.setLayoutY(175);		
		return winImage;
		
	}
	
	private ImageView loadLoseImage() {
		ImageView loseImage = new ImageView(new Image(getClass().getResourceAsStream("/com/example/demo/images/gameover.png")));
		loseImage.setVisible(false);
		loseImage.setFitHeight(700);
		loseImage.setFitWidth(600);
		loseImage.setLayoutX(300);
		loseImage.setLayoutY(-50);
		return loseImage;
	}
	
	protected int getLevelStage() {
		return levelStage;
	}
	
	protected Group getRoot() {
		return root;
	}
	
	protected Timeline getTimeline() {
		return timeline;
	}
	
	protected Scene getScene() {
		return scene;
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenHeight() {
		return screenHeight;
	}
	
	protected double getScreenWidth() {
		return screenWidth;
	}

}
