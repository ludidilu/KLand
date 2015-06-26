package data.dataDB;

import playerData.PlayerData;
import userService.UserService;

public class MainDB {

	public static void init(String _DBName,String _userName,String _password) throws Exception{
		
		DB.init("localhost",6379,UserService.class,PlayerData.class);
	}
}
