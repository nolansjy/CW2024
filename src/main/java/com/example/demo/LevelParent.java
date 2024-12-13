package com.example.demo;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.controller.Controller;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.Node;

/**
 * The LevelParent holds the main game renderer and level design data e.g. level background, enemy health
 */
public abstract class LevelParent {

	private static final String IMAGE_LOCATION = "/com/example/demo/images/";
	private final Group root;
	protected final Scene scene;
	private final ImageView background;
	protected final UserPlane user;
	
	private final List<SpriteDestructible> friendlyUnits;
	private final List<SpriteDestructible> enemyUnits;
	private final List<SpriteDestructible> userProjectiles;
	private final List<SpriteDestructible> enemyProjectiles;
	
	private int currentNumberOfEnemies;
	private LevelView levelView;
	protected GameScreen game;
	protected int playerHealth;
	
	private PropertyChangeSupport support;

	
	/**
	 * Constructs the level. Creates user plane and LevelView
	 * @param game Initial GameScreen instance
	 * @throws IOException
	 */
	public LevelParent(final GameScreen game) throws IOException {
		this.game = game;
		this.root = game.getRoot();		
		this.scene = game.getScene();
		this.background = getBackground(getBackgroundFile(game.getLevelStage()));
		this.playerHealth = getLevelPlayerHealth(game.getLevelStage());
		this.user = new UserPlane.UserPlaneBuilder().setHealth(playerHealth).load().build();
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		friendlyUnits.add(user);
		support = new PropertyChangeSupport(this);
	}

	/**
	 * Spawns userPlane
	 */
	protected abstract void initializeFriendlyUnits();

	/**
	 * Checks lose and win condition and next step
	 */
	protected abstract void checkIfGameOver();

	/**
	 * Spawns level enemy units
	 */
	protected abstract void spawnEnemyUnits();

	/**
	 * Creates and adds LevelView elements
	 * @return Level's LevelView
	 */
	protected abstract LevelView instantiateLevelView();
	
	/**
	 * Gets the background of the level as set by the level (e.g. levelStage=2 returns background for stage 2)
	 * @param levelStage the current level stage number
	 * @return background file name as String
	 * @throws IOException
	 */
	protected abstract String getBackgroundFile(int levelStage) throws IOException;
	
	/**
	 * Gets background of level as ImageView
	 * @param file name of image file as String
	 * @return level background
	 */
	protected ImageView getBackground(String file) {
		return new ImageView(new Image(getClass().getResourceAsStream(IMAGE_LOCATION+file)));
	}
			
	/**
	 * Using the below formula, player's health starts at 3 and increases by 1 with each stage
	 * @param levelStage current level stage number
	 * @return levelStage+2 as health
	 */
	protected int getLevelPlayerHealth(int levelStage) {
		return 2+levelStage;
	}
	
	/**
	 * Adds PropertyChangeListener
	 * @param controller Controller instance
	 */
	public void addListener(Controller controller) {
		support.addPropertyChangeListener(controller);
	}
	
	/**
	 * Stops timeline and notifies Controller to change level
	 * @param levelName class name of next level
	 */
	public void goToNextLevel(String levelName) {
		game.getTimeline().stop();
		support.firePropertyChange("level", this.getClass().getName(), levelName);
	}
	
	/**
	 * Sets background, user, and user health display. Not changed from original.
	 * @return Level's Scene node
	 */
	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	/**
	 * Sets level background and user inputs. Arrow keys to move user plane and space to fire projectiles.
	 */
	protected void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(game.getScreenHeight());
		background.setFitWidth(game.getScreenWidth());
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) user.moveUp();
				if (kc == KeyCode.RIGHT) user.moveFront();
				if (kc == KeyCode.LEFT) user.moveBack();
				if (kc == KeyCode.DOWN) user.moveDown();
				if (kc == KeyCode.SPACE) fireProjectile();
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP    || 
					kc == KeyCode.DOWN  || 
					kc == KeyCode.RIGHT ||
					kc == KeyCode.LEFT) user.stop();
			}
		});
		root.getChildren().add(background);
	}

	/**
	 * The main game renderer. <br>
	 * Functions unchanged from original, except that handleCollisions() 
	 * now uses the SpriteDestructible hitbox as bounds instead of the entire image
	 */
	protected void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}	

	private void fireProjectile() {
		SpriteDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(SpriteDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<SpriteDestructible> actors) {
		List<SpriteDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleCollisions(List<SpriteDestructible> actors1,
			List<SpriteDestructible> actors2) {
		for (SpriteDestructible actor : actors2) {
			Node hitbox = actor.getChildren().get(0);
			Bounds actorBounds =  hitbox.localToScene(hitbox.getBoundsInLocal());
			for (SpriteDestructible otherActor : actors1) {
				Node otherHitbox = otherActor.getChildren().get(0);
				Bounds otherBounds =  otherHitbox.localToScene(otherHitbox.getBoundsInLocal());
				if (actorBounds.intersects(otherBounds)) {
					actor.takeDamage();
					otherActor.takeDamage();
				}
			}
		}
	}

	private void handleEnemyPenetration() {
		for (SpriteDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	/**
	 * Updates continuous LevelView elements
	 */
	protected void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	private boolean enemyHasPenetratedDefenses(SpriteDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > game.getScreenWidth();
	}

	protected UserPlane getUser() {
		return user;
	}


	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(SpriteDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}


	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
	
	protected ImageView getBackground() {
		return background;
	}

}
