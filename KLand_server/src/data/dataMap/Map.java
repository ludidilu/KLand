package data.dataMap;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;

import data.dataCsv.map.Csv_map;

public class Map {
	
	private static HashMap<Integer, MapUnit> dic;

	public static void init(String _path) throws Exception{
		
		dic = new HashMap<>();
		
		Iterator<Integer> iter = Csv_map.dic.keySet().iterator();
		
		while(iter.hasNext()){
			
			int key = iter.next();
			
			Csv_map value = Csv_map.dic.get(key);
			
			DataInputStream reader = new DataInputStream(new FileInputStream(new File(_path + value.name)));
			
			MapUnit unit = new MapUnit();
			
			unit.dic = new HashMap<>();
			
			unit.mapWidth = reader.readInt();
			
			unit.mapHeight = reader.readInt();
			
			int length = reader.readInt();
			
			for(int i = 0 ; i < length ; i++){
				
				int pos = reader.readInt();
				
				int state = reader.readInt();
				
				unit.dic.put(pos, state);
			}
			
			unit.init();
			
			dic.put(value.id, unit);
			
			reader.close();
		}
	}
	
	public static MapUnit getMapUnit(int _id){
		
		return dic.get(_id);
	}
}
