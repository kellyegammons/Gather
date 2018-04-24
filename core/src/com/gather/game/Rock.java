package com.gather.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Rock extends BaseActor {
	
	public Rock(float x, float y, Stage s) {
		super(x,y,s);
		
		//animation
		loadTexture("rock.png");
		
		//collision
		setBoundaryPolygon(8);
		
		//motion
		setSpeed(100);
		
	}
}
