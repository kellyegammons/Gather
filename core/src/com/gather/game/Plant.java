package com.gather.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Plant extends BaseActor {
	
	private Animation<TextureRegion> animPicked;
	public boolean collected;
	
	public Plant(float x, float y, Stage s) {
		super(x,y,s);
		
		collected = false;
		
		loadTexture("dandylion.png");
		
		//animation
		
		//moves up and down forever
		Action bob = Actions.forever(
					Actions.sequence(
						(Actions.scaleTo(2.25f, 2.25f, 2.5f)),
						(Actions.scaleTo(2, 2, 2.5f)),
						(Actions.scaleTo(2.25f, 2.25f, 2.5f))
					)
				);
		this.addAction(bob);
		
		//picked animation
		animPicked = loadAnimationFromSheet("picked.png", 2, 3, 0.1f, false);

		
		//collision
		setBoundaryPolygon(8);
		
	}
	
	//check if the plant is collected
	public boolean isCollected() {
		return collected;
	}
	
	//do something with this
	public void collect(Stage s) {
		//if(this.collected != true) {
			//clearActions();
			//this.setAnimation(animPicked);
			//addAction(Actions.fadeOut(1));
			//addAction(Actions.after(Actions.removeActor()));
			//RustlingLeaves rustlingLeaves = new RustlingLeaves(0,0,mainStage);
			//rustlingLeaves.centerAtActor(plant)
		//}
		// = true;
	}

}
