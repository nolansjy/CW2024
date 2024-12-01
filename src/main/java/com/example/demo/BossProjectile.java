package com.example.demo;


public class BossProjectile extends Projectile {
	
	private static final String IMAGE_NAME = "fireball.png";
	private static final int IMAGE_HEIGHT = 75;
	private static final int HORIZONTAL_VELOCITY = -15;
	private static final int INITIAL_X_POSITION = 950;

	public BossProjectile(ProjectileBuilder builder) {
		super(builder);
	}

	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}
	
	@Override
	public void updateActor() {
		updatePosition();
	}
	
	public static class ProjectileBuilder extends Projectile.ProjectileBuilder {

		@Override
		public SpriteHitboxBuilder load() {
			setImage(IMAGE_NAME, IMAGE_HEIGHT);
			setX(INITIAL_X_POSITION);
			setHitboxHeight(IMAGE_HEIGHT/3);
			return this;
		}

		@Override
		public SpriteDestructible build() {
			return new BossProjectile(this);
		}
		
	}
	
}
