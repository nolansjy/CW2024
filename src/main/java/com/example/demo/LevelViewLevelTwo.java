package com.example.demo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelViewLevelTwo extends LevelView {

	private static final int SHIELD_X_POSITION = 1150;
	private static final int SHIELD_Y_POSITION = 500;
	private final Group root;
	private final ShieldImage shieldImage;
	private final Rectangle healthBar;
	private final Rectangle healthBarBG;
	private final DoubleProperty bossHealth;
	
	public LevelViewLevelTwo(Group root, int heartsToDisplay, int bossHealth) {
		super(root, heartsToDisplay);
		this.root = root;
		this.shieldImage = new ShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		this.healthBar = new Rectangle(450,50,400,10);
		this.healthBarBG = new Rectangle(448,48,406,15);
		this.bossHealth = new SimpleDoubleProperty(bossHealth);
	}
	
	public void showBossHealth() {
		healthBar.widthProperty().bind(bossHealth.multiply(4));
		healthBar.setFill(Color.TOMATO);
		root.getChildren().addAll(healthBarBG, healthBar);
	}
	
	public void updateBossHealth(int currentHealth) {
		bossHealth.set(currentHealth);
	}
	
	public void showShield() {
		shieldImage.showShield();
		root.getChildren().add(shieldImage);
	}

	public void hideShield() {
		shieldImage.hideShield();
	}

}
