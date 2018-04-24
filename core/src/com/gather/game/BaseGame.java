package com.gather.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 *  Created when program is launched; 
 *  manages the screens that appear during the game.
 */
public abstract class BaseGame extends Game {
	
	//ninepatches
	public static NinePatchDrawable defaultBox;
	public static NinePatchDrawable semiTransparentBox;
	
	//label styles
	public static LabelStyle labelStyle;
	
	//text button styles
	public static TextButtonStyle textButtonStyle;
	
	//animation
	public static Action moveArrowHori = Actions.forever(
			Actions.sequence(
				(Actions.moveBy(10, 0, 0.2f)),
				(Actions.moveBy(-20, 0, 0.2f)),
				(Actions.moveBy(10, 0, 0.2f))
			)
		);
	
	public static Action moveArrowVert = Actions.forever(
			Actions.sequence(
				(Actions.moveBy(0, 10, 0.2f)),
				(Actions.moveBy(0, -20, 0.2f)),
				(Actions.moveBy(0, 10, 0.2f))
			)
		);
	
	@Override
   	public void create() {
		
		//NinePatchDrawables
		
		//default
		Texture box = new Texture(Gdx.files.internal("button.png"));
		NinePatch boxNinePatch = new NinePatch(box, 8, 8, 8, 9);
		defaultBox = new NinePatchDrawable(boxNinePatch);
		
		//semi transparent
		Texture stbox = new Texture(Gdx.files.internal("boxSemiTransparent.png"));
		NinePatch stboxNinePatch = new NinePatch(stbox, 12, 12, 12, 13);
		semiTransparentBox = new NinePatchDrawable(stboxNinePatch);
		
   		// prepare for multiple classes/stages to recieve discrete input
       	InputMultiplexer im = new InputMultiplexer();
       	Gdx.input.setInputProcessor(im);
       	
       	//labels
       	labelStyle = new LabelStyle();
       	labelStyle.font = new BitmapFont(Gdx.files.internal("cavestory.fnt"));
       	
       	//text buttons
       	textButtonStyle = new TextButtonStyle();
       	textButtonStyle.up = defaultBox;
       	textButtonStyle.font = labelStyle.font;
       	textButtonStyle.fontColor = Color.GRAY;
   		
   	}
	
    /**
     *  Stores reference to game; used when calling setActiveScreen method.
     */
    private static BaseGame game;
    

    /**
     *  Called when game is initialized; stores global reference to game object.
     */
    public BaseGame() 
    {        
        game = this;
    }
    
    /**
     *  Used to switch screens while game is running.
     *  Method is static to simplify usage.
     */
    public static void setActiveScreen(BaseScreen s)
    {
        game.setScreen(s);
    }
}
