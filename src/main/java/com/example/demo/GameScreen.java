package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
	private final double enemyMaximumYPosition;
	
	private static final String DEFAULT_BACKGROUND = "/com/example/demo/images/enemy_bg1.jpg";
	private static final String BOSS_LEVEL_FILE = "/com/example/demo/data/bossLevel.json";
	private static final String LEVEL_DATA_FILE = "/com/example/demo/data/enemyLevel.json";	
	private static final String UI_STYLE_FILE = "/com/example/demo/styles/gameUI.css";	
	private final ObjectMapper mapper;
	private final File enemyLevelFile;
	private final File bossLevelFile;	
	private final PauseTransition removeAlert;
	private Label alert;
	private LevelParent currentLevel;	
	private int stageType;
	private int difficulty;


	/**
	 * Constructed by the Controller
	 * @param screenHeight used to create Scene
	 * @param screenWidth used to create Scene
	 */
	public GameScreen(double screenHeight, double screenWidth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		scene.getStylesheets().add(getClass().getResource(UI_STYLE_FILE).toExternalForm());
		this.timeline = new Timeline();
		this.background = new ImageView(new Image(getClass().getResourceAsStream(DEFAULT_BACKGROUND)));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;		
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;		
		this.mapper = new ObjectMapper();
		this.enemyLevelFile = new File(getClass().getResource(LEVEL_DATA_FILE).getPath());
		this.bossLevelFile = new File(getClass().getResource(BOSS_LEVEL_FILE).getPath());
		
		this.removeAlert = new PauseTransition(Duration.seconds(2));
		this.stageType = 1;
		this.difficulty = 0;
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
	 * Raises difficulty of levels by 1
	 */
	protected void raiseDifficulty() {
		this.difficulty = difficulty+1;
	}
	
	/**
	 * Sets type of stage for all levels (Currently Stage 1-3, Stage 0 for testing)
	 * @param stageType level's stage type
	 */
	protected void setStageType(int stageType) {
		this.stageType = stageType;
	}
	
	/**
	 * Goes to the next stage type by incrementing 1.
	 */
	protected void nextStageType() {
		this.stageType = stageType+1;
	}
	
	/**
	 * Displays a message on game screen
	 * @param text message content
	 */
	protected void addAlert(String text) {
		alert = new Label(text);
		alert.getStyleClass().add("alert");
		alert.setLayoutX(550);
		alert.setLayoutY(60);
		root.getChildren().add(alert);
		removeAlert.setOnFinished(e -> root.getChildren().remove(alert));
		removeAlert.play();
	}
	
	/**
	 * Stops level and shows win image
	 */
	protected void winGame() {
		timeline.stop();
		ImageView winImage = loadWinImage();
		root.getChildren().add(winImage);
		winImage.setVisible(true);
	}
	
	

	/**
	 * Stops level and shows lose image
	 */
	protected void loseGame() {
		timeline.stop();
		ImageView gameOverImage = loadLoseImage();
		root.getChildren().add(gameOverImage);
		gameOverImage.setVisible(true);
	}		
	
	
	
	/**
	 * Difficulty of current round
	 * @return difficulty
	 */
	public int getDifficulty() {
		return difficulty;
	}
		
	
	/**
	 * Stage type for current level
	 * @return current level's stage type
	 */
	protected int getStageType() {
		return stageType;
	}
		
	/**
	 * LevelData is the contents of enemyLevel.json
	 * @return returns JSON as Jackson Tree node array
	 * @throws IOException
	 */
	protected JsonNode getLevelData() throws IOException {
		return mapper.readTree(enemyLevelFile);		
	}
	
	
	/**
	 * BossData() is the contents of bossLevel.json
	 * @return returns JSON as Jackson Tree node array
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
	
	
	/**
	 * Returns JavaFX Group root node
	 * @return root node 
	 */
	protected Group getRoot() {
		return root;
	}
	
	/**
	 * Timeline is used to start/stop levels
	 * @return Timeline
	 */
	protected Timeline getTimeline() {
		return timeline;
	}
	
	/**
	 * Game instance's Scene node
	 * @return Scene
	 */
	protected Scene getScene() {
		return scene;
	}

	/**
	 * Maximum height an enemy can spawn
	 * @return enemyMaximumYPosition
	 */
	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	/**
	 * Height of screen
	 * @return screenHeight
	 */
	protected double getScreenHeight() {
		return screenHeight;
	}
	
	protected double getScreenWidth() {
		return screenWidth;
	}




}
