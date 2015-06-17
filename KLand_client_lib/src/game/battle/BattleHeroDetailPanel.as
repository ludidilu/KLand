package game.battle
{
	import csv.Csv;
	
	import data.csv.Csv_hero;
	import data.resource.ResourceFont;
	import data.resource.ResourceHero;
	
	import starling.display.Image;
	import starling.display.Quad;
	import starling.display.Sprite;
	import starling.events.Touch;
	import starling.events.TouchEvent;
	import starling.events.TouchPhase;
	import starling.text.TextField;
	import starling.utils.HAlign;
	import starling.utils.VAlign;

	public class BattleHeroDetailPanel extends Sprite
	{
		private var img:Image;
		private var nameTf:TextField;
		private var typeTf:TextField;
		private var starTf:TextField;
		private var hpTf:TextField;
		private var powerTf:TextField;
		private var atkTf:TextField;
//		private var defTf:TextField;
//		private var wisTf:TextField;
		private var commentTf:TextField;
		
		private var bg:Sprite;
		private var ui:Sprite;
		
		public function BattleHeroDetailPanel()
		{
			super();
			
			bg = new Sprite;
			
			bg.addChild(new Quad(300,200,0xFFFFFF00));
			
			bg.flatten();
			
			addChild(bg);
			
			ui = new Sprite;
			
			addChild(ui);
			
			addEventListener(TouchEvent.TOUCH,beTouch);
		}
		
		private function beTouch(e:TouchEvent):void{
			
			var touch:Touch = e.getTouch(this,TouchPhase.ENDED);
				
			if(touch != null){
				
				this.visible = false;
			}
		}
		
		public function setData(_heroID:int):void{
			
			var heroCsv:Csv_hero = Csv.getData(Csv_hero.NAME,_heroID) as Csv_hero;
			
			if(img == null){
				
				img = new Image(ResourceHero.getTexture(heroCsv.picName));
				
				ui.addChild(img);
				
				img.x = 10;
				img.y = 10;
				
				nameTf = new TextField(60,30,heroCsv.name,ResourceFont.fontName,24);
				
				nameTf.hAlign = HAlign.LEFT;
				nameTf.vAlign = VAlign.TOP;
				
				nameTf.x = 80;
				nameTf.y = 10;
				
				ui.addChild(nameTf);
				
				typeTf = new TextField(60,30,heroCsv.heroType.name,ResourceFont.fontName,24);
				
				typeTf.hAlign = HAlign.LEFT;
				typeTf.vAlign = VAlign.TOP;
				
				typeTf.x = 150;
				typeTf.y = 10;
				
				ui.addChild(typeTf);
				
				starTf = new TextField(60,30,"Star:" + heroCsv.star,ResourceFont.fontName,24);
				
				starTf.hAlign = HAlign.LEFT;
				starTf.vAlign = VAlign.TOP;
				
				starTf.x = 220;
				starTf.y = 10;
				
				ui.addChild(starTf);
				
				hpTf = new TextField(100,30,"HP:" + heroCsv.maxHp,ResourceFont.fontName,24);
				
				hpTf.hAlign = HAlign.LEFT;
				hpTf.vAlign = VAlign.TOP;
				
				hpTf.x = 80;
				hpTf.y = 40;
				
				ui.addChild(hpTf);
				
				powerTf = new TextField(100,30,"Morale:" + heroCsv.maxPower,ResourceFont.fontName,24);
				
				powerTf.hAlign = HAlign.LEFT;
				powerTf.vAlign = VAlign.TOP;
				
				powerTf.x = 190;
				powerTf.y = 40;
				
				ui.addChild(powerTf);
				
				atkTf = new TextField(70,30,"Atk:" + heroCsv.atk,ResourceFont.fontName,24);
				
				atkTf.hAlign = HAlign.LEFT;
				atkTf.vAlign = VAlign.TOP;
				
				atkTf.x = 80;
				atkTf.y = 70;
				
				ui.addChild(atkTf);
				
				commentTf = new TextField(280,100,heroCsv.comment,"Verdana",20);
				
				commentTf.hAlign = HAlign.LEFT;
				commentTf.vAlign = VAlign.TOP;
				
				commentTf.x = 10;
				commentTf.y = 100;
				
				ui.addChild(commentTf);
				
			}else{
				
				ui.unflatten();
				
				img.texture = ResourceHero.getTexture(heroCsv.picName);
				
				nameTf.text = heroCsv.name;
				
				typeTf.text = heroCsv.heroType.name;
				
				starTf.text = "Star:" + heroCsv.star;
				
				hpTf.text = "HP:" + heroCsv.maxHp;
				
				powerTf.text = "Morale:" + heroCsv.maxPower;
				
				atkTf.text = "Atk:" + heroCsv.atk;

				commentTf.text = heroCsv.comment;
			}
			
			ui.flatten();
		}
	}
}