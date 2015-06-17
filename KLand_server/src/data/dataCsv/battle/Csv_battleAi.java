package data.dataCsv.battle;

import java.util.HashMap;

import data.dataCsv.Csv;

public class Csv_battleAi extends Csv{

	public static HashMap<Integer, Csv_battleAi> dic = new HashMap<>();
	
	public int mapID;
	public int roundNum;
	public int cardsNum;
	public int[][] defaultHeros;
	public int[] ai;
}
