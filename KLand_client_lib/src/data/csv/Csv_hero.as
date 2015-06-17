package data.csv
{
	import flash.utils.Dictionary;
	
	import csv.Csv;

	public class Csv_hero extends Csv
	{
		public static const NAME:String = "hero";
		
		public var name:String;
		public var picName:String;
		public var type:int;
		public var star:int;
		public var maxHp:int;
		public var atk:int;
		public var maxPower:int;
		
		public var skillCondition:Vector.<int>;
		public var skillConditionArg:Vector.<int>;
		
		public var comment:String;
		
		public var heroType:Csv_heroType;
		
		public static function fix(_dic:Dictionary):void{
			
			for each(var unit:Csv_hero in _dic){
				
				unit.heroType = Csv.getData(Csv_heroType.NAME,unit.type) as Csv_heroType;
			}
		}
	}
}