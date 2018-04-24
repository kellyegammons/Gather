package com.gather.game;

public class Potion extends BaseActor {
	
	public Potion() {
		
		loadTexture("potion.png");
		
		//collision
		setBoundaryPolygon(8);
		
	}

}
