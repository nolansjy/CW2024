package com.example.demo;

import com.example.demo.Sprite.SpriteBuilder;

import javafx.scene.shape.Rectangle;

public abstract class SpriteDestructible extends Sprite implements Destructible {

	private boolean isDestroyed;
	private Rectangle hitbox;
	private final double hitboxHeight;
	private final double hitboxWidth;

	public SpriteDestructible(SpriteHitboxBuilder builder) {
		super(builder);
		this.hitboxWidth = builder.imageHeight;
		this.hitboxHeight = builder.hitboxHeight;
		this.hitbox = new Rectangle(builder.initialX, builder.initialY, hitboxWidth, hitboxHeight);
		this.getChildren().addAll(hitbox, getImage());
		this.setLayoutX(builder.initialX);
		this.setLayoutY(builder.initialY);
		isDestroyed = false;
	}

	@Override
	public abstract void updatePosition();

	@Override
	public abstract void updateActor();

	@Override
	public abstract void takeDamage();

	@Override
	public void destroy() {
		setDestroyed(true);
	}

	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}
	
	public abstract static class SpriteHitboxBuilder extends Sprite.SpriteBuilder {

		private double hitboxHeight;
				
		public SpriteHitboxBuilder setHitboxHeight(double hitboxHeight) {
			this.hitboxHeight = hitboxHeight;
			return this;
		}
		
		public SpriteHitboxBuilder setImagePos(double initialX, double initialY) {
			this.initialX = initialX;
			this.initialY = initialY;
			return this;
		}
		
		public SpriteHitboxBuilder setX(double initialX) {
			this.initialX = initialX;
			return this;
		}
		
		public SpriteHitboxBuilder setY(double initialY) {
			this.initialY = initialY;
			return this;
		}
		
		@Override
		public abstract SpriteHitboxBuilder load();
		
		@Override
		public abstract SpriteDestructible build();
	}
	
}
