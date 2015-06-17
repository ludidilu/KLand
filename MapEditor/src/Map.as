package
{
	import flash.display.BitmapData;
	import flash.display.Shape;
	import flash.events.Event;
	import flash.filesystem.File;
	import flash.filesystem.FileMode;
	import flash.filesystem.FileStream;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	
	import starling.display.Sprite;
	import starling.events.Touch;
	import starling.events.TouchEvent;
	import starling.events.TouchPhase;
	import starling.textures.Texture;

	public class Map
	{
		private static const unitWidth:Number = 40;
		private static const lineWidth:Number = 3;
		
		private static var container:Sprite;
		
		private static var dic:Dictionary;
		private static var mapWidth:int;
		private static var mapHeight:int;
		
		
		private static var textureRed:Texture;
		private static var textureGreen:Texture;
		private static var textureWhite:Texture;
		
		private static var initOK:Boolean;
		
		public static function start(_mapContainer:Sprite,_mapWidth:int,_mapHeight:int,_dic:Dictionary):void{
			
			if(!initOK){
				
				init();
			}
			
			if(container != null){
				
				container.dispose();
				
				container.parent.removeChild(container);
			}
			
			container = new Sprite;
			
			_mapContainer.addChild(container);
			
			mapWidth = _mapWidth;
			mapHeight = _mapHeight;
			
			dic = _dic;
			
			create();
		}
		
		private static function init():void{
			
			initOK = true;
			
			var shape:Shape = getShape(0xFF0000,unitWidth,lineWidth);
			
			var bitmapData:BitmapData = new BitmapData(shape.width,shape.height,true,0x00000000);
			
			bitmapData.draw(shape,null,null,null,null,true);
			
			textureRed = Texture.fromBitmapData(bitmapData,false);
			
			shape = getShape(0x00FF00,unitWidth,lineWidth);
			
			bitmapData = new BitmapData(shape.width,shape.height,true,0x00000000);
			
			bitmapData.draw(shape,null,null,null,null,true);
			
			textureGreen = Texture.fromBitmapData(bitmapData,false);
			
			shape = getShape(0xFFFFFF,unitWidth,lineWidth);
			
			bitmapData = new BitmapData(shape.width,shape.height,true,0x00000000);
			
			bitmapData.draw(shape,null,null,null,null,true);
			
			textureWhite = Texture.fromBitmapData(bitmapData,false);
		}
		
		private static function create():void{
			
			for(var i:int = 0 ; i < mapHeight ; i++){
				
				for(var m:int = 0 ; m < mapWidth ; m++){
					
					if(m == mapWidth - 1 && i % 2 == 1){
						
						break;
					}
					
					var sp:MapUnit = new MapUnit;
					
					var id:int = i * mapWidth - int(i  * 0.5) + m;
					
					if(dic[id] == null){
					
						sp.init(id,textureWhite);
						
					}else if(dic[id] == 1){
						
						sp.init(id,textureGreen);
						
					}else{
						
						sp.init(id,textureRed);
					}
					
					sp.x = unitWidth * 1.5 + ((i % 2) == 0 ? 0 : (unitWidth * 0.5 * Math.sqrt(3))) + m * unitWidth * Math.sqrt(3);
					
					sp.y = unitWidth * 1.5 + i * unitWidth * 1.5;
					
					container.addChild(sp);
					
					sp.addEventListener(TouchEvent.TOUCH,beTouch);
				}
			}
		}
		
		private static function beTouch(e:TouchEvent):void{
			
			var unit:MapUnit = e.currentTarget as MapUnit;
			
			var touch:Touch = e.getTouch(unit,TouchPhase.ENDED);
			
			if(touch != null){
				
				trace("id:",unit.id);
				
				if(dic[unit.id] == null){
					
					dic[unit.id] = 1;
					
					unit.refresh(textureGreen);
					
				}else if(dic[unit.id] == 1){
					
					dic[unit.id] = 2;
					
					unit.refresh(textureRed);
					
				}else{
					
					delete dic[unit.id];
					
					unit.refresh(textureWhite);
				}
			}
		}
		
		public static function save():void{
			
			var file:File = File.applicationDirectory.resolvePath("");
			
			file.browseForSave("save");
			
			file.addEventListener(Event.SELECT,fileComplete);
		}
		
		private static function fileComplete(e:Event):void{
			
			var file:File = e.target as File;
			
			var byteArray:ByteArray = new ByteArray;
			
			byteArray.writeInt(mapWidth);
			byteArray.writeInt(mapHeight);
			
			var pos:int = byteArray.position;
			
			byteArray.writeInt(0);
			
			var length:int = 0;
			
			for(var str:String in dic){
				
				byteArray.writeInt(int(str));
				
				byteArray.writeInt(dic[str]);
				
				length++;
			}
			
			byteArray.position = pos;
			
			byteArray.writeInt(length);
			
			var fs:FileStream = new FileStream;
			
			fs.open(file,FileMode.WRITE);
			
			fs.writeBytes(byteArray);
			
			fs.close();
		}
		
		private static function getShape(_color:uint,_width:Number,_lineWidth:Number):Shape{
			
			var shape:Shape = new Shape;
			
			shape.graphics.lineStyle(_lineWidth);
			
			shape.graphics.beginFill(_color);
			
			shape.graphics.moveTo(_lineWidth * 0.5 + _width * 0.5 * Math.sqrt(3),_lineWidth * 0.5 + 0);
			
			shape.graphics.lineTo(_lineWidth * 0.5 + _width * Math.sqrt(3),_lineWidth * 0.5 + _width * 0.5);
			
			shape.graphics.lineTo(_lineWidth * 0.5 + _width * Math.sqrt(3),_lineWidth * 0.5 + _width * 1.5);
			
			shape.graphics.lineTo(_lineWidth * 0.5 + _width * 0.5 * Math.sqrt(3),_lineWidth * 0.5 + _width * 2);
			
			shape.graphics.lineTo(_lineWidth * 0.5 + 0,_lineWidth * 0.5 + _width * 1.5);
			
			shape.graphics.lineTo(_lineWidth * 0.5 + 0,_lineWidth * 0.5 + _width * 0.5);
			
			shape.graphics.lineTo(_lineWidth * 0.5 + _width * 0.5 * Math.sqrt(3),_lineWidth * 0.5 + 0);
			
			return shape;
		}
	}
}