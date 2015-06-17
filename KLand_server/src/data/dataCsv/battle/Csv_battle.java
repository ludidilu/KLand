package data.dataCsv.battle;

import java.util.HashMap;

import data.dataCsv.Csv;

public class Csv_battle extends Csv{

	public static HashMap<Integer, Csv_battle> dic = new HashMap<>();
	
	public int mapID;
	public int roundNum;
	public int cardsNum;
}
