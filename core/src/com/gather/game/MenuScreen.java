package com.gather.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class MenuScreen extends BaseScreen {

	
	public void initialize() {
		
		//background
		BaseActor grass = new BaseActor(0, 0, mainStage);
		grass.loadTexture("grass.png");
		grass.setSize(800, 600);
		
		//title
		BaseActor title = new BaseActor(0, 0, mainStage);
		title.loadTexture("gather.png");
		
		//start
		TextButton startButton = new TextButton("Start", BaseGame.textButtonStyle);
		startButton.addListener(
				
				(e) -> {
					if(!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
						return false;
					}
					
					GatherGame.setActiveScreen(new LevelScreen());
					return false;
				}
				);
		
		//quit
		TextButton quitButton = new TextButton("Quit", BaseGame.textButtonStyle);
		quitButton.addListener(
				
				(e) -> {
					if(!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)) {
						return false;
					}
					
					Gdx.app.exit();
					return false;
				}
				);
		
		//uiTable
		uiTable.add(title).colspan(2).padBottom(50);
		uiTable.row();
		uiTable.add(startButton);
		uiTable.add(quitButton);
		
		
	}
	
	public void update(float dt) {
		if(Gdx.input.isKeyPressed(Keys.S)) {
			GatherGame.setActiveScreen(new LevelScreen());
		}
	}
	
	//keyboard
	public boolean keyDown(int keyCode) {
		if(Gdx.input.isKeyPressed(Keys.ENTER)) {
			GatherGame.setActiveScreen(new LevelScreen());
			
		}
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		
		return false;
	}
}
