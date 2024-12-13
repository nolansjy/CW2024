package com.example.demo;


/**
 * Parent class of FighterPlane Projectiles
 */
public abstract class Projectile extends SpriteDestructible {

	public Projectile(SpriteHitboxBuilder builder) {
		super(builder);
	}

	/**
	 * Destroys projectile upon 'taking damage' e.g. colliding with plane
	 */
	@Override
	public void takeDamage() {
		this.destroy();
	}

	@Override
	public abstract void updatePosition();
	
	public static abstract class ProjectileBuilder extends SpriteDestructible.SpriteHitboxBuilder {

		@Override
		public abstract SpriteHitboxBuilder load();

		@Override
		public abstract SpriteDestructible build();
		
	}
}
