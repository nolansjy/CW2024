package com.example.demo;

/**
 * @deprecated Not used since only SpriteDestructible used it
 */
public interface Destructible {
	
	/**
	 * How an object takes damage.
	 */
	void takeDamage();

	
	/**
	 * How an object is destroyed.
	 */
	void destroy();
	
}
