package userService;

import game.battle.Battle;
import game.gameAi.GameAi;
import game.gameQueue.GameQueue;
import server.Server_thread;
import server.Server_thread_service;
import data.dataDB.user.DB_user_unit;


public class UserService extends Server_thread_service{

	public static void init() throws Exception{
		
		Server_thread_service.methodMap.put("sendMsg",UserService.class.getDeclaredMethod("sendMsg",String.class));
		
		Server_thread_service.methodMap.put("enterQueueOK",UserService.class.getDeclaredMethod("enterQueueOK",boolean.class));
		Server_thread_service.methodMap.put("quitQueueOK",UserService.class.getDeclaredMethod("quitQueueOK",boolean.class));
		Server_thread_service.methodMap.put("quitQueueWhenDisconnectOK",UserService.class.getDeclaredMethod("quitQueueWhenDisconnectOK",boolean.class));
		Server_thread_service.methodMap.put("enterBattle",UserService.class.getDeclaredMethod("enterBattle",Battle.class));
		
		Server_thread_service.methodMap.put("getBattleDataOK", UserService.class.getDeclaredMethod("getBattleDataOK", boolean.class,int.class,int.class,int.class,int[].class,int[][].class,int.class,int.class,int.class,int[][].class,boolean.class));
		Server_thread_service.methodMap.put("sendBattleActionOK", UserService.class.getDeclaredMethod("sendBattleActionOK", int[][].class,int[][].class,int[][][].class,int[][][].class,int.class,int.class));
		Server_thread_service.methodMap.put("playBattle", UserService.class.getDeclaredMethod("playBattle", int[][].class,int[][].class,int[][][].class,int[][][].class,int.class,int.class));
		Server_thread_service.methodMap.put("fightAiOK", UserService.class.getDeclaredMethod("fightAiOK", boolean.class));
		Server_thread_service.methodMap.put("quitGameAiOK",UserService.class.getDeclaredMethod("quitGameAiOK",boolean.class));
		Server_thread_service.methodMap.put("quitGameAiWhenDisconnectOK",UserService.class.getDeclaredMethod("quitGameAiWhenDisconnectOK",boolean.class));
		Server_thread_service.methodMap.put("quitBattleOK", UserService.class.getDeclaredMethod("quitBattleOK",boolean.class));
		Server_thread_service.methodMap.put("leaveBattle", UserService.class.getDeclaredMethod("leaveBattle",int.class));
	}
	
//	public PlayerData userData;
	
	private Battle battle;
	
	private boolean isInGameQueue;
	
	private boolean isInGameAiQueue;
	
	public UserService(DB_user_unit _user){
		
		super(_user);
	}
	
	public void kick(Server_thread _thread) throws Exception{
		
		if(isInGameQueue){
			
			addProcessingNum();
			quitQueueWhenDisconnect();
			
		}else if(isInGameAiQueue){
			
			addProcessingNum();
			quitGameAiWhenDisconnect();
		}
		
		super.kick(_thread);
	}
	
	public void disconnect(){
		
		if(isInGameQueue){
			
			addProcessingNum();
			quitQueueWhenDisconnect();
			
		}else if(isInGameAiQueue){
			
			addProcessingNum();
			quitGameAiWhenDisconnect();
		}
		
		super.disconnect();
	}
	
	public void sendData(int _id,Object... objVec) throws Exception{
		
		String syncData = user.userData.getSyncData();
		
		if(syncData != null){
			
			super.sendData(1000, syncData);
		}
		
		super.sendData(_id, objVec);
	}
	
	public void sendMsg(String _str) throws Exception{
		
		sendData(999, _str);
	}
	
	public void getUserData() throws Exception{
		
		sendData(3, battle != null, user.userData.getAllData());
	}
	
	public void enterQueue() throws Exception{
		
		if(battle == null && !isInGameQueue && !isInGameAiQueue){
		
			GameQueue.getInstance().process("enterQueue", this);
			
		}else{
			
			sendData(5, false);
		}
	}
	
	public void enterQueueOK(boolean _result) throws Exception{
		
		isInGameQueue = true;
		
		sendData(5, _result);
	}
	
	public void quitQueue() throws Exception{
		
		if(isInGameQueue){
		
			GameQueue.getInstance().process("quitQueue", this);
			
		}else{
			
			sendData(7, false);
		}
	}
	
	public void quitQueueOK(boolean _result) throws Exception{
		
		isInGameQueue = false;
		
		sendData(7, _result);
	}
	
	private void quitQueueWhenDisconnect(){
		
		GameQueue.getInstance().process("quitQueueWhenDisconnect", this);
	}
	
	public void quitQueueWhenDisconnectOK(boolean _result) throws Exception{
		
		isInGameQueue = false;
		
		delProcessingNum();
	}
	
	public void enterBattle(Battle _battle) throws Exception{
		
		if(isInGameQueue){
		
			isInGameQueue = false;
			
		}else if(isInGameAiQueue){
			
			isInGameAiQueue = false;
		}
		
		battle = _battle;
		
		sendData(8);
	}
	
	public void getBattleData(){
		
		if(battle != null){
			
			battle.process("getBattleData", this);
		}
	}
	
	public void getBattleDataOK(boolean _isHost,int _nowRound,int _maxRound,int _mapID,int[] _mapData,int[][] _myCards,int _oppCardsNum,int _userAllCardsNum1,int _userAllCardsNum2,int[][] _heroData,boolean _actionState) throws Exception{
		
		sendData(10, _isHost,_nowRound,_maxRound,_mapID, _mapData, _myCards, _oppCardsNum, _userAllCardsNum1, _userAllCardsNum2, _heroData,_actionState);
	}
	
	public void sendBattleAction(int[][] _moveData, int[][] _summonData){
		
		if(battle != null){
			
			battle.process("sendBattleAction", this, _moveData, _summonData);
		}
	}
	
	public void sendBattleActionOK(int[][] _summonData,int[][] _moveData,int[][][] _skillData, int[][][] _attackData,int _cardUid,int _cardID) throws Exception{
		
		sendData(12, _summonData, _moveData, _skillData, _attackData, _cardUid, _cardID);
	}
	
	public void playBattle(int[][] _summonData,int[][] _moveData,int[][][] _skillData, int[][][] _attackData,int _cardUid,int _cardID) throws Exception{
		
		sendData(13, _summonData, _moveData, _skillData, _attackData, _cardUid, _cardID);
	}
	
	public void fightAi() throws Exception{
		
		if(battle == null && !isInGameQueue && !isInGameAiQueue){
			
			GameAi.getInstance().process("fightAi", this);
			
		}else{
			
			sendData(15, false);
		}
	}
	
	public void fightAiOK(boolean _result) throws Exception{
		
		sendData(15, _result);
	}
	
	public void quitGameAi() throws Exception{
		
		if(isInGameAiQueue){
		
			GameAi.getInstance().process("quitGameAi", this);
			
		}else{
			
			sendData(17, false);
		}
	}
	
	public void quitGameAiOK(boolean _result) throws Exception{
		
		isInGameQueue = false;
		
		sendData(17, _result);
	}

	private void quitGameAiWhenDisconnect(){
		
		GameAi.getInstance().process("quitGameAiWhenDisconnect", this);
	}
	
	public void quitGameAiWhenDisconnectOK(boolean _result) throws Exception{
		
		isInGameAiQueue = false;
		
		delProcessingNum();
	}
	
	public void quitBattle() throws Exception{
		
		if(battle != null){
			
			battle.process("quitBattle", this);
			
		}else{
			
			sendData(19, false);
		}
	}
	
	public void quitBattleOK(boolean _result) throws Exception{
		
		if(_result){
			
			battle = null;
		}
		
		sendData(19, _result);
	}
	
	public void leaveBattle(int _result) throws Exception{
		
		battle = null;
		
		sendData(20, _result);
	}
}
