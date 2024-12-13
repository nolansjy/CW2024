package com.example.demo;

public class EnemyPlane extends FighterPlane {

	private static final String IMAGE_NAME = "enemyplane.png";
	private static final int IMAGE_HEIGHT = 150;
	private static final int HORIZONTAL_VELOCITY = -6;
	private static final double PROJECTILE_X_POSITION_OFFSET = -100.0;
	private static final double PROJECTILE_Y_POSITION_OFFSET = 50.0;
	private static final int ENEMY_BASE_DAMAGE_TAKEN = 1;

	
	private final double fireRate;

	public EnemyPlane(EnemyPlaneBuilder builder) {
		super(builder);
		this.fireRate = builder.fireRate;
	}
	
	public static class EnemyPlaneBuilder extends FighterPlaneBuilder {
		
		protected double fireRate;
		
		public EnemyPlaneBuilder(int health, double fireRate) {
			setHealth(health);
			this.fireRate = fireRate;
		}
		

		@Override
		public SpriteHitboxBuilder load() {
			setDamageTaken(ENEMY_BASE_DAMAGE_TAKEN);
			setImage(IMAGE_NAME, IMAGE_HEIGHT);
			setHitboxHeight(IMAGE_HEIGHT/3);
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
		if (Math.random() < fireRate) {
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
