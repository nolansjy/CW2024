package com.example.demo;


public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;
	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 150;
	private static final int VERTICAL_VELOCITY = 10;
	private static final int HORIZONTAL_VELOCITY = 10;
	private static final int PROJECTILE_X_POSITION_OFFSET = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
	private int velocityMultiplier;
	private int numberOfKills;
	private int movementAxis;

	public UserPlane(UserPlaneBuilder builder) {
		super(builder);
		velocityMultiplier = 0;
		movementAxis = 0;
	}
	
	public static class UserPlaneBuilder extends FighterPlaneBuilder {

		@Override 
		public UserPlaneBuilder setHealth(int health) {
			this.health = health;
			return this;
		}
		
		@Override
		public UserPlaneBuilder load() {
			setImageView(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION);
			setHitboxHeight(IMAGE_HEIGHT/3);
			setDamageTaken(1);
			return this;
		}
		
		@Override
		public UserPlane build() {
			return new UserPlane(this);
		}
	
		
	}
	
	@Override
	public void updatePosition() {
		if (isMoving()) {			
			if(movingByY()) {
				moveY();
			}else {
				moveX();
			}
		}
	}
	
	private void moveY() {
		double initialTranslateY = getTranslateY();
		this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
		double newPosition = getLayoutY() + getTranslateY();
		if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
			this.setTranslateY(initialTranslateY);
		}
	}
	
	private void moveX() {
		double initialTranslateX = getTranslateX();
		this.moveHorizontally(HORIZONTAL_VELOCITY * velocityMultiplier);
		double newPosition = getLayoutX() + getTranslateX();
		if (newPosition > 1000 || newPosition < 5) { //TODO: Constant bounds
			this.setTranslateX(initialTranslateX);
		}
	}
	
	@Override
	public void updateActor() {
		updatePosition();
	}
	
	@Override
	public SpriteDestructible fireProjectile() {
		
		return new UserProjectile.ProjectileBuilder()
				.setImagePos(getProjectileXPosition(PROJECTILE_X_POSITION_OFFSET), 
						     getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET))
				.load().build();
	}

	private boolean isMoving() {
		return velocityMultiplier != 0;
	}
	
	private boolean movingByY() {
		return (movementAxis*velocityMultiplier) < 0;
	}
	

	public void moveUp() {
		movementAxis = 1;
		velocityMultiplier = -1;
	}

	public void moveDown() {
		movementAxis = -1;
		velocityMultiplier = 1;
	}
	
	public void moveFront() {
		movementAxis = 1;
		velocityMultiplier = 1;
	}
	
	public void moveBack() {
		movementAxis = -1;
		velocityMultiplier = -1;
	}

	public void stop() {
		velocityMultiplier = 0;
	}

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}

}
