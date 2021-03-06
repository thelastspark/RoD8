import java.util.HashSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * The Class GameScreen.
 */
public class GameScreen implements Screen{

	/** The world. */
	public World world;

	/** The contact listener. */
	
	public MyContactListener contactListener;

	/** The phase. */
	public int phase;

	/** The player. */
	public static Player player;

	/** The monsters. */
	public static Array<Monster> monsterList = new Array<Monster>();

	/** The item list. */
	public static Array<Item> itemList = new Array<Item>();

	/** The floating item list. */
	public static Array<Item> floatingItemList = new Array<Item>();

	/** The textures. */
	public static Content textures;

	/** The remove mobs. */
	public static Array<Monster> removeMobs = new Array<Monster>();
	
	/** The remove mines. */
	public static Array<Mine> removeMines = new Array<Mine>();

	/** The transition items. */
	public static Array<Item> transitionItems = new Array<Item>();

	/** The chests. */
	public static HashSet<Chest> chests;
	
	/** The mines. */
	public static Array<Mine> mines = new Array<Mine>();

	
	/** The launchers. */
	public static HashSet<Launcher> launchers;

	/** The teleporter. */
	public static Teleporter teleporter;

	/** The Constant PPM. */
	public static final float PPM = 100;//Conversion of 100 pixels = 1 metre

	/** Associated Indexes: 0 : null; 1 : crab; 2 : lemurian; 3 : giant. */
	public static final float[] MONSTER_WIDTH = {0f, 25f, 18f, 30f, 24f, 82f};

	/** Associated Indexes: 0 : null; 1 : crab; 2 : lemurian; 3 : giant. */
	public static final float[] MONSTER_HEIGHT = {0f, 25f, 18f, 60f, 30f, 120f};

	/** The Constant SCALE. */
	public static final float SCALE = 0.7f;

	/** The state time. */
	float stateTime;

	//private OrthographicCamera hudCam;
	
	/** The score font. */
	BitmapFont scoreFont;

	/** The health texture. */
	Texture healthTexture;

	/** The sprite batch. */
	SpriteBatch spriteBatch;

	/** The debug. */
	private boolean debug = false;
		
	/** The portal start. */
	private long portalStart;

	/** The b 2 dr. */
	private Box2DDebugRenderer b2dr;
	
	/** The b 2 d cam. */
	private OrthographicCamera b2dCam;
	
	/** The tile map. */
	private TiledMap tileMap;
	
	/** The tile size. */
	private float tileSize;
	
	/** The tiled map renderer. */
	private OrthogonalTiledMapRenderer tmr;
	
	/** The crystals. */
	private Array<Crystal> crystals;

	/** The cam. */
	private OrthographicCamera cam;

	/** The monster num. */
	private int monsterNum;
	
	/** The item num. */
	private int itemNum;

	/** The difficulty. */
	private short difficulty;

	/** The spawn timer. */
	private long spawnTimer;
	
	/** The item timer. */
	private long itemTimer;

	/** The blank. */
	private Texture blank;
	
	/** The char type. */
	private int charType;

	/** The Constant BIT_PLAYER. */
	//Filter Bits
	private static final short BIT_PLAYER = 2;
	
	/** The Constant BIT_RED. */
	private static final short BIT_GROUND = 4;
	
	/** The Constant BIT_GREEN. */
	private static final short BIT_AIR = 8;
	
	/** The Constant BIT_BLUE. */
	private static final short BIT_LADDER = 16;
	
	/** The Constant BIT_CRYSTAL. */
	private static final short BIT_CHEST = 32;
	
	/** The Constant BIT_MONSTER. */
	public static final short BIT_MONSTER = 64;
	
	/** The Constant BIT_BULLET. */
	private static final short BIT_BULLET = 128;
	
	/** The Constant BIT_ATTACK. */
	private static final short BIT_ATTACK = 256;
	
	/** The Constant BIT_PORTAL. */
	private static final short BIT_PORTAL = 512;

	/** The Constant BIT_ITEM. */
	private static final short BIT_ITEM = 1024;
	
	/** The Constant BIT_LAUNCHER. */
	private static final short BIT_LAUNCHER = 2048;
	
	/** The Constant BIT_MORTAR. */
	private static final short BIT_MINE = 4096;

	/** The Constant BIT_EXPLOSION. */
	private static final short BIT_EXPLOSION = 8192;
	
	/** The Constant BIT_LAVA. */
	private static final short BIT_LAVA = 16384;
	
	/** The game. */
	private SpaceGame game;
	
	/**
	 * Instantiates a new game screen.
	 *
	 * @param game the game
	 * @param charType the char type
	 */
	public GameScreen(SpaceGame game, int charType){
		
		this.game = game;
		
		this.charType = charType;

		portalStart = 0;
		
		scoreFont = new BitmapFont();
		scoreFont.getData().setScale(0.5f);
		
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
		
		difficulty = 1;
		spawnTimer = 0;

		int width = 320;
		int height = 240;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, width, height);
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, width / PPM, height / PPM);
		
		spriteBatch = new SpriteBatch();
		
		world = new World(new Vector2(0, -9.81f), true);
		contactListener = new MyContactListener(this);
		world.setContactListener(contactListener);	
		b2dr = new Box2DDebugRenderer();
		
		//Load textures (temp)
		textures = new Content();
		textures.loadTexture("commando_final.png", "commando");
		textures.loadTexture("sniper_final.png", "sniper");
		textures.loadTexture("crystal.png", "crystal");
		textures.loadTexture("Monster Crab.png", "crab");
		textures.loadTexture("Monster 2 Final.png", "lemurian");
		textures.loadTexture("monster4.png", "giant");
		textures.loadTexture("monster5.png", "golem");
		textures.loadTexture("finalboss.png", "castle");
		textures.loadTexture("whitepixel.png", "blank");
		textures.loadTexture("chestandteleporter.png", "portal");
		textures.loadTexture("Items.png", "items");
		textures.loadTexture("launcher.png", "launcher");
		textures.loadTexture("mine.png", "mine");
		blank = textures.getTexture("blank");

		monsterNum = 0;
		monsterList.ordered = false;
		
		chests = new HashSet<Chest>();
		launchers = new HashSet<Launcher>();
		
		//Create player, tiles and crystals
		createTiles();
		createCrystals();
		createChests();
		createPortal();
		createLaunchers();
		createPlayer();
		
		phase = 0;

	}
	
	/**
	 * Creates the bullet.
	 *
	 * @param identifier the identifier
	 * @param value the value
	 */
	public void createBullet(String identifier, boolean value){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//Create Player
		//x set at X 100
		//y set at X 100
		bdef.position.set((player.getBody().getPosition().x * 100) / PPM, (player.getBody().getPosition().y * 100) / PPM);
		bdef.type = BodyType.DynamicBody;
		
		if(value){
			
			bdef.linearVelocity.set(5f, 0);
		}
		else{
			
			bdef.linearVelocity.set(-5f, 0);
		}
		bdef.bullet = true;
		Body body = world.createBody(bdef);
		body.setGravityScale(0);
		shape.setAsBox(1 / PPM, 1 / PPM);

		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = BIT_BULLET;
		fdef.filter.maskBits = BIT_GROUND | BIT_MONSTER;
		
		body.createFixture(fdef).setUserData(identifier);
	}
	
	/**
	 * Creates the mortar.
	 *
	 * @param damage the damage
	 * @param dir the dir
	 */
	public void createMine(float damage, boolean dir){
				
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.position.set((player.getBody().getPosition().x * 100) / PPM, (player.getBody().getPosition().y * 100) / PPM);
		bdef.type = BodyType.DynamicBody;
		
		if(dir){
			
			bdef.linearVelocity.x = 1.5f;
		}else{
			
			bdef.linearVelocity.x = -1.5f;
		}
		
		bdef.linearVelocity.y = 3f;
		
		Body body = world.createBody(bdef);
		shape.setAsBox(3 / PPM, 3 / PPM);
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = BIT_MINE;
		fdef.filter.maskBits = BIT_GROUND;
		
		Mine m = new Mine(body);
		mines.add(m);
		
		body.createFixture(fdef).setUserData("mine:" + damage);
		m.getBody().setUserData(m);
	}
	
	/**
	 * Creates the local attack.
	 *
	 * @param m the m
	 * @param damage the damage
	 * @param dir the dir
	 * @param centered the centered
	 */
	public void createLocalAttack(Monster m, float damage, boolean dir, boolean centered){
		
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		if(centered){
			shape.setAsBox((2 * m.width / 3) * SCALE / PPM, (2 * m.height / 3) * SCALE / PPM);
		}else{
			if(dir){
				
				shape.setAsBox((m.width / 2) * SCALE / PPM, m.height * SCALE / PPM, new Vector2((m.width / 4) * SCALE / PPM, 0), 0);
			}
			else{
		
				shape.setAsBox((m.width / 2) * SCALE / PPM, m.height * SCALE / PPM, new Vector2(-(m.width / 4) * SCALE / PPM, 0), 0);
			}
		}
		
		Body body = m.getBody();
		
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_ATTACK;
		fdef.filter.maskBits = BIT_PLAYER;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("attack:" + damage);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		
		if(player.health <= 0){
			
			teleporter = null;
			
			crystals.clear();
			chests.clear();
			transitionItems.clear();
			floatingItemList.clear();
			itemList.clear();
			removeMobs.clear();
			monsterList.clear();		
			chests.clear();
			launchers.clear();
			mines.clear();
			removeMines.clear();

			monsterNum = 0;
			itemNum = 0;
			monsterList.ordered = false;
			
			world.dispose();
			
			world = new World(new Vector2(0, -9.81f), true);
			contactListener = new MyContactListener(this);
			world.setContactListener(contactListener);	
			game.setScreen(new GameOverScreen(game));
		}
		//else if(changing){
			//changeLevel();
			//changing = false;
	//	}
		else{
			//stateTime += Gdx.graphics.getDeltaTime();
			stateTime += delta;
			
			if(player.getState() > 3){
				
				player.increaseAnimTime(delta);	
			}
			
			world.step(delta, 6, 2);
			
			for(Monster m : monsterList){
				
				if(m.health <= 0){
					
					if(!(m.isInLava > 0)){
						
						GameScreen.player.maxHealth += GameScreen.player.HealthSteal;
						GameScreen.player.health += GameScreen.player.HealthSteal;
						player.money += player.goldLeech;
					}
					
					if(m.isMarked){
						
						player.markedMob = null;
					}
					
					removeMobs.add(m);
					world.destroyBody(m.getBody());
				}
			}
			
			HashSet<Body> bodies = contactListener.getBodyToRemove();
			
			for (Body body : bodies){
			
					world.destroyBody(body);
			}
			bodies.clear();
		
			
			for(Monster j : removeMobs){
				
				monsterList.removeValue(j, true);
			}
			removeMobs.clear();
			
			for(Mine m : removeMines){
				
				mines.removeValue(m, true);
			}
			removeMines.clear();
			
			LOOP : for(Item i : transitionItems){
	
				floatingItemList.removeValue(i, true);
				
				for(Item j : itemList){
					if(j.type == i.type){
						j.itemCount += 1;
						break LOOP;
					}
				}
				
				itemNum++;
				itemList.add(i);
				i.itemNum = itemNum;
				
				itemTimer = System.currentTimeMillis() + 6000;
			}
			transitionItems.clear();
			
			Gdx.gl.glClearColor(255, 255, 255, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			//movement update
			player.updateMovement();
			
			player.money += player.goldGain;
			
			cam.position.set(player.getPosition().x * PPM, player.getPosition().y * PPM, 0);
			cam.update();
			spriteBatch.setProjectionMatrix(cam.combined);
		
			tmr.setView(cam);
			tmr.render();
			
			for(Launcher launcher : launchers){
				
				launcher.drawLauncher(spriteBatch, stateTime);
			}
			//True = green portal, false = red portal
			teleporter.drawPortal(spriteBatch, !teleporter.wasActivated || teleporter.isFinished);
			
			spriteBatch.begin();
	
			for(Item i : floatingItemList){
				
				i.drawItem(spriteBatch);
			}
			
			for(Mine m : mines){
				
				m.drawMine(spriteBatch, stateTime);
			}
			
			for(Monster m : monsterList){
				
				if(m.getState() > 3){
					m.increaseAnimTime(delta);
				}
				m.monsterMovement();
	
				if(m.isMarked){
					
					spriteBatch.setColor(Color.SALMON);
					m.drawMonsters(spriteBatch, stateTime);
					spriteBatch.setColor(Color.WHITE);
				}
				else{
					
					m.drawMonsters(spriteBatch, stateTime);
				}
				
				spriteBatch.setColor(Color.GREEN);
				spriteBatch.draw(blank, m.getBody().getPosition().x * PPM - ((float) (0.12 * m.health)), m.getBody().getPosition().y * PPM + 20, (float) (0.24 * m.health), 3);
				spriteBatch.setColor(Color.WHITE);
			}
			
			for(Chest chest : chests){
					
				chest.drawChest(spriteBatch);
			}
			
			//Draw player
			player.drawPlayer(spriteBatch, stateTime);
			
		
			if (teleporter.wasActivated){
				
				int tempTime = (int)((60*1000) - (System.currentTimeMillis() - portalStart))/1000;
				GlyphLayout guiLayout = new GlyphLayout(scoreFont, "Time Remaining: " + tempTime);
			
				if(tempTime > 0){
					
					scoreFont.draw(spriteBatch, guiLayout, teleporter.getBody().getPosition().x * PPM - 30, teleporter.getBody().getPosition().y * PPM + 33);
				}
				else if(tempTime <= 0 && monsterList.size == 0){
					
						teleporter.isActive = false;
						teleporter.isFinished = true;
						
						guiLayout = new GlyphLayout(scoreFont, "Press E to go to the next level...");
						scoreFont.draw(spriteBatch, guiLayout, teleporter.getBody().getPosition().x * PPM - 40, teleporter.getBody().getPosition().y * PPM + 33);
				}
				else{
					
					teleporter.isActive = false;
					guiLayout = new GlyphLayout(scoreFont, monsterList.size + " monsters left.");
					scoreFont.draw(spriteBatch, guiLayout, teleporter.getBody().getPosition().x * PPM - 30, teleporter.getBody().getPosition().y * PPM + 33);
				}
			}
			else{
				
				GlyphLayout guiLayout = new GlyphLayout(scoreFont, "Press E to begin...");
				scoreFont.draw(spriteBatch, guiLayout, teleporter.getBody().getPosition().x * PPM - 30, teleporter.getBody().getPosition().y * PPM + 33);
			}
	
			spriteBatch.end();		
	
			if (Gdx.input.isKeyJustPressed(Keys.L)){
				
				createMonster(false);
			}
			
			if (Gdx.input.isKeyJustPressed(Keys.E)){
				
				if(player.money >= 100){
					
					for(Chest chest : chests){
						
						if (chest.isTouched && !chest.isOpen){
							
							chest.isOpen = true;
							player.money -= 100;
							createItem(chest);
						}
					}
				}
				
				if(teleporter.isTouched && monsterList.size == 0){
					
					teleporter.isActive = true;
					teleporter.wasActivated = true;
					portalStart = System.currentTimeMillis();
					if(difficulty == 3){
						createMonster(true);
					}
					phase = 10;
					
					if(difficulty < 4 && teleporter.isFinished){
						
						changeLevel();
					}
				}
			}
			
			if(difficulty < 4){
				if (Gdx.input.isKeyJustPressed(Keys.M)){
					
					for(Monster m : monsterList){
						m.getBody().setTransform(player.getPosition(), 0);
					}
				}
			
				Matrix4 uiMatrix = cam.combined.cpy();
				uiMatrix.setToOrtho2D(0, 0, 500, 500);
				spriteBatch.setProjectionMatrix(uiMatrix);
				spriteBatch.begin();
				
				GlyphLayout guiLayout = new GlyphLayout(scoreFont, "Gold: " + (int) player.money);
				scoreFont.draw(spriteBatch, guiLayout, 5, 490);
				
				spriteBatch.setColor(Color.BLACK);
				spriteBatch.draw(blank, 100, 50, 300, 10);
				
				spriteBatch.setColor(Color.GREEN);
				spriteBatch.draw(blank, 100, 50, 3 * (100 * (player.health / player.maxHealth)), 10);
				spriteBatch.setColor(Color.WHITE);
				
				for(Item i : itemList){
					i.writeItem(spriteBatch);
				}
				
				if(System.currentTimeMillis() < itemTimer){
					itemList.peek().writeDesc(spriteBatch);
				}
				
				int t = (int) ((System.currentTimeMillis() - player.secondUsed)/1000);
				if(t < player.secondCD/1000){
					
					guiLayout = new GlyphLayout(scoreFont, "Ability S ready in..." + ((player.secondCD/1000) - t));
					scoreFont.draw(spriteBatch, guiLayout, 5, 470);
				}
				else{
					
					guiLayout = new GlyphLayout(scoreFont, "Ability S: Ready!");
					scoreFont.draw(spriteBatch, guiLayout, 5, 470);
		
				}
				
				
				t = (int) ((System.currentTimeMillis() - player.thirdUsed)/1000);
				if(t < player.thirdCD/1000){
					
					guiLayout = new GlyphLayout(scoreFont, "Ability D ready in..." + ((player.thirdCD/1000) - t));
					scoreFont.draw(spriteBatch, guiLayout, 5, 450);
				}
				else{
					
					guiLayout = new GlyphLayout(scoreFont, "Ability D: Ready!");
					scoreFont.draw(spriteBatch, guiLayout, 5, 450);
		
				}
				
				
				t = (int) ((System.currentTimeMillis() - player.fourthUsed)/1000);
				if(t < player.fourthCD/1000){
					
					guiLayout = new GlyphLayout(scoreFont, "Ability F ready in..." + ((player.fourthCD/1000) - t));
					scoreFont.draw(spriteBatch, guiLayout, 5, 430);
				}
				else{
					
					guiLayout = new GlyphLayout(scoreFont, "Ability F: Ready!");
					scoreFont.draw(spriteBatch, guiLayout, 5, 430);
		
				}
				
				guiLayout = new GlyphLayout(scoreFont, "Health: " + player.health + "/" + player.maxHealth);
				scoreFont.draw(spriteBatch, guiLayout, 210, 57);
				
				switch(phase){
				
				case 0:
					
					guiLayout = new GlyphLayout(scoreFont, "Press Left and Right Arrow Keys To Move.");
					scoreFont.draw(spriteBatch, guiLayout, 190, 300);
					
					break;
				case 1:
					
					guiLayout = new GlyphLayout(scoreFont, "Press spacebar To Jump.");
					scoreFont.draw(spriteBatch, guiLayout, 190, 300);
					break;
				case 2:
					
					if(this.contactListener.isPlayerOnLadder()){
					
						guiLayout = new GlyphLayout(scoreFont, "Hold the up arrow to climb ladders.");
						scoreFont.draw(spriteBatch, guiLayout, 190, 300);
					}
					else{
						
						guiLayout = new GlyphLayout(scoreFont, "Look around...");
						scoreFont.draw(spriteBatch, guiLayout, 190, 300);
					}
					break;
				case 3:
					
					guiLayout = new GlyphLayout(scoreFont, "Press  A  to use your basic attack.");
					scoreFont.draw(spriteBatch, guiLayout, 190, 300);
					break;
				case 4:
		
					guiLayout = new GlyphLayout(scoreFont, "Press  S  to use your secondary attack.");
					scoreFont.draw(spriteBatch, guiLayout, 190, 300);
					break;
				case 5:
					
					guiLayout = new GlyphLayout(scoreFont, "Press  D  to use your third ability.");
					scoreFont.draw(spriteBatch, guiLayout, 190, 300);
					break;
				case 6:
					
					guiLayout = new GlyphLayout(scoreFont, "Press F to use your fourth ability.");
					scoreFont.draw(spriteBatch, guiLayout, 190, 300);
					break;
				case 7:
					
					guiLayout = new GlyphLayout(scoreFont, "Find the portal...");
					scoreFont.draw(spriteBatch, guiLayout, 190, 300);
				}
					
				
				for(Chest chest : chests){
					
					if(chest.isTouched && player.money >= 100){
						
						guiLayout = new GlyphLayout(scoreFont, "Press  E  to open the chest.");
						scoreFont.draw(spriteBatch, guiLayout, 200, 310);
					}
				}
				
				spriteBatch.end();
			
				int i = 0;
				for (i = 0; i < difficulty && (((System.currentTimeMillis() - spawnTimer)/1000 >= 1)) && teleporter.isActive; i++){
			
					createMonster(false);
				}
				if(i >= difficulty){
					
					spawnTimer = System.currentTimeMillis();
				}
			
				if(debug){
			
					b2dCam.position.set(player.getPosition().x, player.getPosition().y, 0);
					b2dCam.update();
					b2dr.render(world, b2dCam.combined);
				}
				
				
				if(player.getBody().getPosition().y < 35 && difficulty == 3){
					
					player.getBody().setTransform(new Vector2(47, 48), 0);
				}
				
				if(contactListener.isPlayerInLava()){
					
					player.health -= 1;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
	
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		
	}

	
	/**
	 * Change level.
	 */
	private void changeLevel(){
		
		
		crystals.clear();
		chests.clear();
		transitionItems.clear();
		floatingItemList.clear();
		removeMobs.clear();
		monsterList.clear();		
		chests.clear();
		launchers.clear();

		monsterNum = 0;
		monsterList.ordered = false;
		
		world.dispose();
		
		if(difficulty == 3){
			
			game.setScreen(new GameOverScreen(game));
			
			difficulty ++;
		}else{
			
			world = new World(new Vector2(0, -9.81f), true);
			contactListener = new MyContactListener(this);
			world.setContactListener(contactListener);

			difficulty ++;
			
			createTiles();
			createCrystals();
			createChests();
			createPortal();
			createLaunchers();
			createPlayer();

			
			portalStart = 0;
					
			stateTime = 0f;
			
			spawnTimer = 0;
		}
	}

	/**
	 * Creates the player.
	 */
	private void createPlayer(){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		//Create Player
		switch(difficulty){
		
			case 1: 
			
				bdef.position.set(3, 6);
				break;
			case 2:
			
				bdef.position.set(10, 63);
				break;
			case 3:

				bdef.position.set(47, 48);
				break;
		}
		//bdef.position.set(teleporter.getPosition().x, teleporter.getPosition().y + 1);

		bdef.type = BodyType.DynamicBody;
		//bdef.linearVelocity.set(1f, 0);
		Body body = world.createBody(bdef);
		
		shape.setAsBox(
				((Player.PLAYER_WIDTH * SCALE) / 2) / PPM, 
				((Player.PLAYER_HEIGHT * SCALE) / 2) / PPM + 1/PPM);
	//	shape.setAs
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_GROUND | BIT_CHEST | BIT_BULLET | BIT_ATTACK | BIT_LADDER | BIT_PORTAL | BIT_ITEM | BIT_LAUNCHER | BIT_LAVA;
		body.createFixture(fdef).setUserData("player");
		body.setUserData(player);
		//Create Player

		if(difficulty == 1)
			player = new Player(body, this, this.charType);
		
		else
			player.setBody(body);

		player.setState(1);
		player.getBody().setUserData(player);

		
		//Create foot sensor
		shape.setAsBox(
				(((Player.PLAYER_WIDTH - 2) / 2) * SCALE) / PPM, 
				(((Player.PLAYER_HEIGHT / 7) / 2) * SCALE) / PPM, 
				new Vector2(0, -(Player.PLAYER_HEIGHT / 2 * SCALE) / PPM),
				0);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_GROUND | BIT_LADDER;	
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
		
	}

	/**
	 * Creates monsters.
	 *
	 * @param boss the boss
	 */
	private void createMonster(boolean boss){
		int monsterType;
		if(!boss){
			monsterType = (int) (Math.random() * 4) + 1;
		}else{
			monsterType = 5;
		}
		
		float width = MONSTER_WIDTH[monsterType];
		float height = MONSTER_HEIGHT[monsterType];
		
		BodyDef b1def = new BodyDef();
		FixtureDef f1def = new FixtureDef();
		PolygonShape shape1 = new PolygonShape();
		
		Vector2 position;
		
		if(!boss){
			//get crystal spawn point
			position = crystals.random().getPosition();
		}else{
			position = new Vector2(teleporter.getPosition().x, teleporter.getPosition().y + (MONSTER_HEIGHT[monsterType] / 2) / PPM);
		}
		
		//Create Monster
		b1def.position.set(position);
		b1def.type = BodyType.DynamicBody;
		
		Body body1 = world.createBody(b1def);
		
		shape1.setAsBox(
				((width * SCALE) / 2) / PPM, 
				((height * SCALE) / 2) / PPM);
		//shape.setAs
		f1def.shape = shape1;
		f1def.filter.categoryBits = BIT_MONSTER;
		f1def.filter.maskBits = BIT_GROUND | BIT_BULLET | BIT_MINE | BIT_LAUNCHER | BIT_EXPLOSION | BIT_LAVA;

		body1.createFixture(f1def).setUserData("monster:" + monsterNum);

		//Create Monster
		Monster m = new Monster(body1, this, monsterNum, monsterType);
		monsterList.add(m);
		monsterList.peek().setState(1);
		/**Might be able to remove the line below*/
		body1.setUserData(m);
		m.getBody().setUserData(m);
		
		//Create foot sensor
		
		shape1.setAsBox(
				(((width - 2) / 2) * SCALE) / PPM, 
				(((height / 7) / 2) * SCALE) / PPM, 
				new Vector2(0, -(height / 2 * SCALE) / PPM),
				0);
		f1def.shape = shape1;
		f1def.filter.categoryBits = BIT_MONSTER;
		f1def.filter.maskBits = BIT_GROUND;	
		f1def.isSensor = true;
		body1.createFixture(f1def).setUserData("mfoot");
		
		//Create jump sensor
		shape1.setAsBox(
				(((width + 5) / 2) * SCALE) / PPM, 
				(((height / 7) / 2) * SCALE) / PPM,
				new Vector2(0, (((-height / 2)) * SCALE / PPM) + ((monsterList.peek().jumpHeight))),
				0);
		f1def.shape = shape1;
		f1def.filter.categoryBits = BIT_MONSTER;
		f1def.filter.maskBits = BIT_GROUND;	
		f1def.isSensor = true;
		body1.createFixture(f1def).setUserData("mjump");
		
		//Create wall sensor
		shape1.setAsBox(
				(((width + 2) / 2) * SCALE) / PPM, 
				(((height - 2) / 2) * SCALE) / PPM);
		f1def.shape = shape1;
		f1def.filter.categoryBits = BIT_MONSTER;
		f1def.filter.maskBits = BIT_GROUND;	
		f1def.isSensor = true;
		body1.createFixture(f1def).setUserData("mwall");
		body1.setUserData(m);
		monsterNum ++;
	}	

	/**
	 * Creates the tiles.
	 */
	private void createTiles(){
		
		
		switch(difficulty){
		
		
		case 1:
			tileMap = new TmxMapLoader().load("first_stage_map.tmx");
			break;
			
		case 2:
			
			tileMap = new TmxMapLoader().load("second_stage_map.tmx");
			break;
			
		case 3:
			
			tileMap = new TmxMapLoader().load("third_stage_map.tmx");
			break;
		}
		
		 tmr = new OrthogonalTiledMapRenderer(tileMap);

		 tileSize = (int) tileMap.getProperties().get("tilewidth");
		 
		 TiledMapTileLayer layer;
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("air");
		 createLayer(layer, BIT_AIR, "air");
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");
		 createLayer(layer, BIT_GROUND, "ground");
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("ladder");
		 createLayer(layer, BIT_LADDER, "ladder");
		 layer = (TiledMapTileLayer) tileMap.getLayers().get("lava");
		 createLayer(layer, BIT_LAVA, "lava");
	}
	

	/**
	 * Creates the layer.
	 *
	 * @param layer the layer
	 * @param bits the bits
	 * @param userData the user data
	 */
	private void createLayer(TiledMapTileLayer layer, short bits, String userData){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		 //Go through all cells in layer
		 for(int row = 0; row < layer.getHeight(); row++){
			 
			 for(int col = 0; col < layer.getWidth(); col++){
				 
				 //Get cell
				 Cell cell = layer.getCell(col, row);
				 
				 //Check if cell exists
				 if (cell == null) continue;
				 if (cell.getTile() == null) continue;
				 
				 //Create a body + fixture from cell
				 
				 bdef.type = BodyType.StaticBody;//Episode 5
				 bdef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM);
				
				 if(bits != BIT_AIR){
					 ChainShape chainShape = new ChainShape();
					 						 
						 Vector2[] vertices = new Vector2[4];
						 vertices[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);//Bottom left corner
						 vertices[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
						 vertices[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);//Upper right corner
						 vertices[3] = new Vector2(tileSize / 2 / PPM, -tileSize / 2 / PPM);
						 chainShape.createChain(vertices);
					
					 if(bits == BIT_LADDER || bits == BIT_LAVA)
						 fdef.isSensor = true;
					 else
						 fdef.isSensor = false;
					 
					 fdef.friction = 0;
					 fdef.filter.categoryBits = bits;
					 fdef.shape = chainShape;
					 switch(bits){
					 case BIT_AIR:
						 break;
					 case BIT_LADDER:
						 fdef.filter.maskBits = BIT_PLAYER;
						 break;
					 case BIT_GROUND:
						 fdef.filter.maskBits = BIT_PLAYER | BIT_MONSTER | BIT_BULLET | BIT_MINE;
						 break;
					 case BIT_LAVA:
						 
						 fdef.filter.maskBits = BIT_PLAYER | BIT_MONSTER;
					 }
				 
					 world.createBody(bdef).createFixture(fdef).setUserData(userData);
				 }
				 

				 /**Have the ground message be a variable passed in depending on what tile is being imported*/
			 }
		 }
	}
	
	/**
	 * Creates the portal.
	 */
	private void createPortal(){
		
		MapLayer layer = tileMap.getLayers().get("teleporter");
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		for (MapObject mapObject : layer.getObjects()){
			
			bdef.type = BodyType.StaticBody;
			float x = mapObject.getProperties().get("x", Float.class)/ PPM;
			float y = mapObject.getProperties().get("y", Float.class)/ PPM;
	
			bdef.position.set(x,y);
			PolygonShape squareShape = new PolygonShape();
			squareShape.setAsBox(45 / PPM, 25 / PPM);
			
			fdef.shape = squareShape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = BIT_PORTAL;
			fdef.filter.maskBits = BIT_PLAYER;
			
			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("portal");
			
			teleporter = new Teleporter(body);
			teleporter.getBody().setUserData(teleporter);//Might be able to be removed later
		}
	}
	
	/**
	 * Creates the launchers.
	 */
	private void createLaunchers(){
	
		MapLayer layer = tileMap.getLayers().get("launcher");
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		for (MapObject mapObject : layer.getObjects()){
			
			bdef.type = BodyType.StaticBody;
			float x = mapObject.getProperties().get("x", Float.class)/ PPM;
			float y = mapObject.getProperties().get("y", Float.class)/ PPM;

			bdef.position.set(x,y);
			PolygonShape squareShape = new PolygonShape();
			squareShape.setAsBox(14 / PPM, 63 / PPM);
			
			fdef.shape = squareShape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = BIT_LAUNCHER;
			fdef.filter.maskBits = BIT_PLAYER | BIT_MONSTER;
			
			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("launcher");
			
			Launcher c = new Launcher(body);
			launchers.add(c);

			c.getBody().setUserData(c);
			body.setUserData(c);
		}
	}
	
	/**
	 * Creates the chests.
	 */
	private void createChests(){
				
		MapLayer layer = tileMap.getLayers().get("chest");
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		for (MapObject mapObject : layer.getObjects()){
			
			bdef.type = BodyType.StaticBody;
			float x = mapObject.getProperties().get("x", Float.class)/ PPM;
			float y = mapObject.getProperties().get("y", Float.class)/ PPM;

			bdef.position.set(x,y);
			PolygonShape squareShape = new PolygonShape();
			squareShape.setAsBox(15 / PPM, 10 / PPM);
			
			fdef.shape = squareShape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = BIT_CHEST;
			fdef.filter.maskBits = BIT_PLAYER;
			
			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("chest");
			
			Chest c = new Chest(body);
			chests.add(c);
			
			
			c.getBody().setUserData(c);
			body.setUserData(c);
		}
	}
	
	/**
	 * Creates the item.
	 *
	 * @param chest the chest
	 */
	private void createItem(Chest chest){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		bdef.position.set((chest.getBody().getPosition().x * 100) / PPM, (chest.getBody().getPosition().y * 100) / PPM);
		bdef.type = BodyType.DynamicBody;

		shape.setAsBox(16 * SCALE / PPM, 16 * SCALE / PPM, new Vector2(0, 8f * SCALE / PPM), 0);
		fdef.shape = shape;
		fdef.isSensor = true;
		fdef.filter.categoryBits = BIT_ITEM;
		fdef.filter.maskBits = BIT_PLAYER;
		
		Body body = world.createBody(bdef);
		body.createFixture(fdef).setUserData("item:" + itemNum);
		body.setGravityScale(0);		
		Item i = new Item(body, this, ((int) (Math.random() * 10) + 1), itemNum);
		floatingItemList.add(i);
		
		i.getBody().setUserData(i);
	}
	
	/**
	 * Creates the crystals.
	 */
	private void createCrystals(){
		
		crystals = new Array<Crystal>();
		
		MapLayer layer = tileMap.getLayers().get("spawner");
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		for (MapObject mapObject : layer.getObjects()){
			
			bdef.type = BodyType.StaticBody;
			float x = mapObject.getProperties().get("x", Float.class)/ PPM;
			float y = mapObject.getProperties().get("y", Float.class)/ PPM;

			bdef.position.set(x,y);
			CircleShape circleShape = new CircleShape();
			circleShape.setRadius(8 / PPM);
			
			fdef.shape = circleShape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = BIT_CHEST;
			fdef.filter.maskBits = BIT_PLAYER;
			
			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("crystal");
			
			Crystal c = new Crystal(body);
			crystals.add(c);
			c.getBody().setUserData(c);
			body.setUserData(c);
		}
	}
}

/**Group index filtering - look it up later*/

