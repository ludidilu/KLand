package data.dataCsv;

import panel.Panel;
import userService.UserService;
import data.dataCsv.ai.Csv_ai;
import data.dataCsv.battle.Csv_battle;
import data.dataCsv.battle.Csv_battleAi;
import data.dataCsv.hero.Csv_hero;
import data.dataCsv.hero.Csv_heroType;
import data.dataCsv.map.Csv_map;

public class MainCsv {

	public static void init(String _path) throws Exception{
		
		Csv.init(_path,UserService.class);
		
		Csv.setData(Csv_heroType.class, "heroType", null);
		
		Csv.setData(Csv_hero.class, "hero", Csv_hero.class.getDeclaredMethod("fix"));
		
		Csv.setData(Csv_map.class, "map", null);
		
		Csv.setData(Csv_ai.class, "ai", null);
		
		Csv.setData(Csv_battle.class, "battle", null);
		
		Csv.setData(Csv_battleAi.class, "battleAi", null);
		
		Panel.show("CSV±Ìº”‘ÿÕÍ±œ");
	}
}
