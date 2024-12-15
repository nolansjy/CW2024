package com.example.demo;

/**
 * Projectile fired by EnemyPlane
 * @see Projectile
 */
public class EnemyProjectile extends Projectile {
	
	private static final String IMAGE_NAME = "enemyfire.png";
	private static final int IMAGE_HEIGHT = 60;
	private static final int HORIZONTAL_VELOCITY = -8;

	/**
	 * Constructor for EnemyProjectile.
	 * @see ProjectileBuilder
	 */
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
	
	/**
	 * Builder for EnemyProjectile.
	 * @see ProjectileBuilder
	 */
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
