package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

/**
 * Parent class for all plane objects.
 */
public abstract class FighterPlane extends SpriteDestructible {

	private int health;
	private int damageTaken;
	private ColorAdjust takenDamageEffect;
	private PauseTransition takenDamageAnimation;

	/**
	 * Constructs the FighterPlane object and sets the animation for taking damage.
	 * @param builder FighterPlaneBuilder instance
	 */
	public FighterPlane(FighterPlaneBuilder builder) {
		super(builder);
		this.health = builder.health;
		this.damageTaken = builder.damageTaken;
		takenDamageEffect = new ColorAdjust();
		takenDamageEffect.setBrightness(-0.4);
		takenDamageAnimation = new PauseTransition(Duration.seconds(0.5));
		takenDamageAnimation.setOnFinished(e->getImage().setEffect(null));
	}

	/**
	 * Fires projectile(s) based plane on implementation
	 * @return Projectile object to be fired
	 */
	public abstract SpriteDestructible fireProjectile();
	
	/**
	 * Takes damage according to the set damageTaken and runs the takeDamageAnimation (briefly turning dark)
	 * Destroys plane if health at zero.
	 */
	@Override
	public void takeDamage() {
		health-=damageTaken;
		takeDamageAnimation();
		if (healthAtZero()) {
			this.destroy();
		}
		
	}
	
	/**
	 * Sprite image darkens for half a second.
	 */
	private void takeDamageAnimation() {
		getImage().setEffect(takenDamageEffect);
		takenDamageAnimation.playFromStart();
	}

	/**
	 * Sets and gets X position of the projectile to be fired
	 * @param xPositionOffset X distance from plane image where projectile should appear
	 * @return X Position of fired projectile considering the plane's current position
	 */
	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

	/**
	 * Sets and gets Y position of the projectile to be fired
	 * @param yPositionOffset Y distance from the plane image where projectile should appear
	 * @return Y Position of fired projectile considering the plane's current position
	 */
	protected double getProjectileYPosition(double yPositionOffset) {
		return getLayoutY() + getTranslateY() + yPositionOffset;
	}

	private boolean healthAtZero() {
		return health == 0;
	}

	public int getHealth() {
		return health;
	}
	
	public abstract static class FighterPlaneBuilder extends SpriteHitboxBuilder {

		protected int health;
		protected int damageTaken;
				
		/**
		 * @param health health of plane
		 * @return builder instance
		 */
		public FighterPlaneBuilder setHealth(int health) {
			this.health = health;
			return this;
		}
		
		/**
		 * @param damageTaken damage taken when hit
		 * @return builder instance
		 */
		public FighterPlaneBuilder setDamageTaken(int damageTaken) {
			this.damageTaken = damageTaken;
			return this;
		}
		
	}
		
}
