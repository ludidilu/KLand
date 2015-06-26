package playerData;

import java.util.ArrayList;

import data.dataDB.user.DB_user_unit;
import userData.UserDataUnit;

public class PlayerHeroData extends UserDataUnit{

	public PlayerHeroData(DB_user_unit _user_unit,String _name){
		
		super(_user_unit,_name);
	}
	
	public ArrayList<Integer> heros;
}
