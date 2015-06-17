package data.map
{
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	
	import csv.Csv;
	
	import data.Data;
	import data.csv.Csv_map;
	
	import loader.SuperFileLoader;

	public class Map
	{
		private static const path:String = "map/";
		
		private static var dic:Dictionary;
		
		private static var loadNum:int;
		
		private static var callBack:Function;
		
		public static function init(_callBack:Function = null):void{
			
			callBack = _callBack;
			
			loadNum = Csv.getLength(Csv_map.NAME);
			
			dic = new Dictionary;
			
			var tmpDic:Dictionary = Csv.getDic(Csv_map.NAME);
			
			for each(var unit:Csv_map in tmpDic){
				
				SuperFileLoader.load(Data.path + path + unit.name,SuperFileLoader.TYPE_BYTEARRAY,loadMapOK,unit.id);
			}
		}
		
		private static function loadMapOK(_byteArray:ByteArray,_id:int):void{
			
			var unit:MapUnit = new MapUnit;
			
			unit.mapWidth = _byteArray.readInt();
			
			unit.mapHeight = _byteArray.readInt();
			
			var length:int = _byteArray.readInt();
			
			for(var i:int = 0 ; i < length ; i++){
				
				var pos:int = _byteArray.readInt();
				
				var state:int = _byteArray.readInt();
				
				unit.dic[pos] = state;
			}
			
			unit.init();
			
			dic[_id] = unit;
			
			loadNum--;
			
			if(loadNum == 0){
				
				if(callBack != null){
					
					callBack();
					
					callBack = null;
				}
			}
		}
		
		public static function getMap(_id:int):MapUnit{
			
			return dic[_id];
		}
	}
}