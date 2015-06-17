package game.gameAi;

import game.battle.Battle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import publicTools.PublicTools;
import superService.SuperService;
import userService.UserService;
import data.dataCsv.battle.Csv_battleAi;

public class GameAi  extends SuperService{

	private static GameAi instance;
	
	public static GameAi getInstance(){
		
		if(instance == null){
			
			instance = new GameAi();
		}
		
		return instance;
	}
	
	private static Map<String, Method> methodMap;
	
	public static void init() throws Exception{
		
		methodMap = new HashMap<String, Method>();
		
		methodMap.put("fightAi", GameAi.class.getDeclaredMethod("fightAi", UserService.class));
		methodMap.put("quitGameAi", GameAi.class.getDeclaredMethod("quitGameAi", UserService.class));
		methodMap.put("quitGameAiWhenDisconnect", GameAi.class.getDeclaredMethod("quitGameAiWhenDisconnect", UserService.class));
		methodMap.put("battleOver", GameAi.class.getDeclaredMethod("battleOver", Battle.class));
	}
	
	protected Map<String, Method> getMethodMap(){
		
		return methodMap;
	}
	
	private ArrayList<UserService> waittingList;
	private ArrayList<Battle> battleList;
	
	private static int BATTLE_NUM = 1;
	
	public GameAi(){
		
		waittingList = new ArrayList<>();
		
		battleList = new ArrayList<>();
		
		for(int i = 0 ; i < BATTLE_NUM ; i++){
			
			battleList.add(new Battle());
		}
	}
	
	public void fightAi(UserService _userServie){
		
		_userServie.process("fightAiOK", true);
		
		if(!battleList.isEmpty()){
			
			Battle battle = battleList.remove(0);
			
			Csv_battleAi battleAi = getBattleAi();
			
			int ai = getAi(battleAi);
			
			battle.process("init", battleAi.id, _userServie, null, ai);
			
		}else{
			
			waittingList.add(_userServie);
		}
	}
	
	public void quitGameAi(UserService _userService){
		
		if(waittingList.contains(_userService)){
			
			waittingList.remove(_userService);
			
			_userService.process("quitGameAiOK", true);
			
		}else{
			
			_userService.process("quitGameAiOK", false);
		}
	}
	
	public void quitGameAiWhenDisconnect(UserService _userService){
		
		if(waittingList.contains(_userService)){
			
			waittingList.remove(_userService);
			
			_userService.process("quitGameAiWhenDisconnectOK", true);
			
		}else{
			
			_userService.process("quitGameAiWhenDisconnectOK", false);
		}
	}
	
	public void battleOver(Battle _battle){
		
		if(!waittingList.isEmpty()){
			
			UserService userService = waittingList.remove(0);
			
			Csv_battleAi battleAi = getBattleAi();
			
			int ai = getAi(battleAi);
			
			_battle.process("init", battleAi.id, userService, null, ai);
			
		}else{
			
			battleList.add(_battle);
		}
	}
	
	private Csv_battleAi getBattleAi(){
		
		HashMap<Integer, Csv_battleAi> tmpMap = PublicTools.getSomeOfMap(Csv_battleAi.dic, 1);
		
		Iterator<Csv_battleAi> iter = tmpMap.values().iterator();
		
		Csv_battleAi battleAi = iter.next();
		
		return battleAi;
	}
	
	private int getAi(Csv_battleAi _battleAi){
		
		int rand = (int)Math.random() * _battleAi.ai.length;
		
		return _battleAi.ai[rand];
	}
}
