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

/**
 * Controller manages the game creation and level changes. 
 * Implements PropertyChangeListener instead of deprecated Observer/Observable class
 */
public class Controller implements PropertyChangeListener {

	private static final String ENEMY_LEVEL_CLASS_NAME = "com.example.demo.EnemyLevel";
	private final Stage stage;
	private final GameScreen game;

	/**
	 * Constructs a GameScreen instance which will be used for the remainder of the program.
	 * @param stage JavaFX starting stage
	 */
	public Controller(Stage stage) {
		this.stage = stage;
		this.game = new GameScreen(stage.getHeight(), stage.getWidth());
	}

	/**
	 * Initializes first level, an EnemyLevel
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public void launchGame() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
			stage.show();
			goToLevel(ENEMY_LEVEL_CLASS_NAME);
	}
	

	/**
	 * Constructs a EnemyLevel or BossLevel class based on className. <br>
	 * The GameScreen instance is used to load the level and start it.<br>
	 * Also changed to add PropertyChangeListener instead of Observer/Observable
	 * @param className
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
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

	/**
	 * Changed from Observer/Observable to propertyChangeEvent <br>
	 * System will also exit once an exception is caught and alerted instead of generating alerts endlessly.
	 */
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
