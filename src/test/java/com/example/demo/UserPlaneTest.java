package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserPlaneTest {

	UserPlane user;

	@BeforeEach
	void setUp() throws Exception {
		user = new UserPlane.UserPlaneBuilder().setHealth(5).load().build();
	}

	@Test
	void testMoveUp() {
		double initialY = user.getLayoutY();
		user.moveUp();
		user.updatePosition();
		assertTrue(initialY > (user.getLayoutY()+user.getTranslateY()));
	}
	
	@Test
	void testMoveDown() {
		double initialY = user.getLayoutY();
		user.moveDown();
		user.updatePosition();
		assertTrue(initialY < (user.getLayoutY()+user.getTranslateY()));
	}
	
	@Test
	void testMoveFront() {
		double initialX = user.getLayoutX();
		user.moveFront();
		user.updatePosition();
		assertTrue(initialX < user.getLayoutX()+user.getTranslateX());
	}
	
	@Test
	void testMoveBack() {
		double initialX = user.getLayoutX()+16;
		user.moveBack();
		user.updatePosition();
		assertTrue(initialX > user.getLayoutX()+user.getTranslateX());
	}

}
