package game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BattlePublic {

	//----这2个方法现在不使用  如果地图相邻单元格不缓存就会用到
	public static ArrayList<Integer> getNeighbourPosVec(int _mapWidth,int _size,HashMap<Integer, Integer> _dic,int _pos){
		
		ArrayList<Integer> vec = new ArrayList<>();
		
		if(_pos % (_mapWidth * 2 - 1) != 0){
			
			if(_pos > _mapWidth - 1){
			
				int p = _pos - _mapWidth;
				
				if(_dic.containsKey(p)){
					
					vec.add(p);
				}
			}
			
			if(_pos < _size - _mapWidth){
				
				int p = _pos + _mapWidth - 1;
				
				if(_dic.containsKey(p)){
					
					vec.add(p);
				}
			}
			
			if(_pos % (_mapWidth * 2 - 1) != _mapWidth){
				
				int p = _pos - 1;
				
				if(_dic.containsKey(p)){
					
					vec.add(p);
				}
			}
		}
		
		if(_pos % (_mapWidth * 2 - 1) != _mapWidth - 1){
			
			if(_pos > _mapWidth - 1){
				
				int p = _pos - _mapWidth + 1;
				
				if(_dic.containsKey(p)){
					
					vec.add(p);
				}
			}
			
			if(_pos < _size - _mapWidth){
				
				int p = _pos + _mapWidth;
				
				if(_dic.containsKey(p)){
					
					vec.add(p);
				}
			}
			
			if(_pos % (_mapWidth * 2 - 1) != _mapWidth * 2 - 2){
				
				int p = _pos + 1;
				
				if(_dic.containsKey(p)){
					
					vec.add(p);
				}
			}
		}
		
		return vec;
	}
	
	public static int getDirectPos(int _mapWidth,int _size,HashMap<Integer, Integer> _dic,int _pos,int _target){
		
		switch(_target){
		
			case 0:
				
				if(_pos % (_mapWidth * 2 - 1) != _mapWidth - 1){
					
					if(_pos > _mapWidth - 1){
						
						int p = _pos - _mapWidth + 1;
						
						if(_dic.containsKey(p)){
							
							return p;
							
						}else{
							
							return -1;
						}
						
					}else{
						
						return -1;
					}
					
				}else{
					
					return -1;
				}
				
			case 1:
				
				if(_pos % (_mapWidth * 2 - 1) != _mapWidth - 1){
					
					if(_pos % (_mapWidth * 2 - 1) != _mapWidth * 2 - 2){
						
						int p = _pos + 1;
						
						if(_dic.containsKey(p)){
							
							return p;
							
						}else{
							
							return -1;
						}
						
					}else{
						
						return -1;
					}
					
				}else{
					
					return -1;
				}
				
			case 2:
				
				if(_pos % (_mapWidth * 2 - 1) != _mapWidth - 1){
					
					if(_pos < _size - _mapWidth){
						
						int p = _pos + _mapWidth;
						
						if(_dic.containsKey(p)){
							
							return p;
							
						}else{
							
							return -1;
						}
						
					}else{
						
						return -1;
					}
					
				}else{
					
					return -1;
				}
				
			case 3:
				
				if(_pos % (_mapWidth * 2 - 1) != 0){
					
					if(_pos < _size - _mapWidth){
						
						int p = _pos + _mapWidth - 1;
						
						if(_dic.containsKey(p)){
							
							return p;
							
						}else{
							
							return -1;
						}
						
					}else{
						
						return -1;
					}
					
				}else{
					
					return -1;
				}
				
			case 4:
				
				if(_pos % (_mapWidth * 2 - 1) != 0){
					
					if(_pos % (_mapWidth * 2 - 1) != _mapWidth){
						
						int p = _pos - 1;
						
						if(_dic.containsKey(p)){
							
							return p;
							
						}else{
							
							return -1;
						}
						
					}else{
						
						return -1;
					}
					
				}else{
					
					return -1;
				}
				
			default:
				
				if(_pos % (_mapWidth * 2 - 1) != 0){
					
					if(_pos > _mapWidth - 1){
					
						int p = _pos - _mapWidth;
						
						if(_dic.containsKey(p)){
							
							return p;
									
						}else{
							
							return -1;
						}
						
					}else{
						
						return -1;
					}
					
				}else{
					
					return -1;
				}
		}
	}
	//---------------
	
	public static int getDirection(int _mapWidth, int _pos, int _target){
		
		int r = _target - _pos;
		
		if(r == 1){
			
			return 1;
			
		}else if(r == -1){
			
			return 4;
			
		}else if(r == _mapWidth){
			
			return 2;
			
		}else if(r == -_mapWidth){
			
			return 5;
					
		}else if(r == _mapWidth - 1){
			
			return 3;
			
		}else{
			
			return 0;
		}
	}
	
	public static void getPosInNeighbour2(HashMap<Integer, int[]> _neighbourPosMap, int _pos, int[] _result){
		
		int[] arr = _neighbourPosMap.get(_pos);
		
		for(int i = 0 ; i < 6 ; i++){
			
			int pos = arr[i];
			
			if(pos != -1){
				
				int[] arr2 = _neighbourPosMap.get(pos);
				
				if(i == 5){
					
					_result[i * 2] = arr2[i];
					_result[i * 2 + 1] = arr2[0];
					
				}else{
					
					_result[i * 2] = arr2[i];
					_result[i * 2 + 1] = arr2[i + 1];
				}
				
			}else{
				
				_result[i * 2] = -1;
				_result[i * 2 + 1] = -1;
			}
		}
	}
	
	//type 0所有 1敌方 2友方
	public static void getHerosAndDirectionInRange(HashMap<Integer, int[]> _neighbourPosMap,HashMap<Integer, BattleHero> _heroDic,BattleHero _hero,int _type,ArrayList<BattleHero> _heroArr, ArrayList<Integer> _directionArr){
		
		if(_hero.csv.heroType.attackType == 1){
			
			loop1:for(int i = 0 ; i < 6 ; i++){
				
				int nowPos = _hero.pos;
				
				for(int m = 0 ; m < _hero.csv.heroType.attackRange ; m++){
					
					int pos = _neighbourPosMap.get(nowPos)[i];
					
					if(pos == -1){
						
						continue loop1;
						
					}else{
						
						nowPos = pos;
					}
				}
				
				BattleHero targetHero = _heroDic.get(nowPos);
				
				if(targetHero != null){
					
					if(_type == 0){
						
						_heroArr.add(targetHero);
						
						if(_directionArr != null){
						
							_directionArr.add(i);
						}
						
					}else if(_type == 1){
						
						if(targetHero.isHost != _hero.isHost){
							
							_heroArr.add(targetHero);
							
							if(_directionArr != null){
								
								_directionArr.add(i);
							}
						}
						
					}else{
						
						if(targetHero.isHost == _hero.isHost){
							
							_heroArr.add(targetHero);
							
							if(_directionArr != null){
								
								_directionArr.add(i);
							}
						}
					}
				}
			}
		
		}else if(_hero.csv.heroType.attackType == 2){
			
			loop2:for(int i = 0 ; i < 6 ; i++){
				
				int nowPos = _hero.pos;
				
				for(int m = 0 ; m < _hero.csv.heroType.attackRange ; m++){
					
					int pos = _neighbourPosMap.get(nowPos)[i];
					
					if(pos == -1){
						
						continue loop2;
						
					}else{
						
						BattleHero targetHero = _heroDic.get(pos);
						
						if(targetHero != null){
							
							if(_type == 0){
								
								_heroArr.add(targetHero);
								
								if(_directionArr != null){
								
									_directionArr.add(i);
								}
								
							}else if(_type == 1){
								
								if(targetHero.isHost != _hero.isHost){
									
									_heroArr.add(targetHero);
									
									if(_directionArr != null){
										
										_directionArr.add(i);
									}
								}
								
							}else{
								
								if(targetHero.isHost == _hero.isHost){
									
									_heroArr.add(targetHero);
									
									if(_directionArr != null){
										
										_directionArr.add(i);
									}
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
	}
	
	public static int checkAttackType(ArrayList<Integer> _data){
		
		int length = _data.size();
		
		if(length == 1){
			
			return 0;
			
		}else if(length == 2){
			
			int r = Math.abs(_data.get(0) - _data.get(1));
			
			if(r == 3){
				
				return 3;
				
			}else if(r == 1 || r == 5){
				
				return 0;
				
			}else{
				
				return 2;
			}
			
		}else if(length == 3){
			
			int r = ((2 << _data.get(0)) >> 1) + ((2 << _data.get(1)) >> 1) + ((2 << _data.get(2)) >> 1);
			
			switch(r){
			
				case 21:
				case 42:
					
					return 4;
					
				case 7:
				case 14:
				case 28:
				case 56:
				case 49:
				case 35:
					
					return 2;
					
				default:
					
					return 3;	
			}
			
		}else if(length == 4){
			
			int r = ((2 << _data.get(0)) >> 1) + ((2 << _data.get(1)) >> 1) + ((2 << _data.get(2)) >> 1) + ((2 << _data.get(3)) >> 1);
			
			switch(r){
			
				case 29:
				case 58:
				case 53:
				case 43:
				case 23:
				case 46:
				
					return 4;
					
				default:
					
					return 3;	
			}
			
		}else{
			
			return 4;
		}
	}
	
	public static int[] getDamageArr(BattleHero _attackHero, ArrayList<BattleHero> _beAttackHeroArr){
		
		int[] result = new int[_beAttackHeroArr.size()];
		
		int[] hpArr = new int[result.length];
		
		int atk = _attackHero.getAtk();
		
		int allHp = 0;
		
		int[] weightArr = new int[result.length];
		
		int allWeight = 0;
		
		if(_beAttackHeroArr.size() == 0){
			
			result[0] = _beAttackHeroArr.get(0).hp;
			
		}else{
			
			int index = 0;
		
			for(BattleHero hero : _beAttackHeroArr){
				
				allHp = allHp + hero.hp;
				
				hpArr[index] = hero.hp;
				
				int weight = hero.hp * hero.getHpWeight();
				
				allWeight = allWeight + weight;
				
				weightArr[index] = weight;
				
				index++;
			}
			
			if(atk < allHp){
				
				while(atk > 0){
					
					if(allWeight > 0){
					
						int tmpInt = (int)(Math.random() * allWeight);
						
						index = 0;
						
						for(BattleHero hero : _beAttackHeroArr){
							
							int nowWeight = weightArr[index];
							
							if(tmpInt < nowWeight){
								
								result[index] = result[index] + 1;
								
								weightArr[index] = weightArr[index] - hero.getHpWeight();
								
								allWeight = allWeight - hero.getHpWeight();
								
								atk = atk - 1;
								
								allHp = allHp - 1;//因为考虑到可能出现剩下的英雄权重全部为0的情况  所以血量也进行了计算
								
								hpArr[index] = hpArr[index] - 1;
								
								break;
								
							}else{
								
								tmpInt = tmpInt - nowWeight;
								
								index++;
							}
						}
						
					}else{//走到这里只有一种可能性 那就是剩下有血的英雄全部是权重为0的了  那就按血量来取了
						
						int tmpInt = (int)(Math.random() * allHp);
						
						for(int i = 0 ; i < hpArr.length ; i++){
							
							int nowHp = hpArr[i];
							
							if(tmpInt < nowHp){
								
								result[i] = result[i] + 1;
								
								atk = atk - 1;
								
								allHp = allHp - 1;
								
								hpArr[i] = hpArr[i] - 1;
								
								break;
								
							}else{
								
								tmpInt = tmpInt - nowHp;
							}
						}
					}
				}
				
			}else{
				
				for(int i = 0 ; i < hpArr.length ; i++){
					
					result[i] = hpArr[i];
				}
			}
		}
		
		return result;
	}
	
	private static boolean checkSkillCondition(BattleHero _hero, int _condition, int _arg){
		
		switch(_condition){
		
			case 0:
				
				return true;
				
			case 1:
				
				return _hero.moved;
				
			case 2:
				
				return !_hero.moved;
				
			case 3:
				
				return _hero.power > _arg;
				
			case 4:
				
				return _hero.power < _arg;
				
			case 5:
				
				return _hero.hp > _arg;
				
			default:
				
				return _hero.hp < _arg;
		}
	}
	
	private static boolean checkSkillTargetCondition(BattleHero _hero, int _condition, int _arg){
		
		switch(_condition){
		
			case 0:
				
				return true;
				
			case 1:
				
				return _hero.power > _arg;
				
			case 2:
				
				return _hero.power < _arg;
				
			case 3:
				
				return _hero.hp > _arg;
				
			case 4:
				
				return _hero.hp < _arg;
				
			case 5:
				
				return _hero.hp < _hero.csv.maxHp;
				
			case 6:
				
				return _hero.hp == _hero.csv.maxHp;
				
			case 7:
				
				return _hero.csv.heroType.moveType != 0;
				
			case 8:
				
				return _hero.csv.heroType.moveType == 0;
				
			case 9:
				
				return _hero.csv.atk > _arg;
				
			case 10:
				
				return _hero.csv.atk < _arg;
				
			case 11:
				
				return _hero.csv.star > _arg;
				
			case 12:
				
				return _hero.csv.star < _arg;
		}
		
		return false;
	}
	
	private static void getSkillTargetArr(int _skillTarget, HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, BattleHero> _heroMap, BattleHero _hero, ArrayList<BattleHero> _resultArr){
		
		switch(_skillTarget){
		
			case 0:
				
				_resultArr.add(_hero);
				
				break;
				
			case 1:
				
				getHerosAndDirectionInRange(_neighbourPosMap, _heroMap, _hero, 1, _resultArr, null);
				
				break;
				
			case 2:
				
				getHerosAndDirectionInRange(_neighbourPosMap, _heroMap, _hero, 2, _resultArr, null);
				
				break;
				
			default:
					
				getHerosAndDirectionInRange(_neighbourPosMap, _heroMap, _hero, 0, _resultArr, null);
				
				break;
		}
	}
	
	public static int[][] castSkill(HashMap<Integer, int[]> _neighbourPosMap, HashMap<Integer, BattleHero> _heroMap, BattleHero _hero, int _index){
		
		int[][] resultArr = null;
		
		boolean result = checkSkillCondition(_hero, _hero.csv.skillCondition[_index], _hero.csv.skillConditionArg[_index]);
		
		if(result){
			
			ArrayList<BattleHero> skillTargetHeroArr = new ArrayList<>();
			
			int skillTarget = _hero.csv.skillTarget[_index];
			
			getSkillTargetArr(skillTarget, _neighbourPosMap, _heroMap, _hero, skillTargetHeroArr);
			
			if(skillTargetHeroArr.size() > 0){
					
				if(_hero.csv.skillTargetArg[_index] < skillTargetHeroArr.size()){
					
					int allWeight = 0;
					
					ArrayList<Integer> weightList = new ArrayList<>();
					
					ArrayList<BattleHero> tmpHeroList = new ArrayList<>();
					
					for(BattleHero hero : skillTargetHeroArr){
						
						allWeight = allWeight + hero.csv.beSkillTargetWeight;
						
						weightList.add(hero.csv.beSkillTargetWeight);
					}
					
					for(int i = 0 ; i < _hero.csv.skillTargetArg[_index] ; i++){
						
						if(allWeight > 0){
						
							int rand = (int)(Math.random() * allWeight);
							
							for(int m = 0 ; m < skillTargetHeroArr.size() ; m++){
								
								int nowWeight = weightList.get(m);
								
								if(rand < nowWeight){
									
									BattleHero hero = skillTargetHeroArr.remove(m);
									
									tmpHeroList.add(hero);
									
									weightList.remove(m);
									
									allWeight = allWeight - hero.csv.beSkillTargetWeight;
									
									break;
									
								}else{
									
									rand = rand - nowWeight;
								}
							}
							
						}else{//走到这里只可能剩下的所有英雄权重全部等于0  这样的话就直接随机取英雄了
							
							int rand = (int)(Math.random() * skillTargetHeroArr.size());
							
							BattleHero hero = skillTargetHeroArr.get(rand);
							
							tmpHeroList.add(hero);
							
							skillTargetHeroArr.remove(rand);
						}
					}
					
					skillTargetHeroArr = tmpHeroList;
				}
				
				resultArr = new int[skillTargetHeroArr.size() + 1][];
				
				resultArr[0] = new int[]{_hero.pos};
				
				Iterator<BattleHero> iter2 = skillTargetHeroArr.iterator();
				
				int index = 1;
				
				while(iter2.hasNext()){
					
					BattleHero targetHero = iter2.next();
					
					ArrayList<Integer> tmpArr = new ArrayList<>();
					
					for(int m = 0 ; m < _hero.csv.targetCondition[_index].length ; m++){
						
						if(checkSkillTargetCondition(targetHero,_hero.csv.targetCondition[_index][m],_hero.csv.targetConditionArg[_index][m])){
							
							for(int i = 0 ; i < _hero.csv.skillEffect[_index][m].length ; i++){
							
								castSkillEffect(_hero, targetHero, _hero.csv.effectTarget[_index][m][i], _hero.csv.skillEffect[_index][m][i], _hero.csv.skillEffectArg[_index][m][i], tmpArr);
							}
						}
					}
					
					resultArr[index] = new int[tmpArr.size() + 1];
					
					resultArr[index][0] = targetHero.pos;
					
					int i = 1;
					
					for(int data : tmpArr){
						
						resultArr[index][i] = data;
						
						i++;
					}
					
					index++;
				}
			}
		}
		
		return resultArr;
	}
		
	private static void castSkillEffect(BattleHero _hero, BattleHero _targetHero,boolean _effectTarget, int _effect, int[] _effectArg, ArrayList<Integer> _resultArr){
		
		switch(_effect){
		
		case 1:
			
			if(_effectTarget){
					
				_targetHero.isSilent = true;
				
				_resultArr.add(1);
				_resultArr.add(0);
				
			}else{
				
				_hero.isSilent = true;
				
				_resultArr.add(101);
				_resultArr.add(0);
			}
			
			break;
		
		case 2:
			
			if(_effectTarget){
				
				int data = _effectArg[0];
				
				_targetHero.hpChange = _targetHero.hpChange + data;
				
				_resultArr.add(2);
				_resultArr.add(data);
				
			}else{
				
				int data = _effectArg[0];
				
				_hero.hpChange = _hero.hpChange + data;
				
				_resultArr.add(102);
				_resultArr.add(data);
			}
			
			break;
			
		case 3:
			
			if(_effectTarget){
				
				int data = _effectArg[0];
				
				_targetHero.atkFix = _targetHero.atkFix + data;
				
				_resultArr.add(3);
				_resultArr.add(data);
				
			}else{
				
				int data = _effectArg[0];
				
				_hero.atkFix = _hero.atkFix + data;
				
				_resultArr.add(103);
				_resultArr.add(data);
			}
			
			break;
			
		case 4:
			
			if(_effectTarget){
				
				int data = _effectArg[0];
				
				_targetHero.maxHpFix = _targetHero.maxHpFix + data;
				_targetHero.hpChange = _targetHero.hpChange + data;
				
				_resultArr.add(4);
				_resultArr.add(data);
				
			}else{
				
				int data = _effectArg[0];
				
				_hero.maxHpFix = _hero.maxHpFix + data;
				_hero.hpChange = _hero.hpChange + data;
				
				_resultArr.add(104);
				_resultArr.add(data);
			}
			
			break;
			
		case 5:
			
			if(_effectTarget){
				
				int damage = _targetHero.csv.atk * _effectArg[1] + _effectArg[0];
				
				damage = damage > 0 ? 0 : damage;
				
				_targetHero.hpChange = _targetHero.hpChange + damage;
				
				_resultArr.add(2);
				_resultArr.add(damage);
				
			}else{
				
				int damage = _hero.csv.atk * _effectArg[1] + _effectArg[0];
				
				damage = damage > 0 ? 0 : damage;
				
				_hero.hpChange = _hero.hpChange + damage;
				
				_resultArr.add(102);
				_resultArr.add(damage);
			}
			
			break;
			
		case 6:
			
			if(_effectTarget){
				
				_targetHero.atkFix = _targetHero.atkFix + _hero.csv.atk;
				
				_resultArr.add(3);
				_resultArr.add(_hero.csv.atk);
				
			}else{
				
				_hero.atkFix = _hero.atkFix + _targetHero.csv.atk;
				
				_resultArr.add(103);
				_resultArr.add(_targetHero.csv.atk);
			}
			
			break;
			
		case 7:
			
			if(_effectTarget){
				
				int data = _hero.csv.atk - _targetHero.csv.atk;
			
				_targetHero.atkFix = _targetHero.atkFix + data;
				
				_resultArr.add(3);
				_resultArr.add(data);
				
			}else{
				
				int data = _targetHero.csv.atk - _hero.csv.atk;
				
				_hero.atkFix = _hero.atkFix + data;
				
				_resultArr.add(103);
				_resultArr.add(data);
			}
			
			break;
			
		case 8:
			
			if(_effectTarget){
				
				_targetHero.beAtkWeightFix = _targetHero.beAtkWeightFix * _effectArg[0] * 0.01f;
				
				_resultArr.add(6);
				_resultArr.add(_effectArg[0]);
				
			}else{
				
				_hero.beAtkWeightFix = _hero.beAtkWeightFix * _effectArg[0] * 0.01f;
				
				_resultArr.add(106);
				_resultArr.add(_effectArg[0]);
			}
			
			break;
			
		case 9:
			
			if(_effectTarget){
				
				_targetHero.die = true;
				
				_resultArr.add(7);
				_resultArr.add(0);
				
			}else{
				
				_hero.die = true;
				
				_resultArr.add(107);
				_resultArr.add(0);
			}

			break;
		}
		
	}
}

