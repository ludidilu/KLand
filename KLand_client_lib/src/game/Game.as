package game
{
	import flash.display.Stage;
	
	import game.battle.Battle;
	import game.gameQueue.GameQueuePanel;
	import game.login.LoginPanel;
	
	import publicTools.connect.Connect_handle;
	
	import starling.core.Starling;

	public class Game
	{
		private static var stage:Stage;
		private static var loginPanel:LoginPanel;
		private static var gameQueuePanel:GameQueuePanel;
		private static var battle:Battle;
		
		public function Game()
		{
		}
		
		public static function init(_stage:Stage):void{
			
			stage = _stage;
		}
		
		public static function start():void{
			
			loginPanel = new LoginPanel;
			gameQueuePanel = new GameQueuePanel;
			battle = new Battle;
			
			stage.addChild(loginPanel);
		}
		
		public static function loginOK(_boolean:Boolean):void{
			
			if(_boolean){
				
				Connect_handle.sendData(2);
				
			}else{
				
				loginPanel.loginFail();
			}
		}
		
		public static function getUserData(_isInBattle:Boolean,_heros:Vector.<int>):void{
			
			stage.removeChild(loginPanel);
			
			if(!_isInBattle){
			
				stage.addChild(gameQueuePanel);
				
			}else{
				
				enterBattle();
			}
		}
		
		public static function enterQueueOK(_b:Boolean):void{
			
			gameQueuePanel.enterQueueOK(_b);
		}
		
		public static function quitQueueOK(_b:Boolean):void{
			
			gameQueuePanel.quitQueueOK(_b);
		}
		
		public static function fightAiOK(_b:Boolean):void{
			
			gameQueuePanel.fightAiOK(_b);
		}
		
		public static function quitAiOK(_b:Boolean):void{
			
			gameQueuePanel.quitAiOK(_b);
		}
		
		public static function enterBattle():void{
			
			if(gameQueuePanel.parent != null){
				
				gameQueuePanel.reset();
				
				stage.removeChild(gameQueuePanel);
			}
			
			Connect_handle.sendData(9);
		}
		
		public static function getBattleData(_isHost:Boolean,_nowRound:int,_maxRound:int,_mapID:int,_mapData:Vector.<int>,_myCards:Vector.<Vector.<int>>,_oppCardsNum:int,_userAllCardsNum1:int,_userAllCardsNum2:int,_heroData:Vector.<Vector.<int>>,_isActioned:Boolean):void{
			
			battle.start(_isHost,_nowRound,_maxRound,_mapID,_mapData,_myCards,_oppCardsNum,_userAllCardsNum1,_userAllCardsNum2,_heroData,_isActioned);
			
			Starling.current.stage.addChild(battle);
		}
		
		public static function playBattle(_summonData:Vector.<Vector.<int>>,_moveData:Vector.<Vector.<int>>,_skillData:Vector.<Vector.<Vector.<int>>>,_attackData:Vector.<Vector.<Vector.<int>>>,_cardUid:int,_cardID:int):void{
			
			battle.playBattle(_summonData,_moveData,_skillData,_attackData,_cardUid,_cardID);
		}
		
		public static function leaveBattle(_result:int):void{
			
			battle.leaveBattle(_result);
		}
		
		public static function leaveBattleOK():void{
			
			Starling.current.stage.removeChild(battle);
			
			stage.addChild(gameQueuePanel);
		}
	}
}