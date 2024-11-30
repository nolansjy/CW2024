package com.example.demo;

import javafx.scene.image.*;
import javafx.scene.layout.StackPane;

public abstract class ActiveActor extends StackPane {
	
	private static final String IMAGE_LOCATION = "/com/example/demo/images/";
	private final ImageView image;

	public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		//this.setImage(new Image(IMAGE_LOCATION + imageName));
		this.setPrefSize(imageHeight, imageHeight/3);
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

}
