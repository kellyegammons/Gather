package com.gather.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.gather.game.GatherGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		//LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//new LwjglApplication(new GatherDefault(), config);
		
		Game myGame = new GatherGame();
		LwjglApplication launcher = new LwjglApplication(myGame, "Gather Alpha", 800,600);
	}
}
