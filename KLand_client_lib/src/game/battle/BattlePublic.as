package game.battle
{
	

	public class BattlePublic
	{
		public static function getDirect(_mapWidth:int,_pos:int,_target:int):int{
			
			var dis:int = _target - _pos;
			
			switch(dis){
				
				case 1:
					
					return 1;
						
				case -1:
					
					return 4;
						
				case _mapWidth:
					
					return 2;
						
				case -_mapWidth:
					
					return 5;
						
				case _mapWidth - 1:
					
					return 3;
						
				default:
					
					return 0;
			}
		}
		
		public static function getTargetPos(_mapWidth:int,_pos:int,_target:int):int{
			
			switch(_target){
				
				case 0:
					
					return _pos - _mapWidth + 1;
					
				case 1:
					
					return _pos + 1;
					
				case 2:
					
					return _pos + _mapWidth;
					
				case 3:
					
					return _pos + _mapWidth - 1;
					
				case 4:
					
					return _pos - 1;
					
				default:
					
					return _pos - _mapWidth;
			}
		}
		
		public static function getAttackerPos(_mapWidth:int,_pos:int,_num:int,_attackerVec:Vector.<int>):Vector.<int>{
			
			if(_num == 2){
				
				for(var i:int = 0 ; i < _attackerVec.length ; i++){
					
					var dir1:int = getDirection(_mapWidth,_pos,_attackerVec[i]);
					
					for(var m:int = 0 ; m < _attackerVec.length ; m++){
						
						if(i == m){
							
							continue;
						}
						
						var dir2:int = getDirection(_mapWidth,_pos,_attackerVec[m]);
						
						var result:int = Math.abs(dir1 - dir2);
						
						if(result != 1 && result != 5){
							
							var vec:Vector.<int> = new Vector.<int>(2,true);
							
							vec[0] = _attackerVec[i];
							vec[1] = _attackerVec[m];
							
							return vec;
						}
					}
				}
				
			}else if(_num == 3){
			
				for(i = 0 ; i < _attackerVec.length ; i++){
					
					dir1 = getDirection(_mapWidth,_pos,_attackerVec[i]);
					
					for(m = 0 ; m < _attackerVec.length ; m++){
						
						if(i == m){
							
							continue;
						}
						
						dir2 = getDirection(_mapWidth,_pos,_attackerVec[m]);
						
						result = Math.abs(dir1 - dir2);
						
						if(result == 3){
							
							vec = new Vector.<int>(2,true);
							
							vec[0] = _attackerVec[i];
							vec[1] = _attackerVec[m];
							
							return vec;
						}
					}
				}
			
			}else{
				
				for(i = 0 ; i < _attackerVec.length ; i++){
					
					dir1 = getDirection(_mapWidth,_pos,_attackerVec[i]);
					
					for(m = 0 ; m < _attackerVec.length ; m++){
						
						if(m == i){
							
							continue;
						}
						
						dir2 = getDirection(_mapWidth,_pos,_attackerVec[m]);
						
						for(var n:int = 0 ; n < _attackerVec.length ; n++){
							
							if(n == m || n == i){
								
								continue;
							}
							
							var dir3:int = getDirection(_mapWidth,_pos,_attackerVec[n]);
							
							result = Math.pow(2,dir1) + Math.pow(2,dir2) + Math.pow(2,dir3);
							
							if(result == 21 || result == 42){
								
								vec = new Vector.<int>(3,true);
								
								vec[0] = _attackerVec[i];
								vec[1] = _attackerVec[m];
								vec[2] = _attackerVec[n];
								
								return vec;
							}
						}
					}
				}
			}
			
			return vec;
		}
		
		private static function getDirection(_mapWidth:int,_pos:int,_target:int):int{
			
			var dis:int = _target - _pos;
			
			if(dis % _mapWidth == 0){
				
				if(dis > 0){
					
					return 2;
					
				}else{
					
					return 5;
				}
				
			}else if(dis % (_mapWidth - 1) == 0){
				
				if(dis > 0){
					
					return 3;
					
				}else{
					
					return 0;
				}
				
			}else{
				
				if(dis > 0){
					
					return 1;
					
				}else{
					
					return 4;
				}
			}
		}
	}
}