package com.example.demo.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.GameScreen;
import com.example.demo.LevelParent;

public class Controller implements PropertyChangeListener {

	private static final String ENEMY_LEVEL_CLASS_NAME = "com.example.demo.EnemyLevel";
	private final Stage stage;
	private final GameScreen game;

	public Controller(Stage stage) {
		this.stage = stage;
		this.game = new GameScreen(stage.getHeight(), stage.getWidth());
	}

	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
			stage.show();
			goToLevel(ENEMY_LEVEL_CLASS_NAME);
	}
	

	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> myClass = Class.forName(className); 
		Constructor<?> constructor = myClass.getConstructor(GameScreen.class); 
		LevelParent myLevel = (LevelParent) constructor.newInstance(game);
		myLevel.addListener(this);
		stage.setScene(myLevel.initializeScene());
		game.loadLevel(myLevel);
		game.startGame();
	
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
		try {		
			goToLevel((String) evt.getNewValue());
			
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
			System.exit(1);
		}
	}

}
