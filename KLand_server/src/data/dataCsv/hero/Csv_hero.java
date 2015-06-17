package data.dataCsv.hero;

import java.util.ArrayList;
import java.util.HashMap;

import data.dataCsv.Csv;

public class Csv_hero extends Csv{

	public static HashMap<Integer, Csv_hero> dic = new HashMap<>();
	
	public int type;
	public int star;
	public int maxHp;
	public int atk;
	public int maxPower;
	public int[] skillTarget;
	public int[] skillTargetArg;
	public int[] skillCondition;
	public int[] skillConditionArg;
	public int[][] targetCondition;
	public int[][] targetConditionArg;
	public boolean[][][] effectTarget;
	public int[][][] skillEffect;
	public int[][][][] skillEffectArg;
	
	public int beSkillTargetWeight;
	public int beAttackTargetWeight;
	
	public ArrayList<Integer> silentSkillIndexArr = null;
	
	public Csv_heroType heroType;
	
	public void fix(){
		
		for(int i = 0 ; i < skillTarget.length ; i++){
			
			for(int m = 0 ; m < skillEffect[i].length ; m++){
				
				for(int n = 0 ; n < skillEffect[i][m].length ; n++){
				
					if(skillEffect[i][m][n] == 1){
						
						if(silentSkillIndexArr == null){
							
							silentSkillIndexArr = new ArrayList<>();
						}
						
						silentSkillIndexArr.add(i);
					}
				}
			}
		}
		
		heroType = Csv_heroType.dic.get(type);
	}
}
