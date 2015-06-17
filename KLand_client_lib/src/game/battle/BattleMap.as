package game.battle
{
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.utils.Dictionary;
	
	import data.map.MapUnit;
	import data.resource.ResourcePublic;
	
	import starling.core.Starling;
	import starling.display.Image;
	import starling.display.Quad;
	import starling.display.Sprite;
	import starling.events.Touch;
	import starling.events.TouchEvent;
	import starling.events.TouchPhase;

	public class BattleMap extends Sprite
	{
		private static const FLIP_MAP:int = 2;//0不翻转 1水平翻转 2中心翻转
		
		private var bgContainer:Sprite;
		public var mapContainer:Sprite;
		private var selectedFrameContaienr:Sprite;
		
		private var unitWidth:Number;
		
		private var mapUnit:MapUnit;
		
		public var dic:Dictionary;
		
		private var blackFrame:Sprite;
		private var redFrameVec:Vector.<Sprite>;
		private var redFrameIndex:int;
		private var greenFrameVec:Vector.<Sprite>;
		private var greenFrameIndex:int;
		
		private var point1:Point = new Point;
		private var point2:Point = new Point;
		
		private var selectedUnit:BattleMapUnit;
		
		public var moveFun:Function;

		private var movePoint2:Point = new Point;
		private var movePoint1:Point = new Point;
		
		private var tmpPoint:Point = new Point;
		private var tmpPoint2:Point = new Point;
		
		private var tmpRect:Rectangle = new Rectangle;
		
		public function BattleMap()
		{
			unitWidth = ResourcePublic.getTexture("blackFrame").height * 0.5 + 2;
			
			bgContainer = new Sprite;
			
			var quad:Quad = new Quad(10000,10000);
			
			quad.x = -0.5 * quad.width;
			quad.y = -0.5 * quad.height;
			
			quad.alpha = 0;
			
			bgContainer.addChild(quad);
			
			bgContainer.x = -0.5 * Starling.current.backBufferWidth;
			bgContainer.y = -0.5 * Starling.current.backBufferHeight;
			
			bgContainer.flatten();
			
			addChild(bgContainer);
			
			mapContainer = new Sprite;
			
			mapContainer.touchable = false;
			
			addChild(mapContainer);
			
			bgContainer.addEventListener(TouchEvent.TOUCH,bgBeTouch);
			
			selectedFrameContaienr = new Sprite;
			
			selectedFrameContaienr.touchable = false;
			
			addChild(selectedFrameContaienr);
			
			blackFrame = new Sprite;
			
			var img:Image = new Image(ResourcePublic.getTexture("blackFrame"));
			
			img.x = -0.5 * img.width;
			img.y = -0.5 * img.height;
			
			blackFrame.addChild(img);
			blackFrame.flatten();
			
			blackFrame.visible = false;
			
			selectedFrameContaienr.addChild(blackFrame);
			
			redFrameVec = new Vector.<Sprite>(6,true);
			greenFrameVec = new Vector.<Sprite>(6,true);
			
			for(var i:int = 0 ; i < 6 ; i++){
				
				var frame:Sprite = new Sprite;
				
				img = new Image(ResourcePublic.getTexture("redFrame"));
				
				img.x = -0.5 * img.width;
				img.y = -0.5 * img.height;
				
				frame.addChild(img);
				frame.flatten();
				
				redFrameVec[i] = frame;
				
				frame = new Sprite;
				
				img = new Image(ResourcePublic.getTexture("greenFrame"));
				
				img.x = -0.5 * img.width;
				img.y = -0.5 * img.height;
				
				frame.addChild(img);
				frame.flatten();
				
				greenFrameVec[i] = frame;
			}
		}
		
		public function start(_mapUnit:MapUnit,_dic:Dictionary):void{
			
			mapContainer.unflatten();
			
			dic = new Dictionary;
			
			mapUnit = _mapUnit;
			
			for(var i:int = 0 ; i < mapUnit.mapHeight ; i++){
				
				for(var m:int = 0 ; m < mapUnit.mapWidth ; m++){
					
					if(m == mapUnit.mapWidth - 1 && i % 2 == 1){
						
						break;
					}
					
					var id:int = i * mapUnit.mapWidth - int(i * 0.5) + m;
					
					if(_dic[id] != null){
					
						var sp:BattleMapUnit = new BattleMapUnit(this,id);
						
						sp.isMine = _dic[id] == 1;
						
						sp.refresh();
						
						if(!Battle.instance.isHost && FLIP_MAP == 2){
							
							var posI:int = mapUnit.mapHeight - 1 - i;
							var posM:int = mapUnit.mapWidth - 1 - m;
							var fix:int = -1;
							
						}else if(!Battle.instance.isHost && FLIP_MAP == 1){
						
							posI = i;
							posM = mapUnit.mapWidth - 1 - m;
							fix = -1;
						
						}else{
							
							posI = i;
							posM = m;
							fix = 1;
						}
							
						sp.x = unitWidth * 1.5 + ((i % 2) == 0 ? 0 : (fix * unitWidth * 0.5 * Math.sqrt(3))) + posM * unitWidth * Math.sqrt(3);
						
						sp.y = unitWidth * 1.5 + posI * unitWidth * 1.5;
						
						mapContainer.addChild(sp);
						
						dic[sp.id] = sp;
					}
				}
			}
			
			mapContainer.flatten();
		}
		
		public function refresh(_dic:Dictionary):void{
			
			mapContainer.unflatten();
			
			for each(var unit:BattleMapUnit in dic){
				
				var type:int = _dic[unit.id];
				
				if(unit.isMine != type == 1){
					
					unit.isMine = type == 1;
					
					unit.refresh();
				}
			}
			
			mapContainer.flatten();
		}

		public function touchBegin(_unit:BattleMapUnit,_globalX:Number,_globalY:Number):void{
			
			var hero:BattleHero = Battle.instance.heroData[_unit.id];
			
			if(hero != null){
				
				tmpPoint.x = _unit.x;
				tmpPoint.y = _unit.y;
				
				mapContainer.localToGlobal(tmpPoint,tmpPoint2);
			
				Battle.instance.showHeroDetail(tmpPoint2.x,tmpPoint2.y,hero.csv.id);
				
				if(hero.power > 0){
					
					showTargetFrame(hero);
				}
				
				setSelectedUnit(_unit);
				
				if(!Battle.instance.isActioned && hero.isMine && Battle.instance.canMoveData != null && Battle.instance.canMoveData.indexOf(hero.pos) != -1){
					
					moveFun = readyToHeroMove;
					
				}else{
					
					moveFun = readyToContainerMove;
				}
				
				return;
			}
			
			var card:BattleCard = Battle.instance.summonDic[_unit.id];
			
			if(card != null){
				
				Battle.instance.cardTouchBegin(card);
				
				return;
			}
			
			clearSelectedUnit();
			
			Battle.instance.hideHeroDetail();
			
			movePoint2.x = _globalX;
			movePoint2.y = _globalY;
			
			movePoint1.x = Battle.instance.gameContainerX;
			movePoint1.y = Battle.instance.gameContainerY;
			
			moveFun = move;
		}
		
		private function readyToContainerMove(_globalX:Number,_globalY:Number):void{
			
			tmpPoint.x = selectedUnit.x;
			tmpPoint.y = selectedUnit.y;
			
			localToGlobal(tmpPoint,tmpPoint2);
			
			tmpPoint.x = _globalX;
			tmpPoint.y = _globalY;
			
			if(Point.distance(tmpPoint,tmpPoint2) > unitWidth * Battle.instance.gameContainerScale){
				
				clearSelectedUnit();
				
				Battle.instance.hideHeroDetail();
				
				movePoint2.x = _globalX;
				movePoint2.y = _globalY;
				
				movePoint1.x = Battle.instance.gameContainerX;
				movePoint1.y = Battle.instance.gameContainerY;
				
				moveFun = move;
			}
		}
		
		private function readyToHeroMove(_globalX:Number,_globalY:Number):void{
			
			tmpPoint.x = selectedUnit.x;
			tmpPoint.y = selectedUnit.y;
			
			localToGlobal(tmpPoint,tmpPoint2);
			
			tmpPoint.x = _globalX;
			tmpPoint.y = _globalY;
			
			if(Point.distance(tmpPoint,tmpPoint2) > unitWidth * Battle.instance.gameContainerScale){
				
				var hero:BattleHero = Battle.instance.heroData[selectedUnit.id];
				
				if(Battle.instance.money > 0 || hero.pos in Battle.instance.moveDic){
					
					moveFun = heroMove;
					
				}else{
					
					Battle.instance.moneyTremble();
					
					moveFun = null;
				}
			}
		}
		
		private function heroMove(_globalX:Number,_globalY:Number):void{
			
			var hero:BattleHero = Battle.instance.heroData[selectedUnit.id];
			
			tmpPoint.x = selectedUnit.x;
			tmpPoint.y = selectedUnit.y;
			
			localToGlobal(tmpPoint,tmpPoint2);
			
			tmpPoint.x = _globalX;
			tmpPoint.y = _globalY;
			
			if(Point.distance(tmpPoint,tmpPoint2) < unitWidth * Battle.instance.gameContainerScale){
				
				Battle.instance.heroMove(hero,-1);
				
				return;
			}
			
			var angle:Number = Math.atan2(_globalY - tmpPoint2.y,_globalX - tmpPoint2.x);
			
			if(angle >= -Math.PI / 6 && angle < Math.PI / 6){
				
				if(!Battle.instance.isHost && FLIP_MAP == 2){
					
					var target:int = hero.pos - 1;
						
				}else if(!Battle.instance.isHost && FLIP_MAP == 1){
					
					target = hero.pos - 1;
					
				}else{
					
					target = hero.pos + 1;
				}
					
			}else if(angle >= Math.PI / 6 && angle < Math.PI * 0.5){
				
				if(!Battle.instance.isHost && FLIP_MAP == 2){
					
					target = hero.pos - mapUnit.mapWidth;
					
				}else if(!Battle.instance.isHost && FLIP_MAP == 1){
					
					target = hero.pos + mapUnit.mapWidth - 1;
					
				}else{
					
					target = hero.pos + mapUnit.mapWidth;
				}
				
			}else if(angle >= Math.PI * 0.5 && angle < Math.PI * 5 / 6){
				
				if(!Battle.instance.isHost && FLIP_MAP == 2){
					
					target = hero.pos - mapUnit.mapWidth + 1;
					
				}else if(!Battle.instance.isHost && FLIP_MAP == 1){
					
					target = hero.pos + mapUnit.mapWidth;
					
				}else{
					
					target = hero.pos + mapUnit.mapWidth - 1;
				}
				
			}else if(angle >= -Math.PI * 0.5 && angle < -Math.PI / 6){
				
				if(!Battle.instance.isHost && FLIP_MAP == 2){
					
					target = hero.pos + mapUnit.mapWidth - 1;
					
				}else if(!Battle.instance.isHost && FLIP_MAP == 1){
					
					target = hero.pos - mapUnit.mapWidth;
					
				}else{
					
					target = hero.pos - mapUnit.mapWidth + 1;
				}
				
			}else if(angle >= -Math.PI * 5 / 6 && angle < -Math.PI * 0.5){
				
				if(!Battle.instance.isHost && FLIP_MAP == 2){
					
					target = hero.pos + mapUnit.mapWidth;
					
				}else if(!Battle.instance.isHost && FLIP_MAP == 1){
					
					target = hero.pos - mapUnit.mapWidth + 1;
					
				}else{
					
					target = hero.pos - mapUnit.mapWidth;
				}
				
			}else{
				
				if(!Battle.instance.isHost && FLIP_MAP == 2){
					
					target = hero.pos + 1;
					
				}else if(!Battle.instance.isHost && FLIP_MAP == 1){
					
					target = hero.pos + 1;
					
				}else{
					
					target = hero.pos - 1;
				}
			}
			
			Battle.instance.heroMove(hero,target);
		}
		
		private function move(_globalX:Number,_globalY:Number):void{
			
			Battle.instance.gameContainerX = movePoint1.x + _globalX - movePoint2.x;
			Battle.instance.gameContainerY = movePoint1.y + _globalY - movePoint2.y;
		}
		
		public function touchEnd(_unit:BattleMapUnit,_globalX:Number,_globalY:Number):void{
			
			if(moveFun == Battle.instance.cardMove1){
				
				Battle.instance.cardTouchEnd(Battle.instance.nowChooseCard,_globalX,_globalY);
				
			}else{
				
				moveFun = null;
			}
		}
		
		private function bgBeTouch(e:TouchEvent):void{
			
			var touch:Touch = e.getTouch(bgContainer);
			
			if(touch != null){
				
				if(touch.phase == TouchPhase.BEGAN){
					
					Battle.instance.clearNowChooseCard();

					var unit:BattleMapUnit = getTouchUnit(touch.globalX,touch.globalY);
					
					if(unit != null){
						
						touchBegin(unit,touch.globalX,touch.globalY);
						
					}else{
						
						clearSelectedUnit();
						
						Battle.instance.hideHeroDetail();
						
						movePoint2.x = touch.globalX;
						movePoint2.y = touch.globalY;
						
						movePoint1.x = Battle.instance.gameContainerX;
						movePoint1.y = Battle.instance.gameContainerY;
						
						moveFun = move;
					}
					
				}else if(touch.phase == TouchPhase.ENDED){
					
					unit = getTouchUnit(touch.globalX,touch.globalY);
					
					touchEnd(unit,touch.globalX,touch.globalY);

				}else if(touch.phase == TouchPhase.MOVED){
					
					if(moveFun != null){
					
						moveFun(touch.globalX,touch.globalY);
					}
				}
			}
		}
		
		public function getTouchUnit(_globalX:int,_globalY:int):BattleMapUnit{
			
			tmpPoint.x = _globalX;
			tmpPoint.y = _globalY;
			
			this.globalToLocal(tmpPoint,tmpPoint2);
			
			var localX:Number = tmpPoint2.x;
			var localY:Number = tmpPoint2.y;
			
			var b:int = Math.floor((localY - unitWidth * 0.5) / unitWidth / 1.5);
			
			if(b == mapUnit.mapHeight){
				
				b = mapUnit.mapHeight - 1;
			}
			
			if(b >= 0 && b < mapUnit.mapHeight){
				
				if(b % 2 == 0){
					
					var a:int = Math.floor((localX - unitWidth * 0.5) / unitWidth / Math.sqrt(3));
					
					if(a == mapUnit.mapWidth){
						
						a = mapUnit.mapWidth - 1;
					}
					
					if(a < 0 || a >= mapUnit.mapWidth){
						
						return null;
					}
					
				}else{
					
					a = Math.floor((localX - unitWidth * 0.5 - unitWidth * 0.5 * Math.sqrt(3)) / unitWidth / Math.sqrt(3));
					
					if(a == -1){
						
						a = 0;
						
					}else if(a == mapUnit.mapWidth - 1){
						
						a = mapUnit.mapWidth - 2;
					}
					
					if(a < 0 || a >= mapUnit.mapWidth - 1){
						
						return null;
					}
				}
				
				var index:int = b * mapUnit.mapWidth - int(b * 0.5) + a;
				
				if(!Battle.instance.isHost){
				
					if(FLIP_MAP == 2){
						
						index = mapUnit.size - 1 - index;
						
					}else if(FLIP_MAP == 1){
						
						index = b * mapUnit.mapWidth - int(b * 0.5) + mapUnit.mapWidth - b % 2 - 1 - a;
					}
				}
				
				point1.x = unitWidth * 1.5 + ((b % 2) == 0 ? 0 : (unitWidth * 0.5 * Math.sqrt(3))) + a * unitWidth * Math.sqrt(3);
				point1.y = unitWidth * 1.5 + b * unitWidth * 1.5;
				
				point2.x = localX;
				point2.y = localY;
				
				var dis:Number = Point.distance(point2,point1);
				
				var resultUnit:BattleMapUnit = dic[index];
				
				var vec:Vector.<int> = mapUnit.neightbourDic[index];
				
				for each(var i:int in vec){
					
					if(i != -1){
					
						var unit:BattleMapUnit = dic[i];
						
						point1.x = unit.x;
						point1.y = unit.y;
						
						if(Point.distance(point2,point1) < dis){
							
							resultUnit = unit;
							
							break;
						}
					}
				}
				
				if(resultUnit != null){
					
					point1.x = resultUnit.x;
					point1.y = resultUnit.y;
					
					dis = Point.distance(point2,point1);
					
					if(dis > unitWidth){
						
						return null;
					}
				}
				
//				trace("resultUnit:",resultUnit.id);
				
				return resultUnit;
				
			}else{
				
				return null;
			}
		}
		
		public function getSelectedUnit():BattleMapUnit{
			
			return selectedUnit;
		}
		
		public function setSelectedUnit(_unit:BattleMapUnit):void{
			
			selectedUnit = _unit;
			
			blackFrame.visible = true;
			
			blackFrame.x = selectedUnit.x;
			blackFrame.y = selectedUnit.y;
		}
		
		public function clearSelectedUnit():void{
			
			if(selectedUnit != null){
				
				blackFrame.visible = false;
				
				selectedUnit = null;
			}
		}
		
		private function showTargetFrame(_hero:BattleHero):void{
			
			if(_hero.csv.heroType.attackType == 1){
				
				loop1:for(var i:int = 0 ; i < 6 ; i++){
					
					var nowPos:int = _hero.pos;
					
					for(var m:int = 0 ; m < _hero.csv.heroType.attackRange ; m++){
						
						var pos:int = mapUnit.neightbourDic[nowPos][i];
						
						if(pos == -1){
							
							continue loop1;
							
						}else{
							
							nowPos = pos;
						}
					}
					
					checkAddFrame(_hero,nowPos);
				}
					
			}else if(_hero.csv.heroType.attackType == 2){
				
				loop2:for(i = 0 ; i < 6 ; i++){
					
					nowPos = _hero.pos;
					
					for(m = 0 ; m < _hero.csv.heroType.attackRange ; m++){
						
						pos = mapUnit.neightbourDic[nowPos][i];
						
						if(pos == -1){
							
							continue loop2;
							
						}else{
							
							var result:Boolean = checkAddFrame(_hero,pos);
							
							if(result){
								
								continue loop2;
								
							}else{
								
								nowPos = pos;
							}
						}
					}
				}
			}
		}
		
		public function hideTargetFrame():void{
			
			for(var i:int = 0 ; i < redFrameIndex ; i++){
				
				selectedFrameContaienr.removeChild(redFrameVec[i]);
			}
			
			redFrameIndex = 0;
			
			for(i = 0 ; i < greenFrameIndex ; i++){
				
				selectedFrameContaienr.removeChild(greenFrameVec[i]);
			}
			
			greenFrameIndex = 0;
		}
		
		private function checkAddFrame(_hero:BattleHero,_pos:int):Boolean{
			
			var hero:BattleHero = Battle.instance.heroData[_pos];
			
			if(hero != null){
				
				if(hero.isMine == _hero.isMine){
					
					var frame:Sprite = greenFrameVec[greenFrameIndex];
					
					greenFrameIndex++;
					
				}else{
					
					frame = redFrameVec[redFrameIndex];
					
					redFrameIndex++;
				}
				
				selectedFrameContaienr.addChild(frame);
				
				var battleMapUnit:BattleMapUnit = dic[_pos];
				
				frame.x = battleMapUnit.x;
				frame.y = battleMapUnit.y;
				
				return true;
				
			}else{
				
				if(_pos in Battle.instance.summonDic){
					
					if(_hero.isMine){
						
						frame = greenFrameVec[greenFrameIndex];
						
						greenFrameIndex++;
						
					}else{
						
						frame = redFrameVec[redFrameIndex];
						
						redFrameIndex++;
					}
					
					selectedFrameContaienr.addChild(frame);
					
					battleMapUnit = dic[_pos];
					
					frame.x = battleMapUnit.x;
					frame.y = battleMapUnit.y;
					
					return true;
					
				}else{
					
					return false;
				}
			}
		}
		
		public function disposeBattleMap():void{
			
			mapContainer.removeChildren();
			
			clearSelectedUnit();
			
			moveFun = null;
		}
	}
}