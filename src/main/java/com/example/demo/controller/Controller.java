package com.example.demo.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.example.demo.GameScreen;
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
			goToLevel(LEVEL_ONE_CLASS_NAME);
	}
	

	private void goToLevel(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> myClass = Class.forName(className); 
		Constructor<?> constructor = myClass.getConstructor(GameScreen.class); 
		LevelParent myLevel = (LevelParent) constructor.newInstance(game);
		myLevel.addObserver(this);
		stage.setScene(myLevel.initializeScene());
		game.loadLevel(myLevel);
		game.startGame();
	
	}
	 

	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			goToLevel((String) arg1);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e.getClass().toString());
			alert.show();
			System.exit(1);
		}
	}

}
