package playerData;

import java.util.ArrayList;

import userData.UserDataUnit;

public class PlayerHeroData extends UserDataUnit{

	public ArrayList<Integer> heros;
	
	public void init(){
		
		heros = new ArrayList<>();
		
		heros.add(1);
		heros.add(2);
		heros.add(3);
	}
}
