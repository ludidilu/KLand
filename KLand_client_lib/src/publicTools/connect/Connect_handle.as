package publicTools.connect
{
	import connect.Connect;
	
	import game.Game;
	
	import starling.core.Starling;
	

	public class Connect_handle
	{
		private static const ip:String = "127.0.0.1";
		private static const port:int = 1983;
		
		public function Connect_handle()
		{
		}
		
		public static function init(_callBack:Function):void{
			
			Connect.init(ip,port,Connect_handle,sendPacket,receivePacket,_callBack);
		}
		
		private static function sendPacket():void{
			
			Starling.current.touchable = false;
		}
		
		private static function receivePacket():void{
			
			Starling.current.touchable = true;
		}
		
		public static function sendData(_id:int,...arg):void{
			
			Connect.sendData(_id,arg);
		}
		
		public static function loginOK(_boolean:Boolean):void{
			
			Game.loginOK(_boolean);
		}
		
		public static function getUserDataOK(_isInBattle:Boolean,_heros:Vector.<int>):void{
			
			Game.getUserData(_isInBattle,_heros);
		}
		
		public static function logout():void{
			
			trace("被踢下线了！！");
		}
		
		public static function enterQueueOK(_result:Boolean):void{
			
			Game.enterQueueOK(_result);
		}
		
		public static function quitQueueOK(_result:Boolean):void{
			
			Game.quitQueueOK(_result);
		}
		
		public static function fightAiOK(_result:Boolean):void{
			
			Game.fightAiOK(_result);
		}
		
		public static function quitAiOK(_result:Boolean):void{
			
			Game.quitAiOK(_result);
		}
		
		public static function enterBattle():void{
			
			Game.enterBattle();
		}
		
		public static function getBattleDataOK(_isHost:Boolean,_nowRound:int,_maxRound:int,_mapID:int,_mapData:Vector.<int>,_myCards:Vector.<Vector.<int>>,_oppCardsNum:int,_userAllCardsNum1:int,_userAllCardsNum2:int,_heroData:Vector.<Vector.<int>>,_canMoveData:Vector.<int>,_isActioned:Boolean,_actionHeroData:Vector.<Vector.<int>>,_actionSummonData:Vector.<Vector.<int>>):void{
			
			Game.getBattleData(_isHost,_nowRound,_maxRound,_mapID,_mapData,_myCards,_oppCardsNum,_userAllCardsNum1,_userAllCardsNum2,_heroData,_canMoveData,_isActioned,_actionHeroData,_actionSummonData);
		}
		
		public static function sendBattleActionOK(_result:Boolean):void{
			
			Game.sendBattleActionOK(_result);
		}
		
		public static function playBattle(_summonData1:Vector.<Vector.<int>>,_summonData2:Vector.<Vector.<int>>,_moveData:Vector.<Vector.<int>>,_skillData:Vector.<Vector.<Vector.<int>>>,_attackData:Vector.<Vector.<Vector.<int>>>,_cardUid:int,_cardID:int,_oppCardID:int,_canMoveData:Vector.<int>):void{
			
			Game.playBattle(_summonData1,_summonData2,_moveData,_skillData,_attackData,_cardUid,_cardID,_oppCardID,_canMoveData);
		}
		
		public static function quitBattleOK(_result:Boolean):void{
			
			if(_result){
				
				Game.leaveBattle(-1);
			}
		}
		
		public static function leaveBattle(_result:int):void{
			
			Game.leaveBattle(_result);
		}
		
		
		
		
		public static function pushMsg(_str:String):void{
			
			trace("pushMsg:",_str);
		}
	}
}