package data.resource
{
	import flash.display.BitmapData;
	import flash.utils.Dictionary;
	
	import data.Data;
	
	import loader.SuperFileLoader;
	
	import starling.textures.Texture;
	import starling.textures.TextureAtlas;

	public class ResourcePublic
	{
		private static var publicAtlas:TextureAtlas;
		private static var publicTexture:Texture;
		private static var publicXML:XML;
		
		private static const fileName:String = "public";
		private static var dic:Dictionary = new Dictionary;
		
		private static var textureLoaded:Boolean;
		private static var xmlLoaded:Boolean;
		
		private static var callBack:Function;
		
		public function ResourcePublic()
		{
		}
		
		
		public static function init(_callBack:Function):void{
			
			callBack = _callBack;
			
			SuperFileLoader.load(Data.path + Resource.path + fileName + ".png",SuperFileLoader.TYPE_PICTURE,getPic);
			
//			SuperLoader.load(Data.path + Resource.path + fileName + ".png",getPic);
			
			SuperFileLoader.load(Data.path + Resource.path + fileName + ".xml",SuperFileLoader.TYPE_TEXT,getXML);
			
//			SuperURLLoader.load(Data.path + Resource.path + fileName + ".xml",URLLoaderDataFormat.TEXT,getXML);
		}
		
		private static function getPic(_bitmapData:BitmapData):void{
			
			publicTexture = Texture.fromBitmapData(_bitmapData,false);
			
			_bitmapData.dispose();
			
			if(xmlLoaded){
				
				createAtlas();
				
			}else{
				
				textureLoaded = true;
			}
		}
		
		private static function getXML(_str:String):void{
			
			publicXML = new XML(_str); 
			
			if(textureLoaded){
				
				createAtlas();
				
			}else{
				
				xmlLoaded = true;
			}
		}
		
		private static function createAtlas():void{
			
			publicAtlas = new TextureAtlas(publicTexture,publicXML);
			
			var nameVec:Vector.<String> = publicAtlas.getNames();
			
			for each(var str:String in nameVec){
				
				dic[str] = publicAtlas.getTexture(str);
			}
			
			callBack();
		}
		
		public static function getTexture(_name:String):Texture{
			
			return dic[_name];
		}
	}
}