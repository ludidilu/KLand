package
{
	import flash.utils.Dictionary;

	public class SS extends Dictionary
	{
		public function SS()
		{
		}
		
		public function put(_key:Object,_value:Object):void{
			
			this[_key] = _value;
		}
	}
}