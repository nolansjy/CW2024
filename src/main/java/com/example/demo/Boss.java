package com.example.demo;

import java.util.*;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Boss extends EnemyPlane {

	private static final String IMAGE_NAME = "bossplane.png";
	private static final int IMAGE_HEIGHT = 300;
	private static final double INITIAL_X_POSITION = 1000.0;
	private static final double INITIAL_Y_POSITION = 400;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
	private static final double BOSS_FIRE_RATE = .04;
	private static final double BOSS_SHIELD_PROBABILITY = .002;	
	private static final int VERTICAL_VELOCITY = 8;
	private static final int MOVE_FREQUENCY_PER_CYCLE = 5;
	private static final int ZERO = 0;
	private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
	private static final int Y_POSITION_UPPER_BOUND = -100;
	private static final int Y_POSITION_LOWER_BOUND = 475;
	private static final int MAX_FRAMES_WITH_SHIELD = 500;
	private static final int DAMAGE_TAKEN = 5;
	
	private final Circle shield;
	private final List<Integer> movePattern;
	private boolean isShielded;
	private int consecutiveMovesInSameDirection;
	private int indexOfCurrentMove;
	private int framesWithShieldActivated;
	private final int maxShieldFrames;
	private final double shieldProbability;

	public Boss(BossBuilder builder) {
		super(builder);
		this.shield = new Circle(INITIAL_X_POSITION, INITIAL_Y_POSITION, builder.imageHeight/2, Color.GOLD);
		shield.setOpacity(0.5);
		shield.setVisible(false);
		this.getChildren().add(shield);				
		this.shieldProbability = builder.shieldProbability;
		this.maxShieldFrames = builder.maxShieldFrames;
		
		movePattern = new ArrayList<>();
		consecutiveMovesInSameDirection = 0;
		indexOfCurrentMove = 0;
		framesWithShieldActivated = 0;
		isShielded = false;
		initializeMovePattern();
	}
	
	public static class BossBuilder extends EnemyPlaneBuilder {
		
		protected final double shieldProbability;
		protected final int maxShieldFrames;

		public BossBuilder(int health, double fireRate, double shieldProbability, int maxShieldFrames) {
			super(health, fireRate);
			this.shieldProbability = shieldProbability;
			this.maxShieldFrames = maxShieldFrames;
		}

		@Override 
		public BossBuilder setHealth(int health) {
			this.health = health;
			return this;
		}	
		
		@Override
		public BossBuilder load() {
			setImageView(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION);
			setHitboxHeight(IMAGE_HEIGHT/3);
			setDamageTaken(DAMAGE_TAKEN);
			return this;
		}
		
		
		@Override
		public Boss build() {
			return new Boss(this);
		}
				
		
	}

	@Override
	public void updatePosition() {
		double initialTranslateY = getTranslateY();
		moveVertically(getNextMove());
		double currentPosition = getLayoutY() + getTranslateY();
		if (currentPosition < Y_POSITION_UPPER_BOUND || currentPosition > Y_POSITION_LOWER_BOUND) {
			setTranslateY(initialTranslateY);
		}
	}
	
	@Override
	public void updateActor() {
		updatePosition();
		updateShield();
	}

	@Override
	public SpriteDestructible fireProjectile() {
		return bossFiresInCurrentFrame()  
		?  new BossProjectile.ProjectileBuilder()
		  .setY(getProjectileInitialPosition()).load().build() 
		: null;
	}
	
	@Override
	public void takeDamage() {
		if (!isShielded) {
			super.takeDamage();
		}
	}

	private void initializeMovePattern() {
		for (int i = 0; i < MOVE_FREQUENCY_PER_CYCLE; i++) {
			movePattern.add(VERTICAL_VELOCITY);
			movePattern.add(-VERTICAL_VELOCITY);
			movePattern.add(ZERO);
		}
		Collections.shuffle(movePattern);
	}

	private void updateShield() {
		if (isShielded) framesWithShieldActivated++;
		else if (shieldShouldBeActivated()) activateShield();	
		if (shieldExhausted()) deactivateShield();
	}

	private int getNextMove() {
		int currentMove = movePattern.get(indexOfCurrentMove);
		consecutiveMovesInSameDirection++;
		if (consecutiveMovesInSameDirection == MAX_FRAMES_WITH_SAME_MOVE) {
			Collections.shuffle(movePattern);
			consecutiveMovesInSameDirection = 0;
			indexOfCurrentMove++;
		}
		if (indexOfCurrentMove == movePattern.size()) {
			indexOfCurrentMove = 0;
		}
		return currentMove;
	}

	private boolean bossFiresInCurrentFrame() {
		return Math.random() < BOSS_FIRE_RATE;
	}

	private double getProjectileInitialPosition() {
		return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
	}

	private boolean shieldShouldBeActivated() {
		return Math.random() < shieldProbability;
	}

	private boolean shieldExhausted() {
		return framesWithShieldActivated == maxShieldFrames;
	}

	private void activateShield() {
		shield.setVisible(true);
		isShielded = true;
	}

	private void deactivateShield() {
		shield.setVisible(false);
		isShielded = false;
		framesWithShieldActivated = 0;
	}

}
