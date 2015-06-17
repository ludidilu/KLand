package game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import userService.UserService;
import data.dataCsv.hero.Csv_hero;
import data.dataMap.MapUnit;

public class BattleAI {
	
	private static UserService service;
	
	public static void start(UserService _service, MapUnit _mapUnit, HashMap<Integer, Integer> _map, HashMap<Integer, BattleHero> _heroMap, HashMap<Integer, Integer> _userCards, ArrayList<Integer> _canMoveHeroUidArr, int _userMoney, HashMap<Integer, Integer> _summonData, HashMap<Integer, Integer> _moveData){
		
		service = _service;
		
		HashMap<Integer, Csv_hero> canMoveHeroMap = new HashMap<>();
		
		for(int pos : _canMoveHeroUidArr){
			
			BattleHero hero = _heroMap.get(pos);
			
			if(!hero.isHost){
				
				canMoveHeroMap.put(pos, hero.csv);
			}
		}
		
		HashMap<Integer, Csv_hero> summonData = new HashMap<>();
		
		Iterator<Entry<Integer, Integer>> iter = _userCards.entrySet().iterator();
			
		while(iter.hasNext() && _userMoney > 0){
			
			Entry<Integer, Integer> entry = iter.next();
			
			int uid = entry.getKey();
			
			int cardID = entry.getValue();
			
			Csv_hero hero = Csv_hero.dic.get(cardID);
			
			if(_userMoney >= hero.star){
				
				float r = (float)_userMoney / (hero.star + canMoveHeroMap.size());
				
				if(Math.random() < r){
					
					int pos = getSummonPos(_mapUnit.neighbourPosMap,_map,_heroMap,summonData,hero);
					
					if(pos != -1){
						
						summonData.put(pos, hero);
						
						_summonData.put(uid, pos);
						
						_userMoney = _userMoney - hero.star;
					}
				}
			}
		}
		
		
		
		
		
		if(_userMoney == 0 || canMoveHeroMap.size() == 0){
			
			return;
		}
		
		if(_userMoney < canMoveHeroMap.size()){
			
			int allMoveUnitStar = 0;
			
			HashMap<Integer, Csv_hero> tmpMap = new HashMap<>();
			
			HashMap<Integer, Integer> tmpMap2 = new HashMap<>();
			
			Iterator<Entry<Integer, Csv_hero>> iter2 = canMoveHeroMap.entrySet().iterator();
			
			while(iter2.hasNext()){
				
				Entry<Integer, Csv_hero> entry = iter2.next();
				
				int pos = entry.getKey();
				
				Csv_hero hero = entry.getValue();
				
				if(canHitEnemy(_mapUnit.neighbourPosMap,_heroMap,hero,pos)){
					
					tmpMap2.put(pos, hero.star);
					
					allMoveUnitStar = allMoveUnitStar + hero.star;
					
				}else{
					
					tmpMap2.put(pos, hero.star * 3);
					
					allMoveUnitStar = allMoveUnitStar + hero.star * 3;
				}
			}
			
			int tmpMoney = _userMoney;
			
			while(tmpMoney > 0){
				
				int randomStar = (int)(Math.random() * allMoveUnitStar);
				
				iter2 = canMoveHeroMap.entrySet().iterator();
				
				while(iter2.hasNext()){
					
					Entry<Integer, Csv_hero> entry = iter2.next();
					
					int pos = entry.getKey();
					
					Csv_hero hero = entry.getValue();
					
					int star = hero.star;
					
					if(randomStar < star){
						
						tmpMap.put(pos, hero);
						
						iter2.remove();
						
						tmpMoney = tmpMoney - 1;
						
						allMoveUnitStar = allMoveUnitStar - tmpMap2.get(pos);
						
						break;
						
					}else{
						
						randomStar = randomStar - tmpMap2.get(pos);
					}
				}
			}
			
			canMoveHeroMap = tmpMap;
		}
		
		Iterator<Entry<Integer, Csv_hero>> iter2 = canMoveHeroMap.entrySet().iterator();
		
		while(iter2.hasNext()){
			
			Entry<Integer, Csv_hero> entry = iter2.next();
			
			int pos = entry.getKey();
			Csv_hero hero = entry.getValue();
			
			int targetPos = getMovePos(_mapUnit.neighbourPosMap,_map,_heroMap,summonData,canMoveHeroMap,hero,pos);
			
			if(targetPos != -1){
				
				_moveData.put(pos, BattlePublic.getDirection(_mapUnit.mapWidth, pos, targetPos));
			}
		}
	}
	
	private static int getSummonPos(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, Integer> _map, HashMap<Integer, BattleHero> _heroMap, HashMap<Integer, Csv_hero> _summonData, Csv_hero _hero){
		
//		String str = "";
		
		ArrayList<Integer> posArr = new ArrayList<>();
		
		ArrayList<Integer> scoreArr = new ArrayList<>();
		
		int allScore = 0;
		
		Iterator<Entry<Integer, Integer>> iter = _map.entrySet().iterator();
		
		while(iter.hasNext()){
			
			Entry<Integer, Integer> entry = iter.next();
			
			int pos = entry.getKey();
			
			int type = entry.getValue();
			
			if(type == 2 && !_heroMap.containsKey(pos) && !_summonData.containsKey(pos)){
				
				posArr.add(pos);
				
//				str = str + "pos:" + pos;
				
				int tmpScore = 0;
				
				if(canHitEnemy(_neighbourPosMap, _heroMap, _hero, pos)){
					
					tmpScore = tmpScore + 4;
					
					allScore = allScore + 4;
				}
				
				int[] tmpArr = _neighbourPosMap.get(pos);//����1����ĵ�ͼ����
				
				for(int pos2 : tmpArr){
					
					if(pos2 == -1){
						
						continue;
					}
					
					int type2 = _map.get(pos2);
					
					if(type2 == 1){//�ǵ��˵���  ��4��
						
						tmpScore = tmpScore + 4;
						
						allScore = allScore + 4;
					}
					
					if(canHitEnemy(_neighbourPosMap, _heroMap, _hero, pos2)){
						
						tmpScore = tmpScore + 2;
						
						allScore = allScore + 2;
					}
				}
						
//				str = str + " score:" + tmpScore + "\n";
				
				scoreArr.add(tmpScore);
			}
		}
		
//		service.process("sendMsg", str);
		
		if(allScore > 0){
			
			int randomScore = (int)(Math.random() * allScore);
			
			for(int i = 0 ; i < scoreArr.size() ; i++){
				
				int nowScore = scoreArr.get(i);
				
				if(randomScore < nowScore){
					
					return posArr.get(i);
					
				}else{
					
					randomScore = randomScore - nowScore;
				}
			}
			
			return -1;
			
		}else{
			
			return -1;
		}
	}
	
	private static int getMovePos(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, Integer> _map, HashMap<Integer, BattleHero> _heroMap, HashMap<Integer, Csv_hero> _summonData, HashMap<Integer, Csv_hero> _moveMap, Csv_hero _hero, int _pos){
		
		String str = "";
		
		ArrayList<Integer> posArr = new ArrayList<>();
		
		ArrayList<Integer> scoreArr = new ArrayList<>();
		
		int allScore = 0;
		
		int[] tmpArr = _neighbourPosMap.get(_pos);
		
		for(int pos : tmpArr){
			
			if(pos != -1){
				
				if(!_summonData.containsKey(pos)){
					
					BattleHero hero = _heroMap.get(pos);
					
					if(hero != null && !hero.isHost && !_moveMap.containsKey(pos)){
						
						continue;
						
					}else{
						
						posArr.add(pos);
						
						int score = getMovePosScore(_neighbourPosMap, _map, _heroMap, _hero, pos);
						
						scoreArr.add(score);
						
						allScore = allScore + score;
						
						str = str + "pos:" + pos + " score:" + score + "\n";
					}
				}
			}
		}
		
		service.process("sendMsg", str);
		
		
		if(allScore > 0){
			
			int randomScore = (int)(Math.random() * allScore);
			
			for(int i = 0 ; i < scoreArr.size() ; i++){
				
				int nowScore = scoreArr.get(i);
				
				if(randomScore < nowScore){
					
					return posArr.get(i);
					
				}else{
					
					randomScore = randomScore - nowScore;
				}
			}
			
			return -1;
			
		}else{
			
			return -1;
		}
	}
	
	private static int getMovePosScore(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, Integer> _map, HashMap<Integer, BattleHero> _heroMap, Csv_hero _hero, int _pos){
		
		int score = 1;
		
		int type2 = _map.get(_pos);
		
		if(type2 == 1){//�ǵ��˵���  ��3��
			
			score = score + 5;
		}
		
		if(canHitEnemy(_neighbourPosMap,_heroMap,_hero,_pos)){
			
			score = score + 5;
		}
		
		return score;
	}
	
	private static boolean canHitEnemy(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, BattleHero> _heroMap, Csv_hero _hero, int _pos){
		
		if(_hero.heroType.attackType == 1){
			
			loop1:for(int i = 0 ; i < 6 ; i++){
				
				int nowPos = _pos;
				
				for(int m = 0 ; m < _hero.heroType.attackRange ; m++){
					
					int pos = _neighbourPosMap.get(nowPos)[i];
					
					if(pos == -1){
						
						continue loop1;
						
					}else{
						
						nowPos = pos;
					}
				}
				
				BattleHero targetHero = _heroMap.get(nowPos);
				
				if(targetHero != null && targetHero.isHost){
					
					return true;
				}
			}
		
		}else{
			
			loop2:for(int i = 0 ; i < 6 ; i++){
				
				int nowPos = _pos;
				
				for(int m = 0 ; m < _hero.heroType.attackRange ; m++){
					
					int pos = _neighbourPosMap.get(nowPos)[i];
					
					if(pos == -1){
						
						continue loop2;
						
					}else{
						
						BattleHero targetHero = _heroMap.get(nowPos);
						
						if(targetHero != null){
							
							if(targetHero.isHost){
								
								return true;
								
							}else{
							
								continue loop2;
							}
							
						}else{
							
							nowPos = pos;
						}
					}
				}
			}
		}
		
		return false;
	}
}
