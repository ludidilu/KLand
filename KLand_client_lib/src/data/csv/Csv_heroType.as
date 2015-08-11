package data.csv
{
	import csv.Csv;

	public class Csv_heroType extends Csv
	{
		public static const NAME:String = "heroType";
		
		public var name:String;
		public var minAttackRange:int;
		public var maxAttackRange:int;
		public var moveType:int;
	}
}