package com.example.demo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BossLevelView extends LevelView {

	private static final int HEALTHBAR_WIDTH = 400;
	private final Group root;
	private final Rectangle healthBar;
	private final Rectangle healthBarBG;
	private final DoubleProperty bossHealth;
	
	public BossLevelView(Group root, int heartsToDisplay, int bossHealth) {
		super(root, heartsToDisplay);
		this.root = root;
		this.healthBar = new Rectangle(450,50,HEALTHBAR_WIDTH,10);
		this.healthBarBG = new Rectangle(448,48,HEALTHBAR_WIDTH+6,15);
		this.bossHealth = new SimpleDoubleProperty(bossHealth);
	}
	
	public void showBossHealth() {
		double healthBarRatio = HEALTHBAR_WIDTH/bossHealth.doubleValue();
		healthBar.widthProperty().bind(bossHealth.multiply(healthBarRatio));
		healthBar.setFill(Color.TOMATO);
		root.getChildren().addAll(healthBarBG, healthBar);
	}
	
	public void updateBossHealth(int currentHealth) {
		bossHealth.set(currentHealth);
	}

}
