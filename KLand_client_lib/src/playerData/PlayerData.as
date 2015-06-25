package playerData
{
	import userData.UserData;

	public class PlayerData extends UserData
	{
		public static var instance:PlayerData;
		
		public function PlayerData(){
			
			instance = this;
		}
		
		public var heroData:HeroData = new HeroData;
		public var baseData:BaseData = new BaseData;
	}
}