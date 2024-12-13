package com.example.demo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * LevelView for BossLevel includes a boss healthbar
 */
public class BossLevelView extends LevelView {

	private static final int HEALTHBAR_WIDTH = 400;
	private final Group root;
	private final Rectangle healthBar;
	private final Rectangle healthBarBG;
	private final DoubleProperty bossHealth;
	
	/**
	 * The healthbar object is constructed.<b>
	 * bossHealth is converted into a SimpleDoubleProperty so the health
	 * @param root JavaFX root node
	 * @param heartsToDisplay player's health
	 * @param bossHealth boss health
	 */
	public BossLevelView(Group root, int heartsToDisplay, int bossHealth) {
		super(root, heartsToDisplay);
		this.root = root;
		this.healthBar = new Rectangle(450,50,HEALTHBAR_WIDTH,10);
		this.healthBarBG = new Rectangle(448,48,HEALTHBAR_WIDTH+6,15);
		this.bossHealth = new SimpleDoubleProperty(bossHealth);
	}
	
	/**
	 * Adds healthbar to root scene. 
	 * The healthbar display is bound to the bossHealth property so it updates when bossHealth changes.
	 */
	public void showBossHealth() {
		double healthBarRatio = HEALTHBAR_WIDTH/bossHealth.doubleValue();
		healthBar.widthProperty().bind(bossHealth.multiply(healthBarRatio));
		healthBar.setFill(Color.TOMATO);
		root.getChildren().addAll(healthBarBG, healthBar);
	}
	
	/**
	 * Included in updateLevelView so healthbar decreases when boss health decreases
	 * @param currentHealth current bossHealth
	 */
	public void updateBossHealth(int currentHealth) {
		bossHealth.set(currentHealth);
	}

}
