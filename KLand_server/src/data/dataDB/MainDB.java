package data.dataDB;

import playerData.PlayerData;
import userService.UserService;

public class MainDB {

	public static void init(String _path,int _port) throws Exception{
		
		DB.init(_path,_port,UserService.class,PlayerData.class);
	}
}
