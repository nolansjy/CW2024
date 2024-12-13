package com.example.demo;

import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

public abstract class FighterPlane extends SpriteDestructible {

	private int health;
	private int damageTaken;
	private ColorAdjust takenDamageEffect;
	private PauseTransition takenDamageAnimation;

	public FighterPlane(FighterPlaneBuilder builder) {
		super(builder);
		this.health = builder.health;
		this.damageTaken = builder.damageTaken;
		takenDamageEffect = new ColorAdjust();
		takenDamageEffect.setBrightness(-0.4);
		takenDamageAnimation = new PauseTransition(Duration.seconds(0.5));
		takenDamageAnimation.setOnFinished(e->getImage().setEffect(null));
	}

	public abstract SpriteDestructible fireProjectile();
	
	@Override
	public void takeDamage() {
		health-=damageTaken;
		takeDamageAnimation();
		if (healthAtZero()) {
			this.destroy();
		}
		
	}
	
	protected void takeDamageAnimation() {
		getImage().setEffect(takenDamageEffect);
		takenDamageAnimation.playFromStart();
	}

	protected double getProjectileXPosition(double xPositionOffset) {
		return getLayoutX() + getTranslateX() + xPositionOffset;
	}

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
				
		public FighterPlaneBuilder setHealth(int health) {
			this.health = health;
			return this;
		}
		
		public FighterPlaneBuilder setDamageTaken(int damageTaken) {
			this.damageTaken = damageTaken;
			return this;
		}
		
	}
		
}
