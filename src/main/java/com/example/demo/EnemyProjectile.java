package com.example.demo;

/**
 * Projectile fired by EnemyPlane
 */
public class EnemyProjectile extends Projectile {
	
	private static final String IMAGE_NAME = "enemyfire.png";
	private static final int IMAGE_HEIGHT = 50;
	private static final int HORIZONTAL_VELOCITY = -10;

	public EnemyProjectile(ProjectileBuilder builder) {
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
			setHitboxHeight(IMAGE_HEIGHT/3);
			return this;
		}

		@Override
		public SpriteDestructible build() {
			return new EnemyProjectile(this);
		}
		
	}

}
