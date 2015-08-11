import game.Game;
import panel.Panel;
import server.Server;
import userService.UserService;
import data.dataCsv.MainCsv;
import data.dataDB.MainDB;
import data.dataMap.Map;


public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Panel.init();
		
		Server.init();
		
		MainCsv.init("K:/inetpub/wwwroot/KLand/data/csv/");
		
		Map.init("K:/inetpub/wwwroot/KLand/data/map/");
		
		MainDB.init("localhost",6379);
		
		UserService.init();
		
		Game.init();
		
		
		
	}

}
