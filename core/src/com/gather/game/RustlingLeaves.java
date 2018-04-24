package com.gather.game;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class RustlingLeaves extends BaseActor {
	public RustlingLeaves(float x, float y, Stage s) {
		super(x,y,s);
		
		loadAnimationFromSheet("picked.png", 2, 3, 0.1f, false);//default animation
		
	}
	
	public void act(float dt) {
		super.act(dt);
		
		/*
		 * This whirlpool animates once and then disappears
		 * 
		 */
		if(isAnimationFinished()) {
			remove();
		}
	}
}
