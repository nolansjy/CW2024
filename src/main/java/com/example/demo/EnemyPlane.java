package com.example.demo;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int INITIAL_HEALTH = 1;
	private static final double FIRE_RATE = .01;

	public EnemyPlane(EnemyPlaneBuilder builder) {
		super(builder);
	}
	
	public static class EnemyPlaneBuilder extends FighterPlaneBuilder {

		@Override
		public SpriteHitboxBuilder load() {
			setImage(IMAGE_NAME, IMAGE_HEIGHT);
			setHitboxHeight(IMAGE_HEIGHT/3);
			setHealth(INITIAL_HEALTH);
			return this;
		}
		
		@Override
		public SpriteDestructible build() {
			return new EnemyPlane(this);
		}		
		
	}

	@Override
	public void updatePosition() {
		moveHorizontally(HORIZONTAL_VELOCITY);
	}

	@Override
	public SpriteDestructible fireProjectile() {
		if (Math.random() < FIRE_RATE) {
			double projectileXPosition = getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET);
			double projectileYPostion = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
			return (SpriteDestructible) new EnemyProjectile.ProjectileBuilder()
					.setImagePos(projectileXPosition, projectileYPostion)
					.load().build();
		}
		return null;
	}

	@Override
	public void updateActor() {
		updatePosition();
	}

}
