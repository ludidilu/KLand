package application
{
	import flash.display.Stage;
	
	import data.Data;
	
	import game.Game;
	
	import loader.SuperLoader;
	import loader.SuperURLLoader;
	
	import publicTools.connect.Connect_handle;
	import publicTools.tools.UpdateFrameUtil;
	
	import starling.core.Starling;
	import starling.display.Sprite;
	import starling.events.Event;

	public class InitApplication
	{
		private static var initNum:int = 2;
		
		public function InitApplication()
		{
		}
		
		public static function start(_stage:Stage):void{
			
			SuperLoader.init(5);
			SuperURLLoader.init(5);
			
			UpdateFrameUtil.init();
			
			Starling.handleLostContext = true;
			
			Game.init(_stage);
			
			var myStarling:Starling = new Starling(starling.display.Sprite,_stage);
			
			myStarling.addEventListener(Event.CONTEXT3D_CREATE,myStarlingInitOK);
			
			Data.init(oneInitOK);
		}
		
		private static function myStarlingInitOK(e:Event):void{
			
			Starling.current.start();
			
			oneInitOK();
		}
		
		private static function oneInitOK():void{
			
			initNum--;
			
			if(initNum == 0){
				
				Connect_handle.init(Game.start);
			}
		}
	}
}