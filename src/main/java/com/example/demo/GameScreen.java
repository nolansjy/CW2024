package com.example.demo;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.util.Duration;

public class GameScreen {
	
	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	
	private final Group root;
	private final Timeline timeline;
	private final Scene scene;	
	private ImageView background;
	
	private final String backgroundImageName = "/com/example/demo/images/background1.jpg";
	private static final int WIN_IMAGE_X_POSITION = 355;
	private static final int WIN_IMAGE_Y_POSITION = 175;
	private static final int LOSS_SCREEN_X_POSITION = -160;
	private static final int LOSS_SCREEN_Y_POSISITION = -375;
	private final double enemyMaximumYPosition;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private LevelParent currentLevel;


	public GameScreen(double screenHeight, double screenWidth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSISITION);
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
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> currentLevel.updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}
	
	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		root.getChildren().add(background);
	}
	
	protected void winGame() {
		timeline.stop();
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}

	protected void loseGame() {
		timeline.stop();
		root.getChildren().add(gameOverImage);
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
