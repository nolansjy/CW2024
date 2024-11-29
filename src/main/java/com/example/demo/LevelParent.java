package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.util.Duration;

public abstract class LevelParent extends Observable {

	private final Group root;
	private final Scene scene;
	private final ImageView background;
	private final UserPlane user;
	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	
	private int currentNumberOfEnemies;
	private LevelView levelView;
	protected GameScreen game;
	
	public LevelParent(final GameScreen game, String backgroundImageName, int playerInitialHealth) {
		this.game = game;
		this.root = game.getRoot();		
		this.scene = game.getScene();
		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		friendlyUnits.add(user);
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	public void goToNextLevel(String levelName) {
		setChanged();
		notifyObservers(levelName);
	}

	// TODO: This is bad!!
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
	

	public Scene initializeScene() {
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	protected void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(game.getScreenHeight());
		background.setFitWidth(game.getScreenWidth());
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) user.moveUp();
				if (kc == KeyCode.DOWN) user.moveDown();
				if (kc == KeyCode.SPACE) fireProjectile();
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
			}
		});
		root.getChildren().add(background);
	}

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
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

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
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

	private void handleCollisions(List<ActiveActorDestructible> actors1,
			List<ActiveActorDestructible> actors2) {
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
					actor.takeDamage();
					otherActor.takeDamage();
				}
			}
		}
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > game.getScreenWidth();
	}

	protected UserPlane getUser() {
		return user;
	}


	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
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
