package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Contains Game Over image as ImageView. Unchanged from original implementation.
 */
public class GameOverImage extends ImageView {
	
	private static final String IMAGE_NAME = "/com/example/demo/images/gameover.png";

	public GameOverImage(double xPosition, double yPosition) {
		setImage(new Image(getClass().getResource(IMAGE_NAME).toExternalForm()) );
		this.setVisible(false);
		setLayoutX(xPosition);
		setLayoutY(yPosition);
	}
	
	public void showLoseImage() {
		this.setVisible(true);
	}

}
