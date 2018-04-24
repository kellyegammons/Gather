package com.gather.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class BaseScreen implements Screen, InputProcessor {
	
	protected Stage mainStage;
	protected Stage uiStage;
	
	protected Table uiTable;
	
	public BaseScreen() {
		mainStage = new Stage();
		uiStage = new Stage();
		
		uiTable = new Table();
		
		uiTable.setFillParent(true);
		
		uiStage.addActor(uiTable);
		
		initialize();
	}
	
	public abstract void initialize();
	
	public abstract void update(float dt);
	
	public void render(float dt) {
		
		//act
		uiStage.act(dt);
		mainStage.act(dt);
		
		//update
		update(dt);
		
		//clear
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//draw
		mainStage.draw();
		uiStage.draw();
	}

	/*
	 * input proccessor overrides
	 * 
	 */
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	
	/*
	 * screen overrides
	 * 
	 */
	@Override
	public void show() {
		
		// add screen and stages to the InputMultiplexer
		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
		im.addProcessor(this);
		im.addProcessor(uiStage);
		im.addProcessor(mainStage);
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		
		// remove screen and stages to the InputMultiplexer
		InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
		im.removeProcessor(this);
		im.removeProcessor(uiStage);
		im.removeProcessor(mainStage);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
