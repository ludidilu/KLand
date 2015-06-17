package game.gameQueue;

import game.battle.Battle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import publicTools.PublicTools;
import data.dataCsv.battle.Csv_battle;
import superService.SuperService;
import timer.TimerService;
import userService.UserService;

public class GameQueue extends SuperService{

	private static GameQueue instance;
	
	public static GameQueue getInstance(){
		
		if(instance == null){
			
			instance = new GameQueue();
		}
		
		return instance;
	}
	
	private static Map<String, Method> methodMap;
	
	public static void init() throws Exception{
		
		methodMap = new HashMap<String, Method>();
		
		methodMap.put("enterQueue", GameQueue.class.getDeclaredMethod("enterQueue", UserService.class));
		methodMap.put("quitQueue", GameQueue.class.getDeclaredMethod("quitQueue", UserService.class));
		methodMap.put("checkQueue", GameQueue.class.getDeclaredMethod("checkQueue"));
		methodMap.put("quitQueueWhenDisconnect", GameQueue.class.getDeclaredMethod("quitQueueWhenDisconnect", UserService.class));
		methodMap.put("battleOver", GameQueue.class.getDeclaredMethod("battleOver", Battle.class));
	}
	
	protected Map<String, Method> getMethodMap(){
		
		return methodMap;
	}
	
	private ArrayList<UserService> waittingList;
	private ArrayList<Battle> battleList;
	
	private static int BATTLE_NUM = 1;
	private static int TIME_DELAY = 1000;
	
	public GameQueue(){
		
		waittingList = new ArrayList<>();
		
		battleList = new ArrayList<>();
		
		for(int i = 0 ; i < BATTLE_NUM ; i++){
			
			battleList.add(new Battle());
		}
		
		TimerService.getInstance().addTimerCallBack(this, TIME_DELAY, "checkQueue");
	}
	
	public void enterQueue(UserService _userService){
		
		if(!waittingList.contains(_userService)){
		
			waittingList.add(_userService);
			
			_userService.process("enterQueueOK", true);
			
		}else{
			
			_userService.process("enterQueueOK", false);
		}
	}
	
	public void quitQueue(UserService _userService){
		
		if(waittingList.contains(_userService)){
			
			waittingList.remove(_userService);
			
			_userService.process("quitQueueOK", true);
			
		}else{
			
			_userService.process("quitQueueOK", false);
		}
	}
	
	public void quitQueueWhenDisconnect(UserService _userService){
		
		if(waittingList.contains(_userService)){
			
			waittingList.remove(_userService);
			
			_userService.process("quitQueueWhenDisconnectOK", true);
			
		}else{
			
			_userService.process("quitQueueWhenDisconnectOK", false);
		}
	}
	
	public void checkQueue(){
		
		if(waittingList.size() > 1){
			
			Iterator<UserService> iter = waittingList.iterator();
			
			while(iter.hasNext() && !battleList.isEmpty()){
				
				UserService service1 = iter.next();
				
				if(iter.hasNext()){
					
					iter.remove();
					
					UserService service2 = iter.next();
					
					iter.remove();
					
					Battle battle = battleList.remove(0);
					
					battle.process("init", getBattleID(), service1, service2, -1);
				}
			}
		}
	}
	
	public void battleOver(Battle _battle){
		
		battleList.add(_battle);
	}
	
	private int getBattleID(){
		
		HashMap<Integer, Csv_battle> tmpMap = PublicTools.getSomeOfMap(Csv_battle.dic, 1);
		
		Iterator<Integer> iter = tmpMap.keySet().iterator();
		
		int battleID = iter.next();
		
		return battleID;
	}
}
