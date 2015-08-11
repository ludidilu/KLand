package data
{
	import csv.Csv;
	
	import data.csv.Csv_hero;
	import data.csv.Csv_heroType;
	import data.csv.Csv_map;
	import data.map.Map;
	import data.resource.Resource;

	public class Data
	{
		public static const path:String = "K:/inetpub/wwwroot/KLand/data/";
		private static const csvPath:String = "csv/";
		
		private static var callBack:Function;
		
		private static var initNum:int = 2;
		
		public function Data()
		{
		}
		
		public static function init(_callBack:Function = null):void{
			
			callBack = _callBack;
			
			Resource.init(oneInitOK);
			
			var nameVec:Vector.<String> = Vector.<String>([Csv_hero.NAME,Csv_heroType.NAME,Csv_map.NAME]);
			var classVec:Vector.<Class> = Vector.<Class>([Csv_hero,Csv_heroType,Csv_map]);
			
			Csv.init(path + csvPath,nameVec,classVec,oneInitOK);
		}
		
		private static function oneInitOK():void{
			
			initNum--;
			
			if(initNum == 0){
				
				Csv_hero.fix(Csv.getDic(Csv_hero.NAME));
			
				Map.init(mapInitOK);
			}
		}
		
		private static function mapInitOK():void{
			
			if(callBack != null){
				
				callBack();
				
				callBack = null;
			}
		}
	}
}