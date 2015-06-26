package playerData;

import data.dataDB.user.DB_user_unit;
import userData.UserDataUnit;

public class PlayerBaseData extends UserDataUnit{

	public PlayerBaseData(DB_user_unit _user_unit,String _name){
		
		super(_user_unit,_name);
	}
	
	public int money = 3728;
}
