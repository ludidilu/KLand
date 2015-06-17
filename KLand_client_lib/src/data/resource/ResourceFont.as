package data.resource
{
	import data.Data;
	
	import loader.SuperFileLoader;
	
	import starling.text.BitmapFont;
	import starling.text.TextField;

	public class ResourceFont
	{
		public static const fontName:String = "myFont";
		
		private static var fontXML:XML;
		
		private static const fileName:String = "font.xml";
		private static var callBack:Function;
		
		public static function init(_callBack:Function):void{
			
			callBack = _callBack;
			
			SuperFileLoader.load(Data.path + Resource.path + fileName,SuperFileLoader.TYPE_TEXT,getXML);
			
//			SuperURLLoader.load(Data.path + Resource.path + fileName,URLLoaderDataFormat.TEXT,getXML);
		}
		
		private static function getXML(_str:String):void{
			
			fontXML = new XML(_str);
			
			createFont();
		}
		
		private static function createFont():void{
			
			var bitmapFont:BitmapFont = new BitmapFont(ResourceHero.getTexture("font"),fontXML);
			
			//注册
			TextField.registerBitmapFont(bitmapFont,fontName);
			
			callBack();
		}
	}
}