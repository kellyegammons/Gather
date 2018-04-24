package com.gather.game;

public class GatherGame extends BaseGame {
	
	public void create() {
		super.create();
		
		setActiveScreen(new MenuScreen());
	}

}
