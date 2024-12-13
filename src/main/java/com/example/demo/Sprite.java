package com.example.demo;

import javafx.scene.image.*;
import javafx.scene.layout.StackPane;


/**
 * Sprite is a StackPane that contains the base image, size, and position of a game object as an ImageView.
 * Formerly named ActiveActor.
 * <p>
 * A Sprite is built using its SpriteBuilder, which is a nested class.
 * Classes which extend from Sprite should also extend the SpriteBuilder and implement its own builder specifications.
 * </p>
 * <p>
 * The image file should be sized so that it is square and fit to the exact width of the intended image.
 * (i.e. Only the top and bottom of the image can be transparent, not the sides)
 * The height is used to resize the image with a fixed ratio.
 * </p>
 */

public abstract class Sprite extends StackPane {
	
	private static final String IMAGE_LOCATION = "/com/example/demo/images/";
	private final String imageName;
	private final double imageHeight;
	private final double initialX;
	private final double initialY;
	private final ImageView image;	
	
	/**
	 * Creates the base ImageView and adds it to itself as a child.
	 * @param builder Contains the fields needed for construction and position of ImageView.
	 * @see SpriteBuilder
	 */
	public Sprite(SpriteBuilder builder) {
		this.imageName = builder.imageName;
		this.imageHeight = builder.imageHeight;
		this.initialX = builder.initialX;
		this.initialY = builder.initialY;
		this.setPrefSize(imageHeight, imageHeight);
		this.image = new ImageView(new Image(getClass().getResourceAsStream(IMAGE_LOCATION + imageName)));
		this.image.setFitHeight(imageHeight);
		this.image.setPreserveRatio(true);
		this.getChildren().add(image);
		this.setLayoutX(initialX);
		this.setLayoutY(initialY);
	}
	
	/**
	 * Updates state of the game object
	 */
	public abstract void updateActor();

	/**
	 * Updates position of the game object
	 */
	public abstract void updatePosition();

	/**
	 * Sprite moves on X axis
	 * @param horizontalMove Number of pixels to move by (positive is right, negative is left)
	 */
	protected void moveHorizontally(double horizontalMove) {
		this.setTranslateX(getTranslateX() + horizontalMove);
	}

	/**
	 * Sprite moves on Y axis
	 * @param verticalMove Number of pixels to move by (up is negative, down is positive)
	 */
	protected void moveVertically(double verticalMove) {
		this.setTranslateY(getTranslateY() + verticalMove);
	}
	
	/**
	 * @return the base ImageView of the Sprite
	 */
	protected ImageView getImage() {
		return this.image;
	}
	
/**
 * SpriteBuilder sets the image path, height and XY position of the Sprite. 
 * A fluent interface is included which allows for more specific Sprite creation.
 */
public abstract static class SpriteBuilder {
		
		protected String imageName;		
		protected double imageHeight;
		protected double initialX;
		protected double initialY;
		
		/**
		 * @param imageName   file name of the image
		 * @param imageHeight fixed height of the image
		 * @param initialX    initial X position of the image
		 * @param initialY    initial Y position of the image
		 * @return SpriteBuilder instance
		 */
		public SpriteBuilder setImageView(String imageName, double imageHeight, double initialX, double initialY) {
			this.imageName = imageName;
			this.imageHeight = imageHeight;	
			this.initialX = initialX;
			this.initialY = initialY;
			return this;
		}
		
		/**
		 * @param imageName   image file name
		 * @param imageHeight fixed height of image
		 * @return SpriteBuilder instance
		 */
		public SpriteBuilder setImage(String imageName, double imageHeight) {
			this.imageName = imageName;
			this.imageHeight = imageHeight;						
			return this;
		}		
		
		/**
		 * @param initialX initial X position
		 * @param initialY initial Y position
		 * @return SpriteBuilder instance
		 */
		public SpriteBuilder setImagePos(double initialX, double initialY) {
			this.initialX = initialX;
			this.initialY = initialY;
			return this;
		}
		
		/**
		 * @param initialX initial Y position
		 * @return SpriteBuilder instance
		 */
		public SpriteBuilder setX(double initialX) {
			this.initialX = initialX;
			return this;
		}
		
		/**
		 * @param initialY initial Y position
		 * @return SpriteBuilder instance
		 */
		public SpriteBuilder setY(double initialY) {
			this.initialY = initialY;
			return this;
		}
		
		/**
		 * Convenience method to set default or constant values of a game object. 
		 * Should be called after custom values have been set and before build()
		 * 
		 * Example: A Sprite always has a random position
		 * <pre>
		 * {@code
		 * 
		 *  // load() is overriden in CustomSpriteBuilder extended from SpriteBuilder
		 *  load(){
		 *  	setImage("myimage.jpg", 100)
		 *  }
		 *  
		 *  // CustomSprite extends from Sprite
		 *  CustomSprite sprite = new MyCustomSprite.CustomSpriteBuilder
		 *  						.setInitialPos(randomX, randomY).load().build();
		 *  
		 * }
		 * </pre>
		 * @return SpriteBuilder instance
		 */
		public abstract SpriteBuilder load();
		
		/**
		 * @return Built Sprite object
		 */
		public abstract Sprite build();
		
	}

}
