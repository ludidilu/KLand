package game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import data.dataCsv.hero.Csv_hero;
import data.dataMap.MapUnit;

public class BattleAI {
	
	public static void start(MapUnit _mapUnit, HashMap<Integer, Integer> _map, HashMap<Integer, BattleHero> _heroMap, HashMap<Integer, Integer> _userCards, int _userMoney, HashMap<Integer, Integer> _summonData, HashMap<Integer, Integer> _moveData){
		
		HashMap<Integer, BattleHero> canMoveHeroMap = new HashMap<>();
		
		Iterator<Entry<Integer, BattleHero>> iter = _heroMap.entrySet().iterator();
		
		while(iter.hasNext()){
			
			Entry<Integer, BattleHero> entry = iter.next();
		
			BattleHero hero = entry.getValue();
			
			if(!hero.isHost && hero.power > Battle.POWER_CAN_MOVE && hero.csv.heroType.moveType != 0){
				
				int pos = entry.getKey();
				
				canMoveHeroMap.put(pos, hero);
			}
		}
		
		HashMap<Integer, Csv_hero> summonData = new HashMap<>();
		
		Iterator<Entry<Integer, Integer>> iter2 = _userCards.entrySet().iterator();
			
		while(iter2.hasNext() && _userMoney > 0){
			
			Entry<Integer, Integer> entry = iter2.next();
			
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
			
			HashMap<Integer, BattleHero> tmpMap = new HashMap<>();
			
			HashMap<Integer, Integer> tmpMap2 = new HashMap<>();
			
			Iterator<Entry<Integer, BattleHero>> iter3 = canMoveHeroMap.entrySet().iterator();
			
			while(iter3.hasNext()){
				
				Entry<Integer, BattleHero> entry = iter3.next();
				
				int pos = entry.getKey();
				
				BattleHero hero = entry.getValue();
				
				int damage = canHitEnemy(_mapUnit.neighbourPosMap,_heroMap,hero.csv,pos);
				
				if(damage > 0){
					
					tmpMap2.put(pos, hero.csv.star);
					
					allMoveUnitStar = allMoveUnitStar + hero.csv.star;
					
				}else{
					
					tmpMap2.put(pos, hero.csv.star * 3);
					
					allMoveUnitStar = allMoveUnitStar + hero.csv.star * 3;
				}
			}
			
			int tmpMoney = _userMoney;
			
			while(tmpMoney > 0){
				
				int randomStar = (int)(Math.random() * allMoveUnitStar);
				
				iter3 = canMoveHeroMap.entrySet().iterator();
				
				while(iter3.hasNext()){
					
					Entry<Integer, BattleHero> entry = iter3.next();
					
					int pos = entry.getKey();
					
					BattleHero hero = entry.getValue();
					
					int star = hero.csv.star;
					
					if(randomStar < star){
						
						tmpMap.put(pos, hero);
						
						iter3.remove();
						
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
		
		Iterator<Entry<Integer, BattleHero>> iter3 = canMoveHeroMap.entrySet().iterator();
		
		ArrayList<Integer> moveTargetPosArr = new ArrayList<>();
		
		while(iter3.hasNext()){
			
			Entry<Integer, BattleHero> entry = iter3.next();
			
			int pos = entry.getKey();
			BattleHero hero = entry.getValue();
			
			int targetPos = getMovePos(_mapUnit.neighbourPosMap,_map,_heroMap,summonData,canMoveHeroMap,hero,pos,moveTargetPosArr);
			
			if(targetPos != -1){
				
				moveTargetPosArr.add(targetPos);
				
				_moveData.put(pos, BattlePublic.getDirection(_mapUnit.mapWidth, pos, targetPos));
			}
		}
	}
	
	private static int getSummonPos(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, Integer> _map, HashMap<Integer, BattleHero> _heroMap, HashMap<Integer, Csv_hero> _summonData, Csv_hero _hero){
		
		String str = "Summon result:" + _hero.id + "\n";
		
		ArrayList<Integer> posArr = new ArrayList<>();
		
		ArrayList<Integer> scoreArr = new ArrayList<>();
		
		int allScore = 0;
		
		Iterator<Entry<Integer, Integer>> iter = _map.entrySet().iterator();
		
		while(iter.hasNext()){
			
			Entry<Integer, Integer> entry = iter.next();
			
			int pos = entry.getKey();
			
			int type = entry.getValue();
			
			if(type == 2 && !_heroMap.containsKey(pos) && !_summonData.containsKey(pos)){
				
				int tmpScore = getSummonPosScore(_neighbourPosMap,_map,_heroMap,_hero,pos);
				
				str = str + "pos:" + pos + "  score:" + tmpScore + "\n";
				
				if(tmpScore > 0){
					
					allScore = allScore + tmpScore;

					posArr.add(pos);
					
					scoreArr.add(tmpScore);
				}
			}
		}
		
		System.out.println(str);
		
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
	
	private static int getMovePos(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, Integer> _map, HashMap<Integer, BattleHero> _heroMap, HashMap<Integer, Csv_hero> _summonData, HashMap<Integer, BattleHero> _moveMap, BattleHero _hero, int _pos, ArrayList<Integer> _moveTargetPosArr){
		
		String str = "move result:" + _pos + "\n";
		
		ArrayList<Integer> posArr = new ArrayList<>();
		
		ArrayList<Integer> scoreArr = new ArrayList<>();
		
		int allScore = 0;
		
		int[] tmpArr = _neighbourPosMap.get(_pos);
		
		for(int pos : tmpArr){
			
			if(pos != -1){
				
				if(!_summonData.containsKey(pos) && !_moveTargetPosArr.contains(pos)){
					
					BattleHero hero = _heroMap.get(pos);
					
					if(hero != null){
						
						if(hero.isHost){
							
							continue;
							
						}else{
							
							if(!_moveMap.containsKey(pos)){
								
								continue;
							}
						}
					}
					
					int score = getMovePosScore(_neighbourPosMap, _map, _heroMap, _hero, pos);
					
					str = str + "pos:" + pos + " score:" + score + "\n";
					
					if(score > 0){
					
						scoreArr.add(score);
						
						posArr.add(pos);
						
						allScore = allScore + score;
					}
				}
			}
		}
		
		System.out.println(str);
		
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
	
	private static int getSummonPosScore(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, Integer> _map, HashMap<Integer, BattleHero> _heroMap, Csv_hero _hero, int _pos){
		
		int tmpScore = 0;
		
		int damage = willBeHit(_neighbourPosMap, _heroMap, _pos);
			
		if(damage > 0){
			
			if(damage >= _hero.maxHp){
				
				tmpScore = tmpScore - _hero.maxHp * _hero.star * 2;
				
			}else{
				
				tmpScore = tmpScore - damage * _hero.star;
			}
		}
		
		int damageWithoutMove = canHitEnemy(_neighbourPosMap, _heroMap, _hero, _pos);
		
		if(damageWithoutMove > 0){
			
			if(_hero.heroType.moveType == 2){
			
				tmpScore = tmpScore + damageWithoutMove * 2;
				
			}else{
				
				tmpScore = tmpScore + damageWithoutMove;
			}
		}
		
		if(_hero.heroType.moveType != 0){
		
			int[] tmpArr = _neighbourPosMap.get(_pos);//先找1格外的地图格子
			
			boolean hasEnemyPos = false;
			int hasHitEnemy = 0;
			
			for(int pos2 : tmpArr){
				
				if(pos2 == -1){
					
					continue;
				}
				
				int type2 = _map.get(pos2);
				
				if(type2 == 1){//是敌人地盘  加4分
					
					if(!hasEnemyPos){
						
						hasEnemyPos = true;
					}
				}
				
				damage = canHitEnemy(_neighbourPosMap, _heroMap, _hero, pos2);
					
				if(damage > hasHitEnemy){
					
					hasHitEnemy = damage;
				}
			}
			
			if(hasEnemyPos){
				
				tmpScore = tmpScore + 10;
			}
			
			if(damageWithoutMove == 0 && hasHitEnemy > 0){
				
				tmpScore = tmpScore + hasHitEnemy;
			}
		}
		
		return tmpScore;
	}
	
	private static int getMovePosScore(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, Integer> _map, HashMap<Integer, BattleHero> _heroMap, BattleHero _hero, int _pos){
		
		int score = 0;
		
		int type2 = _map.get(_pos);
		
		if(type2 == 1){//是敌人地盘  加3分
			
			score = score + 10;
		}
		
		int damage = canHitEnemy(_neighbourPosMap,_heroMap,_hero.csv,_pos);
			
		score = score + damage;
		
		damage = willBeHit(_neighbourPosMap, _heroMap, _pos);
			
		if(damage >= _hero.hp){
			
			score = score - _hero.hp * _hero.csv.star * 2;
			
		}else{
			
			score = score - damage * _hero.csv.star;
		}
		
		return score;
	}
	
	private static int canHitEnemy(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, BattleHero> _heroMap, Csv_hero _hero, int _pos){
		
		int damage = 0;
		
		int atk = _hero.atk;
		
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
					
					if(targetHero.hp >= atk){
						
						damage = damage + atk * targetHero.csv.star;
						
						return damage;
						
					}else{
						
						damage = damage + targetHero.hp * targetHero.csv.star * 2;
						
						atk = atk - targetHero.hp;
					}
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
								
								if(targetHero.hp >= atk){
									
									damage = damage + atk * targetHero.csv.star;
									
									return damage;
									
								}else{
									
									damage = damage + targetHero.hp * targetHero.csv.star * 2;
									
									atk = atk - targetHero.hp;
								}
							}
							
							continue loop2;
							
						}else{
							
							nowPos = pos;
						}
					}
				}
			}
		}
		
		return damage;
	}
	
	private static int willBeHit(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, BattleHero> _heroMap, int _pos){
		
		int damage = 0;
		
		for(int i = 0 ; i < 6 ; i++){
			
			int nowPos = _pos;
			
			boolean lineAttack = true;
			
			int range = 0;
			
			while(true){
				
				range++;
				
				nowPos = _neighbourPosMap.get(nowPos)[i];
				
				if(nowPos == -1){
					
					break;
				}
				
				BattleHero hero = _heroMap.get(nowPos);
				
				if(hero != null){
					
					if(hero.isHost && hero.power > 0 && hero.csv.heroType.canAttack){
						
						if(hero.csv.heroType.attackType == 1){
							
							if(hero.csv.heroType.attackRange == range){
								
								damage = damage + hero.csv.atk;
							}
							
						}else if(hero.csv.heroType.attackType == 2 && lineAttack){
							
							if(hero.csv.heroType.attackRange >= range){
								
								damage = damage + hero.csv.atk;
							}
						}
						
					}
					
					lineAttack = false;
				}
			}
		}
		
		return damage;
	}
}
