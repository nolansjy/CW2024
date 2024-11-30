package com.example.demo;

import javafx.scene.shape.Rectangle;

public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed;
	private Rectangle hitbox;

	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.hitbox = new Rectangle(initialXPos, initialYPos, imageHeight, imageHeight/3);
		this.getChildren().addAll(hitbox, getImage());
		isDestroyed = false;
		this.setLayoutX(initialXPos);
		this.setLayoutY(initialYPos);
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
	
}
