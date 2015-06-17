package
{
	import starling.display.Image;
	import starling.display.Sprite;
	import starling.text.TextField;
	import starling.textures.Texture;

	public class MapUnit extends Sprite
	{
		public var id:int;
		private var img:Image;
		private var tf:TextField;
		
		public function MapUnit()
		{
			super();
		}
		
		public function init(_id:int,_texture:Texture):void{

			id = _id;
			
			img = new Image(_texture);
				
			addChild(img);
			
			img.x = - img.width * 0.5;
			img.y = - img.height * 0.5;
			
			tf = new TextField(30,30,String(id),"Verdana",15);
			
			tf.x = - tf.width * 0.5;
			tf.y = - tf.height * 0.5;
			
			addChild(tf);
			
			flatten();
		}
		
		public function refresh(_texture:Texture):void{
			
			unflatten();
			
			img.texture = _texture;
			
			flatten();
		}
	}
}