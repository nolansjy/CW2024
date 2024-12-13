package com.example.demo;

import javafx.scene.shape.Rectangle;

/**
 * SpriteDestructible adds a hitbox to the base Sprite using a Rectangle.
 * It also implements the Destructible interface and can now take damage and be destroyed.
 * <p>
 * The current hitbox implementation sets the Rectangle to be the same width of its base image. 
 * The Rectangle height is then resized from the center. 
 * </p>
 * <p>
 * This height is manually determined through external methods. 
 * For example, using a photo editor, the correct hitbox height is found to be roughly 1/3 of the image height. 
 * </p>
 */
public abstract class SpriteDestructible extends Sprite {

	private boolean isDestroyed;
	private Rectangle hitbox;
	private final double hitboxHeight;
	private final double hitboxWidth;

	/**
	 * Constructs a hitbox using the height in the builder and the width of the Sprite image
	 * 
	 * The hitbox is sent to the back of the StackPane and can be accessed at index 0.
	 * @param builder builder instance
	 */
	public SpriteDestructible(SpriteHitboxBuilder builder) {
		super(builder);
		this.hitboxWidth = builder.imageHeight;
		this.hitboxHeight = builder.hitboxHeight;
		this.hitbox = new Rectangle(builder.initialX, builder.initialY, hitboxWidth, hitboxHeight);
		this.getChildren().add(hitbox);
		hitbox.toBack();
		hitbox.setVisible(false);
		isDestroyed = false;
	}

	@Override
	public abstract void updatePosition();

	@Override
	public abstract void updateActor();

	public abstract void takeDamage();

	public void destroy() {
		setDestroyed(true);
	}

	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}
	
	/**
	 * 
	 */
	public abstract static class SpriteHitboxBuilder extends Sprite.SpriteBuilder {

		protected double hitboxHeight;
				
		/**
		 * @param hitboxHeight height of hitbox
		 * @return builder instance
		 */
		public SpriteHitboxBuilder setHitboxHeight(double hitboxHeight) {
			this.hitboxHeight = hitboxHeight;
			return this;
		}
		
		@Override 
		public SpriteHitboxBuilder setImagePos(double initialX, double initialY) {
			this.initialX = initialX;
			this.initialY = initialY;
			return this;
		}
		
		@Override
		public SpriteHitboxBuilder setX(double initialX) {
			this.initialX = initialX;
			return this;
		}
		
		@Override
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
