import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Job;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.SkillData;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.util.SkillData.Rate;
import org.powerbot.game.api.util.net.GeItem;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.GroundItem;

@Manifest(authors = { "nootz" }, name = "FaladorChickenKiller", description = "Gain some quick levels and money.", version = 1.0 )
public class FaladorChickenKiller extends ActiveScript implements PaintListener, MouseListener {
	
	public static Area FaladorChickens = new Area(new Tile[] { new Tile(3012, 3299, 0), new Tile(3020, 3299, 0), new Tile(3020, 3281, 0), 
			new Tile(3012, 3281, 0) });
	
	public static ArrayList<Integer> Chickens = new ArrayList<Integer>();
	public static int[] chickenIDs = { 2313, 2314, 2315 };
	public static int featherID = 314;
	public static boolean showSkillMenu;
	public static String skillString = "Not Chosen!";
	public static int startLevel;	
	public static int chosenSkill;
	public static int startAtkLevel;
	public static int startStrLevel;
	public static int startDefLevel;
	public static int startRngLevel;
	public static int startMagLevel;
	public static int startConstLevel;
	
	public static GeItem featherGE = GeItem.lookup(314);
	public static int featherPrice = featherGE.getPrice();
	public static int totalProfit;
	public static int stackSize;
	
	public static int killCounter;
	public static int oldKills;
	public static int featherCounter;
	public static int oldFeathers;
	
	public static Timer runTime = new Timer(0);	
	public static long startTime;
	public static boolean paused;
	public static boolean toggleDrawing = true;;
	public static boolean skillSelectOpen;
	
	
	public static String status = "Waiting...";
	
	 final RenderingHints antialiasing = new RenderingHints(
	            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    private final Color color1 = new Color(0, 0, 0, 200);
    private final Color color2 = new Color(0, 0, 0);
    private final Color color3 = new Color(237, 237, 237, 226);
    private final Color color4 = new Color(169, 169, 169, 226);
    private final Color color5 = new Color(237, 237, 237, 160);
    private final Color color6 = new Color(255, 255, 255, 200);
    public static Color color_skill = new Color(0, 0, 0, 255);
    public static Color color_const = new Color(255, 43, 43, 170);
    public final static Color color_mouse = new Color(250, 250, 250, 200);
    public final static Color color_mouse_opa = new Color(222, 222, 222, 150);
    public static Color color_item = new Color(107, 216, 255, 225);
    public static Color color_item2 = new Color(120, 180, 202, 120);
    public static Color color_chicken = new Color(255, 221, 85, 225);
    public static Color color_chicken2 = new Color(238, 238, 124, 120);
    public static Color color_player = new Color(255, 110, 0, 225);
    public static Color color_player2 = new Color(235, 159, 102, 220);
    public static Color color_player3 = new Color(255, 34, 41, 225);
    public static Color color_player4 = new Color(240, 107, 111, 220);


    public final static BasicStroke stroke2 = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Myriad Pro", 1, 12);
    private final Font font2 = new Font("Myriad Pro", 1, 10);
    private final Font font3 = new Font("Myriad Pro", 0, 10);
	    
	@Override
	public void onRepaint(Graphics g1) {
	       Graphics2D g = (Graphics2D)g1;
	        g.setRenderingHints(antialiasing);
	        GroundItem item = GroundItems.getNearest(LootFilter);
	        NPC n = NPCs.getNearest(MobFilter);
	        if (toggleDrawing) {
		        if (item != null) {
		        	drawTile(g1, item.getLocation(), color_item, 225);
		        	fillTile(g1, item.getLocation(), color_item2, 220);
		        	g1.setFont(font2);	  
		        	g1.setColor(Color.BLACK);
		        	g1.drawString(item.getGroundItem().getName() + " x" + item.getGroundItem().getStackSize(),
		        			(int) item.getCentralPoint().getX()+26, (int) (item.getCentralPoint().getY()+1));
		        	g1.setColor(color_item);
		        	g1.drawString(item.getGroundItem().getName() + " x" + item.getGroundItem().getStackSize(),
		        			(int) item.getCentralPoint().getX()+25, (int) (item.getCentralPoint().getY()));
		        }	        
		        if (n != null) {
		        	drawTile(g1, n.getLocation(), color_chicken, 225);
		        	fillTile(g1, n.getLocation(), color_chicken2, 220);
	        		g1.setFont(font2); 	        		
		        	g1.setColor(Color.BLACK);
		        	g1.drawString(n.getName(), (int) n.getCentralPoint().getX()+26, (int) (n.getCentralPoint().getY()+1));
		        	g1.setColor(color_chicken);
		        	g1.drawString(n.getName(), (int) n.getCentralPoint().getX()+25, (int) (n.getCentralPoint().getY()));
		        }	 
	        }

	        
    	  	final Point p = Mouse.getLocation(); 
    	    g.setColor(color_mouse_opa);
    	    g.drawLine(0, p.y, Game.getDimensions().width, p.y);
    	    g.drawLine(p.x, 0, p.x, Game.getDimensions().height);
    	    g.setColor(Mouse.isPressed() ? Color.RED : color_mouse);    	    
    	    if (!Mouse.isPressed()) {    	    
    	    g.setStroke(stroke2);
    	    g.drawLine(Mouse.getX(), Mouse.getY() + 8, Mouse.getX(), Mouse.getY() - 8);
    	    g.drawLine(Mouse.getX() + 8, Mouse.getY(), Mouse.getX() - 8, Mouse.getY());
    	    } else {
    	    	g.fillRect(Mouse.getX() - 6, Mouse.getY() + 6, 3, 3);
    	    	g.fillRect(Mouse.getX() + 6, Mouse.getY() + 6, 3, 3);
    	    	g.fillRect(Mouse.getX() - 6, Mouse.getY() - 6, 3, 3);
    	    	g.fillRect(Mouse.getX() + 6, Mouse.getY() - 6, 3, 3);
    	    }

	        g.setColor(color1);
	        g.fillRect(1, 355, 517, 32);
	        g.setColor(color2);
	        g.setStroke(stroke1);
	        g.drawRect(1, 355, 517, 32);
	        g.setColor(color1);
	        g.fillRect(6, 340, 61, 15);
	        g.setColor(color2);
	        g.drawRect(6, 340, 61, 15);
	        g.setColor(color1);
	        g.fillRect(73, 340, 69, 15);
	        g.setColor(color2);
	        g.drawRect(73, 340, 69, 15);
	        g.setColor(color1);
	        g.fillRect(443, 340, 69, 15);
	        g.setColor(color2);
	        g.drawRect(443, 340, 69, 15);
	        g.setColor(color1);
	        //g.fillRect(317, 355, 201, 16);
	        g.setColor(color_skill);
	       // g.fillRect(318, 356, (int) getExpBarLength(chosenSkill, 200), 15);
	        g.fillRect(318, 356, 100, 15);
	        g.setColor(color2);
	        g.drawRect(317, 355, 201, 16);
	        g.setColor(color1);
	       // g.fillRect(317, 371, 201, 16);
	        g.setColor(color_const);
	        //g.fillRect(318, 372, (int) getExpBarLength(3, 200), 15);
	        g.fillRect(318, 372, 200, 15);
	        g.setColor(color2);
	        g.drawRect(317, 371, 201, 16);
	        g.setFont(font1);
	        g.drawString("FaladorChickenKiller", 9, 370);
	        g.setColor(color3);
	        g.drawString("FaladorChickenKiller", 8, 369);
	        g.setFont(font2);
	        g.setColor(color4);
	        g.drawString("Pause", 22, 351);
	        g.drawString("Toggle Draw", 79, 351);
	        g.drawString("Skill to Track", 449, 351);
	        if (skillSelectOpen) {
		        g.setColor(color1);
		        g.fillRect(425, 320, 87, 14);
		        g.setColor(color2);
		        g.drawRect(425, 320, 87, 14);
		        g.setColor(color1);
		        g.fillRect(425, 300, 87, 14);
		        g.setColor(color2);
		        g.drawRect(425, 300, 87, 14);
		        g.setColor(color1);
		        g.fillRect(425, 280, 87, 14);
		        g.setColor(color2);
		        g.drawRect(425, 280, 87, 14);
		        g.setColor(color1);
		        g.fillRect(425, 260, 87, 14);
		        g.setColor(color2);
		        g.drawRect(425, 260, 87, 14);
		        g.setColor(color1);
		        g.fillRect(425, 240, 87, 14);
		        g.setColor(color2);
		        g.drawRect(425, 240, 87, 14);
		        g.setFont(font2);
		        g.setColor(color4);
		        g.drawString("Attack", 455, 331);
		        g.drawString("Strength", 450, 311);
		        g.drawString("Defence", 451, 291);
		        g.drawString("Magic", 454, 271);
		        g.drawString("Range", 455, 251);
	        }
	        g.setFont(font3);
	        g.setColor(color5);
	        g.drawString("Run Time: " + runTime.toElapsedString(), 8, 382);
	        g.drawString("Killed: " + killCounter + " ( " + getPerHour(killCounter) + " p/h )", 115, 382);
	        g.drawString("Profit: " + kFormat(totalProfit) + " ( " + kFormat(getPerHour(totalProfit)) + " p/h )", 210, 382);
	        g.drawString("Status: " + status, 135, 369);
	        g.setColor(color6);
	        if (skillString == "Not Chosen!") {
		        g.drawString("Choose a skill by clicking \"Skill to Track\".", 322, 367);
	        } else {
		        g.drawString("[ " + skillString + ": " + Skills.getRealLevel(chosenSkill) + " (+" + (Skills.getRealLevel(chosenSkill) - startLevel) + ") | " + kFormat(getXpHr(chosenSkill)) + " xp/h | TTL: " + getTTNL(chosenSkill) + " ]", 322, 367);
	        }
	        g.drawString("[ Constitution: " + Skills.getRealLevel(3) + " (+" + (Skills.getRealLevel(3) - startConstLevel) + ") | " + kFormat(getXpHr(3)) + " | TTL: " + getTTNL(3) + " ]", 322, 383);

    }

    private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
    private Tree jobContainer = null;

    public synchronized final void provide(final Node... jobs) {
            for (final Node job : jobs) {
                    if(!jobsCollection.contains(job)) {
                            jobsCollection.add(job);
                    }
            }
            jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

    public synchronized final void revoke(final Node... jobs) {
            for (final Node job : jobs) {
                    if(jobsCollection.contains(job)) {
                            jobsCollection.remove(job);
                    }
            }
            jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
    }

    public final void submit(final Job... jobs) {
            for (final Job job : jobs) {
                    getContainer().submit(job);
            }
    }

	@Override
	public int loop() {
		
		if (paused) {			
			Task.sleep(100);
		} else if (jobContainer != null) {
		            final Node job = jobContainer.state();
		            if (job != null) {
		                    jobContainer.set(job);
		                    getContainer().submit(job);
		                    job.join();
		            }
		}
		
		if (Players.getLocal().getInteracting() != null && Players.getLocal().getInteracting().getHpPercent() <= 0) {
			if (killCounter - oldKills < 1) {
			killCounter+=1;
			}
		}		
		if (featherCounter < (oldFeathers+stackSize)) {
			featherCounter = featherCounter + stackSize;
			totalProfit = featherCounter*featherPrice;
		}
		
		
		return 50;
	}
	
    @Override
    public void onStart() {
    		System.out.println("Starting FaladorChickenKiller.");
			provide(new Loot(), new Combat()); 
			paused = false;
			startAtkLevel = Skills.getRealLevel(Skills.ATTACK);
			startStrLevel = Skills.getRealLevel(Skills.STRENGTH);
			startDefLevel = Skills.getRealLevel(Skills.DEFENSE);
			startRngLevel = Skills.getRealLevel(Skills.RANGE);
			startMagLevel = Skills.getRealLevel(Skills.MAGIC);
			startConstLevel = Skills.getRealLevel(Skills.CONSTITUTION);
			startTime = System.currentTimeMillis();
			oldKills = killCounter;	
			oldFeathers = featherCounter;
			for (int i : chickenIDs) {
				Chickens.add(i);
			}
    }
    
    @Override
    public void onStop() {
            System.out.println("Stopping FaladorChickenKiller.");
    }
	
	public static Filter<NPC> MobFilter = new Filter<NPC>(){		
		@Override
		public boolean accept(NPC n) {
			for (int i : Chickens) {
				return n != null && Chickens.contains(n.getId()) && n.getLocation().canReach() 
						&& FaladorChickens.contains(n.getLocation()) && n.getHpPercent() > 0 &&	!n.isInCombat();	
			}
			return false;
		}
	};
	
	public static Filter<GroundItem> LootFilter = new Filter<GroundItem>(){
		@Override
		public boolean accept(GroundItem n) {			
				return n != null && n.getLocation().canReach() && FaladorChickens.contains(n.getLocation()) && n.getId() == featherID;
		}		
	};	
	
	public class Combat extends Node {
		
		@Override
		public boolean activate() {
			return Players.getLocal() != null && Players.getLocal().getInteracting() == null;
		}

		@Override
		public void execute() {
			NPC n = NPCs.getNearest(MobFilter);
			Player p = Players.getLocal();
			oldKills = killCounter;	
			
			if (n == null) {
				status = "Waiting for chicken...";
				Task.sleep(50);				
			} else {			
				if (n.isOnScreen()) {
					if (p.getInteracting() == null && !p.isMoving()) {
						n.interact("Attack");
						status = "Fighting chicken!";
						System.out.println("Attacking chicken.");
						Task.sleep(Random.nextInt(400, 600));
					}					
				} else if (!n.isOnScreen()) {
					Camera.setPitch(Random.nextInt(1, 25));
					Camera.turnTo(n);				
					status = "Finding chicken.";
					System.out.println("Finding chicken.");
				}
			}
			
		}		
	}
	
	public class Loot extends Node {

		@Override
		public boolean activate() {	
			GroundItem item = GroundItems.getNearest(LootFilter);
			return item != null && Players.getLocal() != null && Players.getLocal().getInteracting() == null &&
					!Players.getLocal().isMoving();
		}

		@Override
		public void execute() {
			int currentMouseX = Mouse.getX();
	        int currentMouseY = Mouse.getY();
	        int currentPitch = Camera.getPitch();
	        int currentAngle = Camera.getYaw();
			GroundItem item = GroundItems.getNearest(LootFilter);
			oldFeathers = featherCounter;
			stackSize = item.getGroundItem().getStackSize();
			
			if (item != null) {
				if (!item.isOnScreen()) {
					System.out.println("Turning to feathers.");
					Camera.setPitch(Random.nextInt(10, 25));
					Camera.turnTo(item);
				} else {		
					System.out.println("Picking up " + item.getGroundItem().getName() + ".");
					status = "Picking up feathers.";
						if(item.click(false) && Menu.isOpen() && Menu.contains("Take")) {						
							Menu.select("Take", item.getGroundItem().getName());
							//featherCounter = featherCounter + item.getGroundItem().getStackSize();							
						}
					Mouse.move(currentMouseX + Random.nextInt(-20, 20), currentMouseY + Random.nextInt(-20, 20));
					Camera.setPitch(currentPitch + Random.nextInt(20, 70));
			        Camera.setAngle(currentAngle + Random.nextInt(-200, 200));
				}		
			}
			
		}
		
	}
	
	public static NumberFormat k = new DecimalFormat("###,###,###");
	public static NumberFormat z = new DecimalFormat("#");
	
	public static String kFormat(final int num) {
		return num / 1000 + "." + (num % 1000) / 100 + "K";
	}
	
	public static int getXpHr(int skill) {
		SkillData sr = new SkillData(runTime);
		return sr.experience(Rate.HOUR, skill);		
	}
	
	public static long getTTNL(int skill) {
		SkillData sr = new SkillData(runTime);
		return sr.timeToLevel(Rate.HOUR, skill);
	}
	
	public static double getExpBarLength(int skill, int maxlength) {
        int xpCurrent = Skills.getExperienceRequired(Skills.getRealLevel(skill));
        int xpNext = Skills.getExperienceRequired(Skills.getRealLevel(skill) + 1);        
        double xpBarLength = (xpCurrent / xpNext)*maxlength;
		return xpBarLength;   
	}
	
	public static int getPerHour(int integer) {
		int perHour = (int) (integer * 3600000D / (System.currentTimeMillis() - startTime));
		return perHour;		
	}
	
    public static void drawTile(final Graphics g1, final Tile tile, final Color color, final int alpha) {    	
        if (tile != null) {
                        g1.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                        for (Polygon p1 : tile.getBounds()) {
                                        g1.drawPolygon(p1);
                        }
        }
    }
    
    public static void fillTile(final Graphics g1, final Tile tile, final Color color, final int alpha) {    	
        if (tile != null) {
                        g1.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                        for (Polygon p1 : tile.getBounds()) {
                                        g1.fillPolygon(p1);
                        }
        }
    }

	@Override
	public void mouseClicked(MouseEvent p) {
		final Rectangle pauseBot = new Rectangle(6, 340, 61, 15);
		final Rectangle toggleDraw = new Rectangle(73, 340, 69, 15);		
		final Rectangle skillSelect = new Rectangle(443, 340, 69, 15);
		final Rectangle skillAtk = new Rectangle(425, 320, 87, 14);
		final Rectangle skillStr = new Rectangle(425, 300, 87, 14);
		final Rectangle skillDef = new Rectangle(425, 280, 87, 14);
		final Rectangle skillMag = new Rectangle(425, 260, 87, 14);
		final Rectangle skillRng = new Rectangle(425, 240, 87, 14);
		
		if(pauseBot.contains(p.getPoint())) {
			paused = !paused;
			status = paused ? "Paused." : "Waiting...";
        }		
		if(toggleDraw.contains(p.getPoint())) {
			toggleDrawing = !toggleDrawing;
        }
		if (skillSelect.contains(p.getPoint())) {
			skillSelectOpen = !skillSelectOpen;
			
		}
		if (skillAtk.contains(p.getPoint()) && skillSelectOpen) {
			skillString = "Attack";
			chosenSkill = 0;
			color_skill = new Color(192, 46, 46, 170);
			startLevel = startAtkLevel;
		}
		if (skillStr.contains(p.getPoint()) && skillSelectOpen) {
			skillString = "Strength";
			chosenSkill = 2;
			color_skill = new Color(46, 134, 55, 170);
			startLevel = startStrLevel;
		}
		if (skillDef.contains(p.getPoint()) && skillSelectOpen) {
			skillString = "Defence";
			chosenSkill = 1;
			color_skill = new Color(89, 144, 255, 170);
			startLevel = startDefLevel;
		}
		if (skillMag.contains(p.getPoint()) && skillSelectOpen) {
			skillString = "Magic";
			chosenSkill = 6;
			color_skill = new Color(35, 158, 210, 170);
			startLevel = startMagLevel;
		}
		if (skillRng.contains(p.getPoint()) && skillSelectOpen) {
			skillString = "Range";
			chosenSkill = 4;
			color_skill = new Color(168, 108, 39, 170);
			startLevel = startRngLevel;
		}
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

