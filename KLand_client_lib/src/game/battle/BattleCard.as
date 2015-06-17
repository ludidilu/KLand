package game.battle
{
	import csv.Csv;
	
	import data.csv.Csv_hero;
	import data.resource.ResourceFont;
	import data.resource.ResourceHero;
	
	import starling.display.Image;
	import starling.display.Sprite;
	import starling.events.Touch;
	import starling.events.TouchEvent;
	import starling.events.TouchPhase;
	import starling.text.TextField;
	import starling.utils.HAlign;
	import starling.utils.VAlign;

	public class BattleCard extends Sprite
	{
		private static const fontSize:int = 20;
		
		public var heroCsv:Csv_hero;
		
		private var img:Image;
		
		public var uid:int;
		
		private var typeTf:TextField;
		private var starTf:TextField;
		private var hpTf:TextField;
		private var atkTf:TextField;
		
		private var isSummoned:Boolean = false;
		
		public function BattleCard(_csvID:int)
		{
			super();
			
			heroCsv = Csv.getData(Csv_hero.NAME,_csvID) as Csv_hero;
			
			addEventListener(TouchEvent.TOUCH,beTouch);
		
			img = new Image(ResourceHero.getTexture(heroCsv.picName));
			
			img.x = -0.5 * img.width;
			img.y = -0.5 * img.height;
			
			addChild(img);
			
			typeTf = new TextField(img.width,img.height,heroCsv.heroType.name,ResourceFont.fontName,fontSize,0xFFFFFF);
			
			typeTf.hAlign = HAlign.LEFT;
			typeTf.vAlign = VAlign.TOP;
			
			typeTf.x = img.x;
			typeTf.y = img.y;
			
			addChild(typeTf);
			
			starTf = new TextField(img.width,img.height,String(heroCsv.star),ResourceFont.fontName,fontSize,0xFFFFFF);
			
			starTf.hAlign = HAlign.RIGHT;
			starTf.vAlign = VAlign.TOP;
			
			starTf.x = img.x;
			starTf.y = img.y;
			
			addChild(starTf);
			
			hpTf = new TextField(img.width,img.height,String(heroCsv.maxHp),ResourceFont.fontName,fontSize,0xFFFFFF);
			
			hpTf.hAlign = HAlign.RIGHT;
			hpTf.vAlign = VAlign.BOTTOM;
			
			hpTf.x = img.x;
			hpTf.y = img.y;
			
			addChild(hpTf);
			
			atkTf = new TextField(img.width,img.height,String(heroCsv.atk),ResourceFont.fontName,fontSize,0xFFFFFF);
			
			atkTf.hAlign = HAlign.LEFT;
			atkTf.vAlign = VAlign.BOTTOM;
			
			atkTf.x = img.x;
			atkTf.y = img.y;
			
			addChild(atkTf);
				
			flatten();
		}
		
		public function setIsSummoned(_b:Boolean):void{
			
			if(isSummoned != _b){
				
				isSummoned = _b;
				
				unflatten();
				
				starTf.color = isSummoned ? 0xFF00FF : 0xFFFFFF;
				
				flatten();
			}
		}
		
		private function beTouch(e:TouchEvent):void{
			
			var touch:Touch = e.getTouch(this);
			
			if(touch != null){
				
				if(touch.phase == TouchPhase.BEGAN){
				
					Battle.instance.cardTouchBegin(this);
					
				}else if(touch.phase == TouchPhase.MOVED){
					
					Battle.instance.cardTouchMove(this,touch.globalX,touch.globalY);
					
				}else if(touch.phase == TouchPhase.ENDED){
					
					Battle.instance.cardTouchEnd(this,touch.globalX,touch.globalY);
				}
			}
		}
	}
}