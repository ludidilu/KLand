package game.battle
{
	import data.resource.ResourceFont;
	import data.resource.ResourcePublic;
	
	import starling.display.Image;
	import starling.display.Sprite;
	import starling.text.TextField;

	public class BattleMapUnit extends Sprite
	{
//		private static const stateVec:Vector.<String> = Vector.<String>(["blackFrame","redFrame","redFrame"]);
		
		private var map:BattleMap;
		
		public var id:int;
		public var isMine:Boolean;

		private var bg:Image;

		private var tf:TextField;
		
		public function BattleMapUnit(_map:BattleMap,_id:int)
		{
			map = _map;
			
			id = _id;
		}
		
		public function refresh():void{
			
			if(bg == null){
				
				bg = new Image(ResourcePublic.getTexture(isMine ? "greenBg" : "redBg"));
				
				bg.x = -0.5 * bg.width;
				bg.y = -0.5 * bg.height;
				
				addChild(bg);
				
				tf = new TextField(30,30,String(id),ResourceFont.fontName,14,0x0);
				
				addChild(tf);
				
				tf.x = -55;
				
			}else{
				
				bg.texture = ResourcePublic.getTexture(isMine ? "greenBg" : "redBg");
			}
		}
	}
}