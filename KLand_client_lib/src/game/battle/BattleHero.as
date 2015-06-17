package game.battle
{
	import com.greensock.TweenLite;
	import com.greensock.easing.Linear;
	
	import data.csv.Csv_hero;
	import data.resource.ResourceFont;
	import data.resource.ResourceHero;
	
	import starling.display.Image;
	import starling.display.Sprite;
	import starling.text.TextField;
	import starling.utils.HAlign;
	import starling.utils.VAlign;

	public class BattleHero extends Sprite
	{
		private static const fontSize:int = 20;
		
		public var isMine:Boolean;
		public var csv:Csv_hero;
		public var hp:int;
		public var power:int;
		public var pos:int;
		
		public var hpChange:int;
		public var atkFix:int;
		public var maxHpFix:int;
		
		private var frameImg:Image;
		private var img:Image;
		private var hpTf:TextField;
		private var atkTf:TextField;
		private var typeTf:TextField;
		private var powerTf:TextField;
		
		private var selected:Boolean;
		
		public function BattleHero(){
			
			super();
		}
		
		public function refresh(_refreshSkillAndMove:Boolean):void{
			
			var resultHp:int = hp + hpChange;
			
			if(resultHp < 0){
				
				var tmpHp:int = 0;
				
				var hpColor:uint = 0xFFFF0000;
				
			}else if(resultHp < csv.maxHp + maxHpFix){
				
				tmpHp = resultHp;
				
				hpColor = 0xFFFF0000;
				
			}else if(maxHpFix == 0){
				
				tmpHp = csv.maxHp + maxHpFix;
				
				hpColor = 0xFFFFFFFF;
				
			}else{
				
				tmpHp = csv.maxHp + maxHpFix;
				
				hpColor = 0xFF00FF00;
			}
			
			var tmpAtk:int = csv.atk + atkFix;
			
			if(tmpAtk < 0){
				
				tmpAtk = 0;
			}
			
			if(tmpAtk > csv.atk){
				
				var atkColor:uint = 0xFF00FF00;
				
			}else if(tmpAtk < csv.atk){
				
				atkColor = 0xFFFF0000;
				
			}else{
				
				atkColor = 0xFFFFFFFF;
			}
			
			if(_refreshSkillAndMove){
				
				if(power > 0){
				
					var colorType:int = 0;
					
					loop:for(var i:int = 0 ; i < csv.skillCondition.length ; i++){
						
						switch(csv.skillCondition[i]){
							
							case 0:
								
								colorType = 2;
								
								break loop;
							
							case 1:
								
								if(isMine){
									
									if(!Battle.instance.isActioned){
										
										if(Battle.instance.moveDic[pos] != null){
											
											if(Battle.instance.moveResultDic[pos]){
												
												colorType = 2;
												
												break loop;
												
											}else{
												
												colorType = 1;
											}
											
										}else{
											
											colorType = 1;
										}
									}
									
								}else{
									
									if(Battle.instance.isActioned){
										
										colorType = 1;
									}
								}
								
								break;
							
							case 2:
								
								if(isMine){
									
									if(!Battle.instance.isActioned){
										
										if(Battle.instance.moveDic[pos] != null){
											
											if(Battle.instance.moveResultDic[pos]){
												
												colorType = 1;
												
											}else{
												
												colorType = 2;
												
												break loop;
											}
											
										}else{
											
											colorType = 2;
											
											break loop;
										}
										
									}else{
										
										colorType = 2;
										
										break loop;
									}
									
								}else{
									
									if(!Battle.instance.isActioned){
										
										colorType = 2;
										
										break loop;
										
									}else{
										
										colorType = 1;
									}
								}
								
								break;
							
							case 3:
								
								if(power > csv.skillConditionArg[i]){
									
									colorType = 2;
									
									break loop;
								}
								
								break;
							
							case 4:
								
								if(power < csv.skillConditionArg[i]){
									
									colorType = 2;
									
									break loop;
								}
								
								break;
							
							case 5:
								
								if(hp > csv.skillConditionArg[i]){
									
									colorType = 2;
									
									break loop;
								}
								
								break;
							
							case 6:
								
								if(hp < csv.skillConditionArg[i]){
									
									colorType = 2;
									
									break loop;
								}
								
								break;
						}
					}
					
					if(colorType == 0){
						
						var typeColor:uint = 0xFFFFFFFF;
						
					}else if(colorType == 1){
						
						typeColor = 0xFFFFFF00;
						
					}else{
						
						typeColor = 0xFF00FF00;
					}
					
				}else{
					
					typeColor = 0xFFFFFFFF;
				}
				
			}else{
				
				typeColor = 0xFFFFFFFF;
			}
			
//			var typeColor:uint = 0xFFFFFFFF;
			
			if(power == 0){
				
				var powerColor:uint = 0xFFFF0000;
				
			}else if(power < Battle.POWER_CAN_MOVE || csv.heroType.moveType == 0){
				
				powerColor = 0xFFFFFF00;
				
			}else{
				
				powerColor = 0xFFFFFFFF;
			}
			
			
			if(img == null){
				
				frameImg = new Image(ResourceHero.getTexture("redCardFrame"));
				
				frameImg.x = -0.5 * frameImg.width;
				frameImg.y = -0.5 * frameImg.height;
				
				frameImg.visible = selected;
				
				addChild(frameImg);
				
				img = new Image(ResourceHero.getTexture(csv.picName));
				
				img.x = -0.5 * img.width;
				img.y = -0.5 * img.height;
				
				addChild(img);
				
				hpTf = new TextField(img.width,img.height,String(tmpHp),ResourceFont.fontName,fontSize,hpColor);
				
				hpTf.hAlign = HAlign.RIGHT;
				hpTf.vAlign = VAlign.BOTTOM;
				
				hpTf.x = img.x;
				hpTf.y = img.y;
				
				addChild(hpTf);
				
				atkTf = new TextField(img.width,img.height,String(tmpAtk),ResourceFont.fontName,fontSize,atkColor);
				
				atkTf.hAlign = HAlign.LEFT;
				atkTf.vAlign = VAlign.BOTTOM;
				
				atkTf.x = img.x;
				atkTf.y = img.y;
				
				addChild(atkTf);
				
				typeTf = new TextField(img.width,img.height,csv.heroType.name,ResourceFont.fontName,fontSize,typeColor);
				
				typeTf.hAlign = HAlign.LEFT;
				typeTf.vAlign = VAlign.TOP;
				
				typeTf.x = img.x;
				typeTf.y = img.y;
				
				addChild(typeTf);
				
				powerTf = new TextField(img.width,img.height,String(power),ResourceFont.fontName,fontSize,powerColor);
				
				powerTf.hAlign = HAlign.RIGHT;
				powerTf.vAlign = VAlign.TOP;
				
				powerTf.x = img.x;
				powerTf.y = img.y;
				
				addChild(powerTf);
				
			}else{
				
				unflatten()
				
				frameImg.visible = selected;
					
				hpTf.text = String(tmpHp);
				
				hpTf.color = hpColor;
				
				atkTf.text = String(tmpAtk);
				
				atkTf.color = atkColor;
				
				typeTf.color = typeColor;
				
				powerTf.text = String(power);
				
				powerTf.color = powerColor;
			}
			
			flatten();
		}
		
		public function tremble(_callBack:Function = null,...arg):void{
			
			startTremble(x,10,_callBack,arg);
		}
		
		private function startTremble(_opos:Number,_posFix:Number,_callBack:Function,_arg:Array):void{
			
			if(Math.abs(_posFix) < 1){
				
				x = _opos;
				
				if(_callBack != null){
					
					_callBack.apply(null,_arg);
				}
				
				return;
			}
			
			if(_posFix < 0){
				
				var posFix:Number = -_posFix - 0.5;
				
			}else{
				
				posFix = -_posFix + 0.5;
			}
			
			TweenLite.to(this,0.05,{x:_opos + _posFix,ease:Linear.easeNone,onComplete:startTremble,onCompleteParams:[_opos,posFix,_callBack,_arg]});
		}
		
		public function getMaxHp():int{
			
			return csv.maxHp + maxHpFix;
		}
	}
}