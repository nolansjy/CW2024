package com.example.demo;

public abstract class FighterPlane extends SpriteDestructible {

	private int health;

	public FighterPlane(FighterPlaneBuilder builder) {
		super(builder);
		this.health = builder.health;
	}

	public abstract SpriteDestructible fireProjectile();
	
	@Override
	public void takeDamage() {
		health--;
		if (healthAtZero()) {
			this.destroy();
		}
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
				
		public FighterPlaneBuilder setHealth(int health) {
			this.health = health;
			return this;
		}		
		
	}
		
}
