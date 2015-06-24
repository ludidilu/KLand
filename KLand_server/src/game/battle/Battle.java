package game.battle;

import game.gameAi.GameAi;
import game.gameQueue.GameQueue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import publicTools.PublicTools;
import superService.SuperService;
import userService.UserService;
import data.dataCsv.ai.Csv_ai;
import data.dataCsv.battle.Csv_battle;
import data.dataCsv.battle.Csv_battleAi;
import data.dataCsv.hero.Csv_hero;
import data.dataMap.Map;
import data.dataMap.MapUnit;

public class Battle extends SuperService{

	private static HashMap<String, Method> methodMap;
	
	public static void init() throws Exception{
		
		methodMap = new HashMap<>();
		
		methodMap.put("init",Battle.class.getDeclaredMethod("init",int.class,UserService.class,UserService.class,int.class));
		methodMap.put("getBattleData", Battle.class.getDeclaredMethod("getBattleData", UserService.class));
		methodMap.put("sendBattleAction", Battle.class.getDeclaredMethod("sendBattleAction", UserService.class, int[][].class, int[][].class));
		methodMap.put("quitBattle", Battle.class.getDeclaredMethod("quitBattle", UserService.class));
	}
	
	protected HashMap<String, Method> getMethodMap(){
		
		return methodMap;
	}
	
	private static int START_CARDS_NUM = 4;
	private static int MAX_CARDS_NUM = 5;
	private static int MAX_MONEY = 5;
	private static int START_MONEY = 3;
	public static int POWER_CAN_MOVE = 2;
	
	private UserService service1;
	private UserService service2;
	
	private boolean actionState = true;
	
	private int mapID;
	private MapUnit mapUnit;
	private HashMap<Integer, Integer> map;
	private HashMap<Integer, BattleHero> heroMap;
	private ArrayList<Integer> userAllCards1;
	private ArrayList<Integer> userAllCards2;
	private HashMap<Integer, Integer> userCards1;
	private HashMap<Integer, Integer> userCards2;
	
	private int score1;
	private int score2;
	
	private int maxRound;
	private int nowRound;
	
	private int aiMoney;
	
	private int uidIndex = 1;
	
	public Battle(){
		
		heroMap = new HashMap<>();
		
		userCards1 = new HashMap<>();
		userCards2 = new HashMap<>();
	}
	
	public void init(int _battleID,UserService _service1, UserService _service2, int _aiID){
		
		service1 = _service1;
		
		if(_service2 != null){
		
			service2 = _service2;
		}
		
		initBattle(_battleID,_aiID);
		
		service1.process("enterBattle",this);
		
		if(service2 != null){
		
			service2.process("enterBattle",this);
		}
	}
	
	private void initBattle(int _battleID,int _aiID){
		
		if(service2 != null){
		
			Csv_battle csv_battle = Csv_battle.dic.get(_battleID);
			
			mapID = csv_battle.mapID;
			
			mapUnit = Map.getMapUnit(mapID);
			
			maxRound = csv_battle.roundNum;
			
			userAllCards1 = PublicTools.getSomeOfArr(service1.userData.heros,csv_battle.cardsNum);
			
			userAllCards2 = PublicTools.getSomeOfArr(service2.userData.heros,csv_battle.cardsNum);
			
		}else{
			
			Csv_battleAi csv_battleAi = Csv_battleAi.dic.get(_battleID);
			
			mapID = csv_battleAi.mapID;
			
			mapUnit = Map.getMapUnit(mapID);
			
			maxRound = csv_battleAi.roundNum;
			
			userAllCards1 = PublicTools.getSomeOfArr(service1.userData.heros,csv_battleAi.cardsNum);
			
			Csv_ai csv_ai = Csv_ai.dic.get(_aiID);
			
			int[] aiHeros = csv_ai.heros;
			
			userAllCards2 = new ArrayList<>();
			
			for(int heroID : aiHeros){
				
				userAllCards2.add(heroID);
			}
			
			userAllCards2 = PublicTools.getSomeOfArr(userAllCards2,csv_battleAi.aiCardsNum);
			
			aiMoney = csv_ai.money;
			
			if(csv_battleAi.defaultHeros.length > 0){
			
				for(int i = 0 ; i < csv_battleAi.defaultHeros.length ; i++){
					
					int pos = csv_battleAi.defaultHeros[i][0];
					int heroID = csv_battleAi.defaultHeros[i][1];
					
					BattleHero hero = new BattleHero();
					
					hero.csv = Csv_hero.dic.get(heroID);
					
					if(mapUnit.dic.get(pos) == 1){
					
						hero.isHost = true;
						
					}else{
						
						hero.isHost = false;
					}
					
					hero.pos = pos;
					
					hero.hp = hero.csv.maxHp;
					
					hero.power = hero.csv.maxPower;
					
					heroMap.put(pos, hero);
				}
			}
		}
		
		nowRound = 1;
		
		map = (HashMap<Integer, Integer>)mapUnit.dic.clone();
		
		score1 = mapUnit.score1;
		
		score2 = mapUnit.score2;
		
		for(int i = 0 ; i < START_CARDS_NUM ; i++){
			
			if(userAllCards1.size() > 0){
			
				int uid = getUid();
				
				userCards1.put(uid,userAllCards1.remove(0));
			}
			
			if(userAllCards2.size() > 0){
			
				int uid = getUid();
				
				userCards2.put(uid,userAllCards2.remove(0));
			}
		}
	}
	
	private int getUid(){
		
		int uid = uidIndex;
		
		uidIndex++;
		
		return uid;
	}
	
	public void getBattleData(UserService _service){
		
		Iterator<Entry<Integer, Integer>> iter = map.entrySet().iterator();
		
		int[] mapData = new int[score1];
		
		int index = 0;
		
		while(iter.hasNext()){
			
			Entry<Integer, Integer> entry = iter.next();
			
			int pos = entry.getKey();
			
			int type = entry.getValue();
			
			if(type == 1){
			
				mapData[index] = pos;
				
				index++;
			}
		}
		
		HashMap<Integer, Integer> cards;
		int oppCardsNum;
		
		if(_service == service1){
			
			cards = userCards1;
			oppCardsNum = userCards2.size();
			
		}else{
			
			cards = userCards2;
			oppCardsNum = userCards1.size();
		}
		
		int[][] cardsData = null;
		
		if(cards.size() > 0){
		
			cardsData = new int[cards.size()][];
			
			iter = cards.entrySet().iterator();
			
			index = 0;
			
			while(iter.hasNext()){
				
				Entry<Integer, Integer> entry = iter.next();
				
				cardsData[index] = new int[]{entry.getKey(),entry.getValue()};
				
				index++;
			}
		}
		
		int[][] heroData = null;
		
		if(heroMap.size() > 0){
			
			heroData = new int[heroMap.size()][];
			
			index= 0;
			
			Iterator<Entry<Integer, BattleHero>> iter3 = heroMap.entrySet().iterator();
			
			while(iter3.hasNext()){
				
				Entry<Integer, BattleHero> entry = iter3.next();
				
				int key = entry.getKey();
				BattleHero hero = entry.getValue();
				
				int[] oneHeroData = new int[]{key,hero.csv.id,hero.hp,hero.power};
				
				heroData[index] = oneHeroData;
				
				index++;
			}
		}
		
		boolean isHost = _service == service1;
		
		_service.process("getBattleDataOK", isHost, nowRound, maxRound, mapID, mapData, cardsData, oppCardsNum, userAllCards1.size(), userAllCards2.size(), heroData, actionState);
	}
	
	public void sendBattleAction(UserService _service, int[][] _moveData, int[][] _summonData){
		
		boolean isHost = _service == service1;
		
		if(isHost != actionState){
			
			_service.process("sendMsg", "sendBattleAction error!");
			
			return;
		}
		
		HashMap<Integer, Integer> moveData = null;
		HashMap<Integer, Integer> summonData = null;
		
		if(_moveData != null){
			
			moveData = new HashMap<>();
			
			for(int i = 0 ; i < _moveData.length ; i++){
				
				moveData.put(_moveData[i][0], _moveData[i][1]);
			}
		}
		
		if(_summonData != null){
			
			summonData = new HashMap<>();
			
			for(int i = 0 ; i < _summonData.length ; i++){
				
				summonData.put(_summonData[i][0], _summonData[i][1]);
			}
		}
		
		showBattle(summonData,moveData);
	}

	private void showBattle(HashMap<Integer, Integer> _summonData,HashMap<Integer, Integer> _moveData){

		battleStart(_summonData,_moveData);
		
		actionState = !actionState;
		
		nowRound++;
		
		if(nowRound > maxRound){
			
			if(score1 > score2){
				
				service1.process("leaveBattle", 1);
				
				if(service2 != null){
					
					service2.process("leaveBattle", 2);
				}
				
			}else if(score1 < score2){
				
				service1.process("leaveBattle", 2);
				
				if(service2 != null){
				
					service2.process("leaveBattle", 1);
				}
				
			}else{
				
				service1.process("leaveBattle", 3);
				
				if(service2 != null){
				
					service2.process("leaveBattle", 3);
				}
			}
			
			battleOver();
			
		}else{
			
			if(score1 == 0){
				
				service1.process("leaveBattle", 2);
				
				if(service2 != null){
				
					service2.process("leaveBattle", 1);
				}
				
				battleOver();
				
			}else if(score2 == 0){
				
				service1.process("leaveBattle", 1);
				
				if(service2 != null){
				
					service2.process("leaveBattle", 2);
				}
				
				battleOver();
				
			}else{
				
				if(service2 == null && !actionState){
					
					HashMap<Integer, Integer> summonData = new HashMap<>();
					HashMap<Integer, Integer> moveData = new HashMap<>();
					
					BattleAI.start(mapUnit,map,heroMap,userCards2,aiMoney,summonData,moveData);
					
					showBattle(summonData,moveData);
				}
			}
		}
	}
	
	private void battleStart(HashMap<Integer, Integer> _summonData, HashMap<Integer, Integer> _moveData){
		
		HashMap<Integer, Integer> userCards;
		HashMap<Integer, Integer> oppUserCards;
		ArrayList<Integer> oppAllUserCards;
		UserService service;
		UserService oppService;
		int mapState;
		
		if(actionState){
			
			userCards = userCards1;
			oppUserCards = userCards2;
			oppAllUserCards = userAllCards2;
			service = service1;
			oppService = service2;
			mapState = 1;
			
		}else{
			
			userCards = userCards2;
			oppUserCards = userCards1;
			oppAllUserCards = userAllCards1;
			service = service2;
			oppService = service1;
			mapState = 2;
		}
		
		int money;
		
		if(service != null){
			
			if(actionState){
				
				money = START_MONEY + (nowRound - 1) / 2;
				
				if(money > MAX_MONEY){
					
					money = MAX_MONEY;
				}
				
			}else{
				
				money = MAX_MONEY;
			}
			
		}else{
			
			money = aiMoney;
		}
		
		int[][] summonResult = null;
		
		if(_summonData != null){
			
			ArrayList<int[]> summonArr = null;
			
			Iterator<Entry<Integer, Integer>> iter = _summonData.entrySet().iterator();
			
			while(iter.hasNext()){
				
				Entry<Integer, Integer> entry = iter.next();
				
				int uid = entry.getKey();
				int pos = entry.getValue();
				
				if(!userCards.containsKey(uid)){
					
					service.process("sendMsg", "BattleError 1");
					
					continue;
				}
				
				int cardID = userCards.get(uid);
				
				Csv_hero heroCsv = Csv_hero.dic.get(cardID);
				
				if(heroCsv.star > money){
					
					service.process("sendMsg", "BattleError 2");
					
					continue;
				}
				
				if(!map.containsKey(pos)){
					
					service.process("sendMsg", "BattleError 3");
					
					continue;
				}
				
				if(map.get(pos) != mapState){
					
					service.process("sendMsg", "BattleError 4");
					
					continue;
				}
				
				if(heroMap.containsKey(pos)){
					
					service.process("sendMsg", "BattleError 5");
					
					continue;
				}
				
				userCards.remove(uid);
				
				BattleHero hero = new BattleHero();
				
				hero.csv = heroCsv;
				
				hero.isHost = actionState;
				
				hero.pos = pos;
				
				hero.hp = heroCsv.maxHp;
				
				hero.power = heroCsv.maxPower;
				
				hero.isJustSummon = true;
				
				heroMap.put(pos, hero);
				
				money = money - heroCsv.star;
				
				if(summonArr == null){
					
					summonArr = new ArrayList<>();
				}
				
				summonArr.add(new int[]{uid,cardID,pos});
			}
			
			if(summonArr != null){
				
				summonResult = new int[summonArr.size()][];
				
				summonArr.toArray(summonResult);
			}
		}
		
		//----检测移动合法性----
		int[][] moveResult = null;
		
		if(_moveData != null){
			
			Iterator<Entry<Integer, Integer>> iter = _moveData.entrySet().iterator();
			
			while(iter.hasNext()){
				
				Entry<Integer, Integer> entry = iter.next();
				
				if(money < 1){
					
					service.process("sendMsg", "BattleError 11");
					
					iter.remove();
					
					continue;
				}
				
				money = money - 1;
				
				int pos = entry.getKey();
				int direct = entry.getValue();
				
				BattleHero hero = heroMap.get(pos);
				
				if(hero == null){
					
					service.process("sendMsg", "BattleError 6");
					
					iter.remove();
					
					continue;
				}
				
				if(hero.isHost != actionState){
					
					service.process("sendMsg", "BattleError 7");
					
					iter.remove();
					
					continue;
				}
				
				if(hero.isJustSummon || hero.power < POWER_CAN_MOVE || hero.csv.heroType.moveType == 0){
					
					service.process("sendMsg", "BattleError 8");
					
					iter.remove();
					
					continue;
				}

				int targetPos = mapUnit.neighbourPosMap.get(pos)[direct];
				
				if(targetPos != -1){
					
					entry.setValue(targetPos);
					
				}else{
					
					service.process("sendMsg", "BattleError 9");
					
					iter.remove();
				}
			}
		}
		//--------
		
		//----真正开始移动了----
		if(_moveData != null){
			
			ArrayList<Integer> resultPosArr = new ArrayList<>();
			ArrayList<Integer> resultTargetArr = new ArrayList<>();
			
			ArrayList<Integer> checkedPosArr = new ArrayList<>(); 
		
			Iterator<Integer> iter = _moveData.keySet().iterator();
			
			while(iter.hasNext()){
				
				int pos = iter.next();
				
				if(checkedPosArr.contains(pos)){
					
					continue;
				}
				
				ArrayList<Integer> tmpArr = new ArrayList<>();
				
				tmpArr.add(pos);
				
				int target;
				
				boolean result;
				
				while(true){
					
					target = _moveData.get(pos);
					
					if(heroMap.containsKey(target)){
						
						if(_moveData.containsKey(target)){
							
							int index = tmpArr.indexOf(target);
							
							if(index == -1){
								
								pos = target;
								
								tmpArr.add(pos);
								
							}else if(index == 0){
								
								result = true;
								
								break;
								
							}else{
								
								service.process("sendMsg", "BattleError 10");
									
								result = false;
								
								break;
							}
							
						}else{
							
							service.process("sendMsg", "BattleError 11");
							
							result = false;
							
							break;
						}
						
					}else{
						
						result = true;
						
						break;
					}
				}
				
				if(result){
					
					HashMap<Integer, BattleHero> tmpMap = new HashMap<>();
					
					Iterator<Integer> iter2 = tmpArr.iterator();
					
					while(iter2.hasNext()){
						
						pos = iter2.next();
						target = _moveData.get(pos);
						
						BattleHero hero = heroMap.remove(pos);
						
						hero.moved = true;
						
						hero.pos = target;
						
						checkedPosArr.add(pos);
						
						resultPosArr.add(pos);
						resultTargetArr.add(target);
						
						tmpMap.put(target, hero);
						
						if(hero.isHost && map.get(target) == 2){
							
							map.put(target, 1);
							
							score1++;
							
							score2--;
							
						}else if(!hero.isHost && map.get(target) == 1){
							
							map.put(target, 2);
							
							score1--;
							
							score2++;
						}
					}
					
					Iterator<Entry<Integer, BattleHero>> iter3 = tmpMap.entrySet().iterator();
					
					while(iter3.hasNext()){
						
						Entry<Integer, BattleHero> entry = iter3.next();
						
						heroMap.put(entry.getKey(), entry.getValue());
					}
					
				}else{
					
					Iterator<Integer> iter2 = tmpArr.iterator();
					
					while(iter2.hasNext()){
						
						pos = iter2.next();
						
						checkedPosArr.add(pos);
					}
				}
			}
			
			moveResult = new int[resultPosArr.size()][];
			
			for(int i = 0 ; i < resultPosArr.size() ; i++){
				
				moveResult[i] = new int[]{resultPosArr.get(i),resultTargetArr.get(i)};
			}
		}
		//--------
		
		//----开始使用技能----
		int[][][] skillResult = null;
		
		ArrayList<int[][]> skillData = new ArrayList<>();
		
		//----先找沉默技能----
		Iterator<BattleHero> iter = heroMap.values().iterator();
		
		while(iter.hasNext()){

			BattleHero hero = iter.next();

			if((hero.isJustSummon && hero.csv.heroType.moveType != 2) || hero.power == 0 || hero.csv.silentSkillIndexArr == null){
				
				continue;
			}
			
			for(int silentSkillIndex : hero.csv.silentSkillIndexArr){
				
				int[][] oneSkillData = BattlePublic.castSkill(mapUnit.neighbourPosMap, heroMap, hero, silentSkillIndex);
			
				if(oneSkillData != null){
				
					skillData.add(oneSkillData);
				}
			}
		}
		//--------
		
		//----使用其他技能----
		iter = heroMap.values().iterator();
		
		while(iter.hasNext()){

			BattleHero hero = iter.next();
			
			if((hero.isJustSummon && hero.csv.heroType.moveType != 2) || hero.power == 0 || hero.isSilent){
				
				continue;
			}
			
			for(int i = 0 ; i < hero.csv.skillTarget.length ; i++){
				
				if(hero.csv.silentSkillIndexArr != null && hero.csv.silentSkillIndexArr.contains(i)){
					
					continue;
				}
				
				int[][] oneSkillData = BattlePublic.castSkill(mapUnit.neighbourPosMap, heroMap, hero, i);
				
				if(oneSkillData != null){
					
					skillData.add(oneSkillData);
				}
			}
		}
		//--------
		
		//----技能使用后的结算----
		iter = heroMap.values().iterator();
		
		while(iter.hasNext()){

			BattleHero hero = iter.next();
			
			if(hero.die){
				
				iter.remove();
				
				continue;
			}
			
			if(hero.hpChange != 0){
			
				hero.hp = hero.hp + hero.hpChange;
				
				if(hero.hp < 1){
					
					iter.remove();
					
					continue;
				}
				
				if(hero.maxHpFix > 0){
					
					if(hero.hpChange <= 0){
						
						hero.maxHpFix = 0;
						
					}else if(hero.hpChange < hero.maxHpFix){
						
						hero.maxHpFix = hero.hpChange;
					}
				}
				
				if(hero.hp > hero.getMaxHp()){
					
					hero.hp = hero.getMaxHp();
				}
				
				hero.hpChange = 0;
			}
		}
		
		if(skillData.size() > 0){
			
			skillResult = new int[skillData.size()][][];
			
			skillData.toArray(skillResult);
		}
		//--------
		
		//----开始攻击----
		int[][][] attackResult = null;
		
		HashMap<BattleHero, HashMap<BattleHero, Integer>> attackMap = new HashMap<>();
		HashMap<BattleHero, HashMap<BattleHero, Integer>> beAttackMap = new HashMap<>();
		
		iter = heroMap.values().iterator();
		
		while(iter.hasNext()){

			BattleHero hero = iter.next();
			
			if(!hero.csv.heroType.canAttack || (hero.isJustSummon && hero.csv.heroType.moveType != 2) || hero.power == 0 || hero.getAtk() < 1){
				
				continue;
			}
			
			ArrayList<BattleHero> heroArr = new ArrayList<>();
			ArrayList<Integer> directionArr = new ArrayList<>();
			
			BattlePublic.getHerosAndDirectionInRange(mapUnit.neighbourPosMap, heroMap, hero, 1, heroArr, directionArr);
			
			if(heroArr.size() > 0){
				
				int[] damageArr = BattlePublic.getDamageArr(hero, heroArr);
			
				HashMap<BattleHero, Integer> damageHeroMap = new HashMap<>();
				
				attackMap.put(hero, damageHeroMap);
				
				for(int i = 0 ; i < heroArr.size() ; i++){
				
					BattleHero targetHero = heroArr.get(i);
					
					int direction = directionArr.get(i);
					
					damageHeroMap.put(targetHero, damageArr[i]);
					
					HashMap<BattleHero, Integer> tmpMap = beAttackMap.get(targetHero);
					
					if(tmpMap == null){
						
						tmpMap = new HashMap<>();
						
						beAttackMap.put(targetHero, tmpMap);
					}
						
					tmpMap.put(hero, direction);
				}
			}
		}
		
		if(beAttackMap.size() > 0){
			
			ArrayList<int[][]> attackResultArr = new ArrayList<>();
		
			Iterator<Entry<BattleHero, HashMap<BattleHero, Integer>>> iter2 = beAttackMap.entrySet().iterator();
			
			while(iter2.hasNext()){
				
				Entry<BattleHero, HashMap<BattleHero, Integer>> entry = iter2.next();
				
				BattleHero hero = entry.getKey();
				
				HashMap<BattleHero, Integer> tmpMap = entry.getValue();
				
				int[][] attackHeroData = new int[tmpMap.size() + 1][];
				
				attackHeroData[0] = new int[]{hero.pos,0};
				
				Iterator<Entry<BattleHero, Integer>> iter3 = tmpMap.entrySet().iterator();
				
				int index = 0;
				
				boolean needCheckHit = false;
				
				ArrayList<Integer> targetArr = null;
				
				if(hero.power > 0){
					
					needCheckHit = true;
					
					targetArr = new ArrayList<>();
				}
				
				while(iter3.hasNext()){
					
					Entry<BattleHero, Integer> entry2 = iter3.next();
					
					int target = entry2.getValue();
					BattleHero attackHero = entry2.getKey();
					
					int damage = attackMap.get(attackHero).get(hero);
					
					attackHeroData[index + 1] = new int[]{attackHero.pos, damage};
					
					hero.hpChange = hero.hpChange - damage;
					
					if(needCheckHit){
						
						int attackHeroNum = attackMap.get(attackHero).size();
						
						if(attackHeroNum == 1 && !hero.hasLosePower){
							
							hero.hasLosePower = true;
						}
						
						if(!targetArr.contains(target)){
							
							targetArr.add(target);
						}
					}
					
					index++;
				}
				
				if(needCheckHit){
				
					int result = BattlePublic.checkAttackType(targetArr);
						
					if(result == 0){
						
						if(hero.hasLosePower){
							
							hero.power = hero.power - 1;
							
							attackHeroData[0][1] = 1;
							
						}else{
							
							attackHeroData[0][1] = 0;
						}
						
					}else{
						
						if(!hero.hasLosePower){
						
							hero.hasLosePower = true;
						}
						
						hero.power = hero.power - result;
						
						if(hero.power < 0){
							
							hero.power = 0;
						}
						
						attackHeroData[0][1] = result;
					}
					
				}else{
					
					attackHeroData[0][1] = 0;
				}
				
				attackResultArr.add(attackHeroData);
			}
			
			attackResult = new int[attackResultArr.size()][][];
			
			attackResultArr.toArray(attackResult);
		}
		//--------
		
		//----重置英雄数据----
		iter = heroMap.values().iterator();
		
		while(iter.hasNext()){
			
			BattleHero hero = iter.next();
			
			if(hero.hpChange < 0){
			
				hero.hp = hero.hp + hero.hpChange;
				
				if(hero.hp < 1){
					
//					service1.process("sendMsg", hero.pos + "die!!!");
					
					iter.remove();
					
					continue;
				}
				
				if(hero.maxHpFix > 0){
					
					hero.maxHpFix = hero.maxHpFix + hero.hpChange;
					
					if(hero.maxHpFix < 0){
						
						hero.maxHpFix = 0;
					}
				}
				
				hero.hpChange = 0;
			}
			
			if(hero.maxHpFix > 0){
				
				hero.hp = hero.hp - hero.maxHpFix;
				
				hero.maxHpFix = 0;
			}
			
			if(hero.hp < hero.csv.maxHp && hero.power > 0){
				
				hero.hp = hero.hp + hero.power;
				
				if(hero.hp > hero.csv.maxHp){
					
					hero.hp = hero.csv.maxHp;
				}
			}
			
//			service1.process("sendMsg", hero.pos + ":" + hero.hp);
			
			if(hero.isSilent){
				
				hero.isSilent = false;
			}
			
			if(hero.isJustSummon){
				
				hero.isJustSummon = false;
			}
			
			if(hero.moved){
				
				hero.moved = false;
			}
			
			if(!hero.hasLosePower){
				
				if(hero.power < hero.csv.maxPower){
				
					hero.power++;
				}
				
			}else{
				
				hero.hasLosePower = false;
			}
			
			hero.atkFix = hero.maxHpFix = 0;
			
			hero.beAtkWeightFix = 1f;
		}
		
		int cardUid = -1;
		int cardID = -1;
		
		if(oppAllUserCards.size() > 0){
			
			cardID = oppAllUserCards.remove(0);
			
			if(oppUserCards.size() < MAX_CARDS_NUM){

				cardUid = getUid();
				
				oppUserCards.put(cardUid, cardID);
			}
		}
		//--------
		
		if(service != null){
		
			service.process("sendBattleActionOK", summonResult, moveResult, skillResult, attackResult, cardUid, cardUid == -1 ? cardID : -1);
		}
		
		if(oppService != null){
		
			oppService.process("playBattle", summonResult, moveResult, skillResult, attackResult, cardUid, cardID);
		}
	}
	
	public void battleOver(){
		
		actionState = true;
		
		service1 = null;
		
		mapUnit = null;
		map = null;
		
		userCards1.clear();
		userCards2.clear();
		
		userAllCards1.clear();
		userAllCards2.clear();
		
		heroMap.clear();
		
		uidIndex = 1;
		
		if(service2 != null){
			
			service2 = null;
			GameQueue.getInstance().process("battleOver", this);
			
		}else{
			
			GameAi.getInstance().process("battleOver", this);
		}
	}
	
	public void quitBattle(UserService _service){
		
		if(_service == service1){
			
			service1.process("quitBattleOK", true);
			
			if(service2 != null){
				
				service2.process("leaveBattle", 0);
			}
			
			battleOver();
			
		}else if(_service == service2){
			
			service2.process("quitBattleOK", true);
			
			service1.process("leaveBattle", 0);
			
			battleOver();
			
		}else{
			
			_service.process("quitBattleOK", false);
		}
	}
}
