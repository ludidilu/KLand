package game;

import game.battle.Battle;
import game.gameAi.GameAi;
import game.gameQueue.GameQueue;

public class Game {

	public static void init() throws Exception{
		
		GameQueue.init();
		
		GameAi.init();
		
		Battle.init();
	}
}
