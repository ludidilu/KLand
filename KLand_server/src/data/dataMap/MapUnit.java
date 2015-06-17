package data.dataMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class MapUnit {

	public int mapWidth;
	public int mapHeight;
	
	public int size;
	
	public int score1;
	public int score2;
	
	public HashMap<Integer, Integer> dic;
	
	public HashMap<Integer, int[]> neighbourPosMap;
	
	public void init(){
		
		size = mapWidth * mapHeight - mapHeight / 2;
		
		neighbourPosMap = new HashMap<>();
		
		Iterator<Entry<Integer, Integer>> iter = dic.entrySet().iterator();
		
		while(iter.hasNext()){
			
			Entry<Integer, Integer> entry = iter.next();
			
			int pos = entry.getKey();
			
			int type = entry.getValue();
			
			if(type == 1){
				
				score1++;
				
			}else{
				
				score2++;
			}
			
			int[] vec = getNeighbourPosVec(pos);
			
			neighbourPosMap.put(pos, vec);
		}
	}
	
	private int[] getNeighbourPosVec(int _pos){
		
		int[] vec = new int[6];
		
		if(_pos % (mapWidth * 2 - 1) != 0){
			
			if(_pos > mapWidth - 1){
			
				int p = _pos - mapWidth;
				
				if(dic.containsKey(p)){
					
					vec[5] = p;
					
				}else{
					
					vec[5] = -1;
				}
				
			}else{
				
				vec[5] = -1;
			}
			
			if(_pos < size - mapWidth){
				
				int p = _pos + mapWidth - 1;
				
				if(dic.containsKey(p)){
					
					vec[3] = p;
					
				}else{
					
					vec[3] = -1;
				}
				
			}else{
				
				vec[3] = -1;
			}
			
			if(_pos % (mapWidth * 2 - 1) != mapWidth){
				
				int p = _pos - 1;
				
				if(dic.containsKey(p)){
					
					vec[4] = p;
					
				}else{
					
					vec[4] = -1;
				}
				
			}else{
				
				vec[4] = -1;
			}
			
		}else{
			
			vec[3] = -1;
			vec[4] = -1;
			vec[5] = -1;
		}
		
		if(_pos % (mapWidth * 2 - 1) != mapWidth - 1){
			
			if(_pos > mapWidth - 1){
				
				int p = _pos - mapWidth + 1;
				
				if(dic.containsKey(p)){
					
					vec[0] = p;
					
				}else{
					
					vec[0] = -1;
				}
				
			}else{
				
				vec[0] = -1;
			}
			
			if(_pos < size - mapWidth){
				
				int p = _pos + mapWidth;
				
				if(dic.containsKey(p)){
					
					vec[2] = p;
					
				}else{
					
					vec[2] = -1;
				}
				
			}else{
				
				vec[2] = -1;
			}
			
			if(_pos % (mapWidth * 2 - 1) != mapWidth * 2 - 2){
				
				int p = _pos + 1;
				
				if(dic.containsKey(p)){
					
					vec[1] = p;
					
				}else{
					
					vec[1] = -1;
				}
				
			}else{
				
				vec[1] = -1;
			}
			
		}else{
			
			vec[0] = -1;
			vec[1] = -1;
			vec[2] = -1;
		}
		
		return vec;
	}
}
