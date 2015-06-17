package data.dataCsv.hero;

import java.util.HashMap;

import data.dataCsv.Csv;

public class Csv_heroType extends Csv{

	public static HashMap<Integer, Csv_heroType> dic = new HashMap<>();

	public boolean canAttack;
	public int attackType;
	public int attackRange;
	public int moveType;
}
