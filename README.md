# ReadMe
## How To Compile

**GitHub Repo:** https://github.com/nolansjy/CW2024  
**Git URI:** https://github.com/nolansjy/CW2024.git

**Using Git Repo**
1. Open Eclipse IDE Workspace
2. Import > Git > Project from Git > Clone URI (Paste link) > Next/Finish wizard
3. When project is loaded, Run > Run As Java Application  
4. If prompted, select com.example.demo to run

**Using Source File**
1. Unzip source file 
2. Open Eclipse IDE Workspace
2. Import > General > Projects from Folder or Archive > Directory... (Select source directory) > Next/Finish wizard
3. When project is loaded, Run > Run as Java Application
4. If prompted, select com.example.demo to run

**Note:**  You may see exceptions in the console while the application is compiling, but these are related to the unit tests and do not indicate actual compilation errors. See below for explanation. 

## Implemented And Working Correctly

### Game Development Features
- Game object builder: A Builder pattern is used in classes for game objects. It is easier to create customized game objects in future development
- Level and stage design: Using JSON files, specific levels and stages can be configured (i.e. levels with different backgrounds, enemy spawn rate, etc)

**Note:** Level refers to all level instances while stage refers to the specific preset configuration of a level as seen in the JSON file.

### Game Object Features
- Hitboxes: Game objects now have hitboxes and do not use the dimensions of their image file as a boundary
- Damage animation: Planes will be shaded when they take damage
- User movement: User's Plane can now move horizontally, originally only vertically
- Boss shield: Boss shield is now a visible circle surrounding it, previously invisible

### Gameplay Features
- Enemy and Boss levels: Game alternates between enemy and boss levels
- Endless levels: Game will continue creating levels until player chooses to end the game
- 3-stage sequence: 3 stages have been implemented, each composed of an enemy level followed by a boss level. Player must defeat all 3 before being prompted to continue or end the game. The difficulty is raised after each round. 
- Level alert: Levels can play a message when it starts. Specifically, enemy levels will start with their stage title and difficulty (e.g. Clear Skies 1)
- Difficulty scaling: Levels now scale with a difficulty modifier. e.g. The number of enemies in a level increase by 1 with each difficulty

## Implemented and Not Working Correctly
- Boss Healthbar: While the healthbar displays correctly in the first round of stages, the width of the healthbar exceeds the set width in subsequent rounds. This issue was thought to be solved but reappeared during the end of development, so it was ultimately not fixed.  
-  Unit tests: While the unit tests are able to be run, they throw an exception in the console as mentioned above. This is likely due to improper configuration of the TestFX library which allows JavaFX testing. Since the unit tests could still run, this problem was not prioritized
-  Appropriate difficulty scaling: While difficulty scaling has been correctly implemented, the current configuration does not provide an enjoyable player experience as it becomes too difficult. More time was needed to determine the exact number scaling that will provide a challenging but enjoyable experience

## Not Implemented

- User scaling: Ideally the player also receives level-ups such as increased health or temporary immunity as the difficulty increases. More focus was placed on level design so this was not implemented
- Shield design: The current shield implementation is boring to play against. A feature such as breaking a shield should have been implemented, but this was not done due to aforementioned focus on level design. 
- Full code coverage: Unit tests were only written during the development of new features and not for the existing features. During development there was difficulty integrating JavaFX with JUnit using TestFX, which finally resulted in the existing problems with the current unit tests, so more tests were not written. 

## New Java Classes
- GameScreen: This class represents the main game instance that is passed to all levels. It now controls the Timeline and thus the creation of new levels. Levels will get shared information from it such as the current difficulty as well as update information such as the current background.
- SpriteBuilder: The is the builder for the Sprite (former ActiveActor). It is a StackPane that contains the image node, size and position of a Sprite.
- SpriteHitboxBuilder: This is the builder for the SpriteDestructible. It creates the Hitbox rectangle and adds it as the first child of the StackPane. It is descended from SpriteBuilder and all subsequent Builders descend from this. 
- FighterPlaneBuilder: This builds the FighterPlane. It adds a health and damageTaken value. 
  - UserPlaneBuilder descends directly from it
  - EnemyPlaneBuilder adds a fireRate value
  - BossPlaneBuilder further adds a shieldProbability and maxShieldFrames value
- ProjectileBuilder: Builds the Projectile. UserProjectile, EnemyProjectile and BossProjectile descend without further additions. 

**Note:** All Builder classes are nested inside the class they are named after.

## Modified Java Classes

- LevelParent: Only takes GameScreen in the constructor. It still contains the main updateScene() methods, but the Timeline-related methods were taken away and placed in GameScreen as GameScreen now controls the main Timeline.
- LevelOne/**EnemyLevel**: Renamed. Level configuration such as totalEnemies are now retrieved from the correct stage in the enemyLevel.json file and then the difficulty modifier is applied. 
- LevelTwo/**BossLevel**: Renamed. Level configuration such as bossHealth are now retrieved from the correct stage in the bossLevel.json file and then the difficulty modifier is applied. 
- LevelViewLevelTwo/**BossLevelView**: Renamed. Adds a boss healthbar. 
- UserPlane: Can now move horizontally using right and left keys. 
- EnemyPlane: EnemyPlane information is configured in EnemyLevel, which then uses the EnemyPlaneBuilder to create EnemyPlanes.
- Boss/**BossPlane**: Renamed. Implements the shield as a Circle that covers it. Boss information such as health are first configured in the BossLevel as described above, before the Boss object is created 
- ActiveActor/**Sprite**: Renamed. Now implements StackPane and holds a ImageView node
- ActiveActorDestructible/**SpriteDestructible**: Renamed. Destructible interface methods are simply contained there after Destructible was removed. 
- Destructible: Deleted this class interface because it was only used in SpriteDestructible
- ShieldImage, GameOverImage, WinImage: Deleted. Shield implementation was changed, and a class that only holds an image was deemed unnecessary. The GameOver image and WinImage are now constructed in the GameScreen. 
- Controller: Now uses the PropertyChangeListener system instead of Observer/Observable which is deprecated. Works with the new GameScreen and Level creation system. Also stops the application when an exception is caught and alerted.


## Unexpected Problems
- Maven Dependency management: Difficulty with adding new modules. A long troubleshooting and trial-and-error process took place before it was finally resolved. 
- Eclipse Javadoc Generation: Eclipse could not be used to generate Javadoc due to what seems to be a to be a [known issue](https://bugs.eclipse.org/bugs/show_bug.cgi?id=543405) with Eclipse. Maven is used to generate the Javadoc instead using the Maven Javadoc Plugin. Using the Run As configuration: Maven clean, Maven install, then a custom Run Configuration that contains the javadoc:javadoc goal. 
