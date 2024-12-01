package com.example.demo;

import javafx.scene.image.*;
import javafx.scene.layout.StackPane;


public abstract class Sprite extends StackPane {
	
	private static final String IMAGE_LOCATION = "/com/example/demo/images/";
	private final String imageName;
	private final double imageHeight;
	private final double initialX;
	private final double initialY;
	private final ImageView image;	
	
	public Sprite(SpriteBuilder builder) {
		this.imageName = builder.imageName;
		this.imageHeight = builder.imageHeight;
		this.initialX = builder.initialX;
		this.initialY = builder.initialY;
		this.setPrefSize(imageHeight, imageHeight);
		this.image = new ImageView(new Image(getClass().getResource(IMAGE_LOCATION + imageName).toExternalForm()));
		this.image.setFitHeight(imageHeight);
		this.image.setPreserveRatio(true);
	}
	
	public abstract void updateActor();

	public abstract void updatePosition();

	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
	
	protected ImageView getImage() {
		return this.image;
	}
	
public abstract static class SpriteBuilder {
		
		protected String imageName;		
		protected double imageHeight;
		protected double initialX;
		protected double initialY;
		
		public SpriteBuilder setImageView(String imageName, double imageHeight, double initialX, double initialY) {
			this.imageName = imageName;
			this.imageHeight = imageHeight;	
			this.initialX = initialX;
			this.initialY = initialY;
			return this;
		}
		
		public SpriteBuilder setImage(String imageName, double imageHeight) {
			this.imageName = imageName;
			this.imageHeight = imageHeight;						
			return this;
		}
		
		
		
		public abstract SpriteBuilder load();
		public abstract Sprite build();
		
	}

}
