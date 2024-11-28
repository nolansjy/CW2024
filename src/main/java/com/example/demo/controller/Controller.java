package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.GameScreen;
import com.example.demo.LevelOne;
import com.example.demo.LevelParent;

public class Controller implements Observer {

	private static final String LEVEL_ONE_CLASS_NAME = "com.example.demo.LevelOne";
	private final Stage stage;
	private final GameScreen game;

	public Controller(Stage stage) {
		this.stage = stage;
		this.game = new GameScreen(stage.getHeight(), stage.getWidth());
	}

	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
			stage.show();
			startScreen();
			goToLevel(LEVEL_ONE_CLASS_NAME);
	}
	
	private void startScreen() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		game.addObserver(this);
		Scene scene = game.initializeScene();
		stage.setScene(scene);
		game.startGame();
	}

	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> myClass = Class.forName(className); 
		Constructor<?> constructor = myClass.getConstructor(GameScreen.class); 
		LevelParent myLevel = (LevelParent) constructor.newInstance(game);
	    myLevel.addObserver(this); 
	    //Scene scene = myLevel.initializeScene();
	    //stage.setScene(scene); 
	    myLevel.startLevel();
	
	}
	
//	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
//	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		Class<?> myClass = Class.forName(className); 
//		Constructor<?> constructor = myClass.getConstructor(double.class, double.class); 
//		LevelParent myLevel = (LevelParent) constructor.newInstance(stage.getHeight(), stage.getWidth());
//	    myLevel.addObserver(this); 
//	    //Scene scene = myLevel.initializeScene();
//	    //stage.setScene(scene); 
//	    myLevel.startGame();
//	
//	}
	 

	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			goToLevel((String) arg1);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
		}
	}

}
