package game.battle;

import data.dataCsv.hero.Csv_hero;

public class BattleHero {

	public boolean isHost;
	public Csv_hero csv;
	
	public int hp;
	public int power;
	
	public int pos;
	
	//º∆À„”√ Ù–‘
	public boolean isJustSummon = false;
	public boolean moved = false;
	public boolean isSilent = false;
	public boolean isStopMove = false;
	
	public int atkFix = 0;
	public int maxHpFix = 0;
//	public float defFix = 1;
	
	public int hpChange = 0;
	public boolean hasLosePower = false;
	
	public int getAtk(){
		
		return csv.atk + atkFix;
	}
	
	public int getMaxHp(){
		
		return csv.maxHp + maxHpFix;
	}
}
