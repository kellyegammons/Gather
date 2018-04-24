package com.gather.game;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class Inventory extends Table {
	
	public Inventory() {
		
		pad(10);
		
		for(int i = 0; i < 5 ; i++) {//add 5 rows of 3 cells
			add().expand().center().fill().pad(20, 10, 20, 10).width(34).height(34);
			add().expand().center().fill().pad(20, 10, 20, 10).width(34).height(34);
			add().expand().center().fill().pad(20, 10, 20, 10).width(34).height(34);
			row();
		}
		
		setBackground(BaseGame.semiTransparentBox);

	}
	
	public boolean put(Actor item) {
		
		Boolean status = false;
		
		Array<Cell> cells = getCells();
		
		//loop through the cells in the table
		for( Cell<?> cell : cells) {
			
			//the first cell that returns null...
			if(cell.getActor() == null) {
				
				cell.setActor(item);
				status = true;
				break;
			}
			
		}
		
		return status;
	}
	
	public boolean check(String className, int amount) {
		
		int found = 0;
		
		Boolean status = false;
		
		Array<Cell> cells = getCells();
		
		//the class
		@SuppressWarnings("rawtypes")
		Class theClass = null;
		
		//try to get the name from the string
		//exception if the class does not exist
		try {
			theClass = Class.forName(className);
		} catch(Exception error) {
			error.printStackTrace();
		}
		
			
		for( Cell<?> cell : cells) {
				
			//the first cell that returns null...
			if(cell.getActor() != null && theClass.isInstance(cell.getActor())) {
							
				found++;
				
				if(found == amount) {
					status = true;
					break;
				}
			}
						
		}

		return status;
		
	}
	
	public boolean remove(String className) {

		Boolean status = false;
		
		Array<Cell> cells = getCells();
		
		//the class
		@SuppressWarnings("rawtypes")
		Class theClass = null;
		
		//try to get the name from the string
		//exception if the class does not exist
		try {
			theClass = Class.forName(className);
		} catch(Exception error) {
			error.printStackTrace();
		}
		
		//loop through the cells in the table
		for( Cell<?> cell : cells) {
					
			//the first cell that returns null...
			if(cell.getActor() != null && theClass.isInstance(cell.getActor())) {
						
				cell.clearActor();
				status = true;
				break;
			}
					
		}
		
		return status;
	}

	@Override
	public void add(Actor... actors) {
		// TODO Auto-generated method stub
		super.add(actors);	
		
	}

	@Override
	public boolean removeActor(Actor actor) {
		// TODO Auto-generated method stub
		return super.removeActor(actor);
	}

	@Override
	public Array<Cell> getCells() {
		// TODO Auto-generated method stub
		return super.getCells();
	}
	
}
