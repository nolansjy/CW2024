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
	private static final String USER_DATA_FILE = "/com/example/demo/data/userData.json";
	private static final String BOSS_LEVEL_FILE = "/com/example/demo/data/bossLevel.json";
	private static final String LEVEL_DATA_FILE = "/com/example/demo/data/enemyLevel.json";
	
	private static final int WIN_IMAGE_X_POSITION = 355;
	private static final int WIN_IMAGE_Y_POSITION = 175;
	private static final int LOSS_SCREEN_X_POSITION = -160;
	private static final int LOSS_SCREEN_Y_POSISITION = -375;
	private final double enemyMaximumYPosition;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private LevelParent currentLevel;
	private int levelStage;
	private final ObjectMapper mapper;
	private final File enemyLevelFile;
	private final File bossLevelFile;
	


	public GameScreen(double screenHeight, double screenWidth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.background = new ImageView(new Image(getClass().getResourceAsStream(DEFAULT_BACKGROUND)));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSISITION);
		this.levelStage=1;
		
		this.mapper = new ObjectMapper();
		this.enemyLevelFile = new File(getClass().getResource(LEVEL_DATA_FILE).getPath());
		this.bossLevelFile = new File(getClass().getResource(BOSS_LEVEL_FILE).getPath());
	}

	
	public void startGame() {
		background.requestFocus();
		timeline.play();
	}
	
	public void loadLevel(LevelParent level) {
		this.currentLevel = level;
		background = currentLevel.getBackground();
		initializeTimeline();
	}

	private void initializeTimeline() {
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> currentLevel.updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}
	
	protected void raiseStage() {
		this.levelStage = levelStage+1;
	}
	
	
	protected void winGame() {
		timeline.stop();
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		root.getChildren().add(gameOverImage);
		gameOverImage.showLoseImage();
	}
	
	protected JsonNode getLevelData() throws IOException {
		return mapper.readTree(enemyLevelFile);		
	}
	
	
	protected JsonNode getBossData() throws IOException {
		return mapper.readTree(bossLevelFile);		
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
