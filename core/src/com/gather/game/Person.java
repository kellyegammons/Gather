package com.gather.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Person extends BaseActor {
	
	private Animation<TextureRegion> animWalkDown, animWalkUp, animWalkLeft, animWalkRight;

	public Person(float x, float y, Stage s) {//todo: change this sprite to something npc-ish
		super(x,y,s);
		
		//animation
		//default animation: walking forward
		String[] filesWalkDown = {"blonde-01.png", "blonde-02.png", "blonde-01.png", "blonde-03.png"};
		animWalkDown = loadAnimationFromFiles(filesWalkDown, 0.1f, true);
		
		/*
		 * set other walking animations
		 */
		//down
		String[] filesWalkUp = {"blonde-04.png", "blonde-05.png", "blonde-04.png", "blonde-06.png"};
		animWalkUp = loadAnimationFromFiles(filesWalkUp, 0.1f, true);
		//left
		String[] filesWalkLeft = {"blonde-10.png", "blonde-11.png", "blonde-10.png", "blonde-12.png"};
		animWalkLeft = loadAnimationFromFiles(filesWalkLeft, 0.1f, true);
		//right
		String[] filesWalkRight = {"blonde-07.png", "blonde-08.png", "blonde-07.png", "blonde-09.png"};
		animWalkRight = loadAnimationFromFiles(filesWalkRight, 0.1f, true);
		
		
		//movement
		setAcceleration(800);
		setDeceleration(800);
		setMaxSpeed(200);
		
		//collision
		setBoundaryPolygon(8);
		

	}
	
	public void act(float dt) {
		super.act(dt);
		
		//directional walking
		//move and change animation after key press

		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			this.setAnimation(animWalkLeft);
			accelerateAtAngle(180);
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			this.setAnimation(animWalkRight);
			accelerateAtAngle(0);
		}
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			this.setAnimation(animWalkUp);
			accelerateAtAngle(90);
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			this.setAnimation(animWalkDown);
			accelerateAtAngle(270);
		}
		
		//physics
		applyPhysics(dt);
		
		//pause animation when not moving
		setAnimationPaused(!isMoving());
		
		//prevent from exiting world boundary
		boundToWorld();
		
		//keep camera aligned with person
		alignCamera();
		
		
	}
}
