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
		
		public static function syncData(_str:String):void{
			
			Game.syncData(_str);
		}
		
		public static function logout():void{
			
			trace("被踢下线了！！");
		}
		
		public static function enterBattle():void{
			
			Game.enterBattle();
		}
		
		public static function playBattle(_summonData:Vector.<Vector.<int>>,_moveData:Vector.<Vector.<int>>,_skillData:Vector.<Vector.<Vector.<int>>>,_attackData:Vector.<Vector.<Vector.<int>>>,_cardUid:int,_cardID:int):void{
			
			Game.playBattle(_summonData,_moveData,_skillData,_attackData,_cardUid,_cardID);
		}
		
		public static function leaveBattle(_result:int):void{
			
			Game.leaveBattle(_result);
		}
		
		
		
		
		public static function pushMsg(_str:String):void{
			
			trace("pushMsg:",_str);
		}
	}
}