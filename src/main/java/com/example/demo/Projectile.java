package com.example.demo;


/**
 * Parent class of FighterPlane Projectiles
 */
public abstract class Projectile extends SpriteDestructible {

	/**
	 * Projectile builder
	 * @param builder SpriteHitboxBuilder
	 * @see SpriteHitboxBuilder
	 */
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
	
	/**
	 * Builds a Projectile. 
	 * @see SpriteHitboxBuilder
	 */
	public static abstract class ProjectileBuilder extends SpriteDestructible.SpriteHitboxBuilder {

		@Override
		public abstract SpriteHitboxBuilder load();

		@Override
		public abstract SpriteDestructible build();
		
	}
}
