package com.gather.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class LevelScreen extends BaseScreen {

	private Person girl;
	private BaseActor caldron;
	private boolean atCaldron;
	private boolean win;
	
	//labels
	private Label messageLabel;
	
	//arrows
	private BaseActor arrowCraft;
	private BaseActor arrowCaldron;
	
	//inventory
	protected Inventory inventory;
	
	public void initialize() {
		
		/*
		 * initialized from back to front
		 * Actors are added to the stage automatically when initialized
		 * 
		 */
		
		//labels
		messageLabel = new Label("Gather 3 plants sto craft 1 potion.", BaseGame.labelStyle);
		
		//grassy background
		BaseActor grass = new BaseActor(0, 0, mainStage);
		grass.loadTexture("grass.png");
		grass.setSize(1200, 900);
		
		//world boundary
		BaseActor.setWorldBounds(grass);
		
		//plants
		//make rocks in random places
		for(int i = 0; i < 24; i++) {
			new Plant(MathUtils.random(100, 1100), MathUtils.random(100, 800), mainStage);
		}
		
		//rocks
		//make rocks in random places
		for(int i = 0; i < 8; i++) {
			new Rock(MathUtils.random(100, 1100), MathUtils.random(100, 800), mainStage);
		}
		
		//other objects
		caldron = new BaseActor(500, 500, mainStage);
		caldron.loadTexture("caldron.png");
		caldron.setBoundaryPolygon(8);
		
		//craft button
		TextButton craftButton = new TextButton("Craft", BaseGame.textButtonStyle);
		craftButton.addListener(
						
				(e) -> {
					if(!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown)|| atCaldron == false) {
						return false;
					}
					
					Boolean ingredientsRemoved = false;
					
					if(inventory.check("com.gather.game.Plant", 3)) {//if there are at least 3 plants in inventory
						ingredientsRemoved = inventory.remove("com.gather.game.Plant");//returns true if an item was found and removed
						ingredientsRemoved = inventory.remove("com.gather.game.Plant");//returns true if an item was found and removed
						ingredientsRemoved = inventory.remove("com.gather.game.Plant");//returns true if an item was found and removed
					}
					
					if(ingredientsRemoved == true) {//if the ingredients were removed from the inventory, put a new potion in inventory
						Potion potion = new Potion();
						inventory.put(potion);
					}
							
					return false;
				}
		);
		
		//characters
		girl = new Person(500, 400, mainStage);
		
		//inventory
		inventory = new Inventory();
		
		//conditions
		atCaldron = false;
		win = false;
		
		//uiTable
		uiTable.pad(10);
		uiTable.add(craftButton).top();
		uiTable.add().top().expandX().expandY();
		uiTable.add(inventory).top();
		
		//message
		messageLabel.setPosition(20, 500);
		uiStage.addActor(messageLabel);
		
		//arrows
		arrowCraft = new BaseActor();
		arrowCraft.loadTexture("arrow.png");
		arrowCraft.setPosition(120, 560);
		arrowCraft.addAction(BaseGame.moveArrowHori);
		
		arrowCaldron = new BaseActor();
		arrowCaldron.loadTexture("arrow.png");
		arrowCaldron.setPosition(502.5f, 565);
		arrowCaldron.rotateBy(90);
		arrowCaldron.addAction(BaseGame.moveArrowVert);
	}
	
	public void update(float dt) {
		
		//rocks
		for(BaseActor rockActor : BaseActor.getList(mainStage, "com.gather.game.Rock")) {
			
			//prevent collision
			//girl.preventOverlap(rockActor);
			
			//push rock
			rockActor.preventOverlap(girl);
			
		}
		
		//plants
		for(BaseActor plantActor : BaseActor.getList(mainStage, "com.gather.game.Plant")) {

			Plant plant = (Plant)plantActor;
			
			//girl collects plant
			if(girl.overlaps(plant)) {

				if(!plant.isCollected()) {

					//add to inventory
					if(inventory.put(plant)) {
						plant.clearActions();
						plant.collected = true;
						RustlingLeaves rustlingLeaves = new RustlingLeaves(0,0,mainStage);
						rustlingLeaves.centerAtActor(plant);
					}
					
				}
			} 
		}
		
		//there should be at least 3 plants in the girl's inventory for her to craft a potion
		if(inventory.check("com.gather.game.Plant", 3)) {
			messageLabel.setText("Step on the Caldron \nand click \"Craft\" to make a potion.");
			uiStage.addActor(arrowCraft);
			mainStage.addActor(arrowCaldron);
		}
		
		//win condition instructions
		if(inventory.check("com.gather.game.Potion", 1)) {
			messageLabel.setText("Continue collecting plants \nand making potions until \nyou have 8 red potions in total.");
			arrowCraft.remove();
			arrowCaldron.remove();
		}
		
		//the girl can only craft potions at the caldron
		if(girl.overlaps(caldron)) {
			atCaldron = true;
		} else {
			atCaldron = false;
		}
		
		
		//the girl wins if she has crafted three potions
		if(inventory.check("com.gather.game.Potion", 8) && !win) {
			
			win = true;
			
			BaseActor youWinMessage = new BaseActor(0, 0, uiStage);
			youWinMessage.loadTexture("you-win.png");
			youWinMessage.centerAtPosition(400, 400);
			youWinMessage.setOpacity(0);
			youWinMessage.addAction(Actions.delay(1));
			youWinMessage.addAction(Actions.after(Actions.fadeIn(1)));
		}
		
	}
	
	public boolean keyDown(int keycode) {
		
		//descrete input
		if(keycode == Keys.X) {
			System.out.println("pressed");
		}
		
		return false;
		
	}
	
}
