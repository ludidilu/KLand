package
{
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	import flash.utils.Dictionary;
	
	public class Test extends flash.display.Sprite
	{
		public function Test()
		{
			super();
			
			// 支持 autoOrient
			stage.align = StageAlign.TOP_LEFT;
			stage.scaleMode = StageScaleMode.NO_SCALE;
			
			var dic:Dictionary = new Dictionary;
			
			trace(dic[1] == 0);
		}
	}
}