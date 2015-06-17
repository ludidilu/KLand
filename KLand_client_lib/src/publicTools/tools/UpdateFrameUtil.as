package publicTools.tools
{
	import flash.display.Shape;
	import flash.events.Event;
	import flash.utils.Dictionary;

	public class UpdateFrameUtil
	{
		private static var sp:Shape;
		
		private static var dic:Dictionary;
		
		private static var isUpdateFrame:Boolean;
		
		public static function init():void{
			
			sp = new Shape;
			
			sp.addEventListener(Event.ENTER_FRAME,updateFrame);
			
			dic = new Dictionary;
		}
		
		public static function addUpdateFrame(_fun:Function,..._arg):void{
			
			if(isUpdateFrame){
				
				throw new Error("addUpdateFrame error!");
			}
			
			dic[_fun] = {fun:_fun,arg:_arg};
		}
		
		public static function delUpdateFrame(_fun:Function):void{
			
			if(isUpdateFrame){
				
				throw new Error("delUpdateFrame error!");
			}
			
			delete dic[_fun];
		}
		
		private static function updateFrame(e:Event):void{
			
			isUpdateFrame = true;
			
			for each(var obj:Object in dic){
				
				obj.fun.apply(null,obj.arg);
			}
			
			isUpdateFrame = false;
		}
	}
}