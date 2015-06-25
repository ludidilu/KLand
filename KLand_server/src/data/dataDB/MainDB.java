package data.dataDB;

import java.sql.ResultSet;

import userService.UserService;
import data.dataDB.user.DB_user;
import data.dataDB.user.DB_user_unit;

public class MainDB {

	public static void init(String _DBName,String _userName,String _password) throws Exception{
		
		DB.init(_DBName,_userName,_password,UserService.class,DB_user_unit.class);
		
		String sql = "select * from userhero";
        
        ResultSet rs = DB.stmt.executeQuery(sql);  
        
        while (rs.next()){
        	
        	int uid = rs.getInt("uid");
        	
        	String herosStr = rs.getString("heros");
        	
        	DB_user_unit user = DB_user.getUserByID(uid);
        	
        	UserService service = (UserService)user.service;
        	
        	if(!herosStr.equals("")){
        	
	        	String[] tmpStrVec = herosStr.split("\\$");
	        	
	        	for(int i = 0 ; i < tmpStrVec.length ; i++){
	        		
	        		service.userData.heroData.heros.add(Integer.parseInt(tmpStrVec[i]));
	        	}
        	}
        }
	}
}
