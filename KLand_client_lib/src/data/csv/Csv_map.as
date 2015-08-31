package data.csv
{
	import csv.Csv;

	public class Csv_map extends Csv
	{
		public static const NAME:String = "map";
		
		public var name:String;
		public var flipType:int;//地图翻转类型(0不翻转 1水平翻转 2中心翻转)
	}
}