package data.csv
{
	import csv.Csv;

	public class Csv_heroType extends Csv
	{
		public static const NAME:String = "heroType";
		
		public var name:String;
		public var attackType:int;
		public var attackRange:int;
		public var moveType:int;
	}
}