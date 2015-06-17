package data.resource
{
	

	public class Resource
	{
		public static const path:String = "resource/";
		
		private static var callBack:Function;
		
		private static var loadNum:int = 2;
		
		public function Resource()
		{
		}
		
		public static function init(_callBack:Function):void{
			
			callBack = _callBack;
			
			ResourcePublic.init(oneLoadOK);
			ResourceHero.init(oneLoadOK);
			
		}
		
		private static function oneLoadOK():void{
			
			loadNum--;
			
			if(loadNum == 0){
				
				ResourceFont.init(callBack);
			}
		}
	}
}