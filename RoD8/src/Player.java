import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * The Class Player.
 */
public class Player extends B2DSprite{

	public float money;

	public float maxHealth = 100f;

	public float health = maxHealth;
	
	public float goldGain = 0.02f;

	public float goldLeech = 10f;

	public float HealthSteal = 0f;

	public float HealthLeech = 0f;
	
	public int totalJumps = 1;
	
	public int jumpCount;
	
	public float attackTime = 0.07f;
	
	public float moveSpeed = 1f;
	
	public float knockbackChance = 0f;
	
	public float mortarChance = 0f;

	/** The Constant PLAYER_WIDTH. */
	public static float PLAYER_WIDTH = 8f;

	/** The Constant PLAYER_HEIGHT. */
	public static float PLAYER_HEIGHT = 20f;

	Animation<TextureRegion> runRight;

	Animation<TextureRegion> jumpRight;

	Animation<TextureRegion> standingRight;

	Animation<TextureRegion> climbing;

	Animation<TextureRegion> primaryRight;

	Animation<TextureRegion> secondaryRight;

	Animation<TextureRegion> tertiaryRight;

	Animation<TextureRegion> quaternaryRight;
	
	private float animTime;
	private int framesRun;
	
	private int type;
	private TextureRegion prevFrame = null;
	private GameScreen gameScreen;

	
	/**
	 * Instantiates a new player.
	 *
	 * @param body the body
	 */
	public Player(Body body, GameScreen gameScreen, int type){//Add a type int later to determing which animations will be loaded in
		
		super(body);
		
		this.gameScreen = gameScreen;
		
		this.type = type;
		
		money = 0;
		
		switch(this.type){
			
		case 1:
			this.createCommando();
			break;
		case 2:
			this.createSniper();
			break;
		}
		
		framesRun = 0;
	}
	
	/**Framesrun needs to be changed depending on the character/class*/
	public void drawPlayer(SpriteBatch spriteBatch, float stateTime){
			
		//spriteBatch.begin();
		
		switch(this.type){
		
		case 1:
				drawCommando(spriteBatch, stateTime);
				break;
		case 2:
				drawSniper(spriteBatch, stateTime);
				break;
		case 3:
				
				break;
		}
	}
	
	/**
	 * Update player movement.
	 */
	public void updateMovement(){
		
		if (this.getState() <= 3){
			
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
			
				if(this.jumpCount > 0){		
			
					this.getBody().applyLinearImpulse(new Vector2(0f, 2.8f), this.getPosition(), true);
					
					this.setState(2);
					
					this.jumpCount -= 1;
				}
			}
		
			if (Gdx.input.isKeyPressed(Keys.UP) && gameScreen.contactListener.isPlayerOnLadder()){
				
				this.setState(0);
				this.getBody().setLinearVelocity(new Vector2(0f, 1.5f));
			}
			
			if(Gdx.input.isKeyPressed(Keys.LEFT)){	
		
				this.setState(3);
				this.setFace(false);//CHANGE!!!
		
				if(this.getBody().getLinearVelocity().x > -moveSpeed)
					this.getBody().applyLinearImpulse(new Vector2(-moveSpeed / 2, 0f), this.getPosition(), true);
			}
		
			if(Gdx.input.isKeyPressed(Keys.RIGHT)){
						
				this.setState(3);
				this.setFace(true);
		
				if(this.getBody().getLinearVelocity().x < moveSpeed)			
					this.getBody().applyLinearImpulse(new Vector2(moveSpeed / 2, 0f), this.getPosition(), true);
			}
		
			if(!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT) && gameScreen.contactListener.isPlayerOnGround() == true){
			
				this.setState(1);
			}
	
			if(gameScreen.contactListener.isPlayerOnGround() == false && gameScreen.contactListener.isPlayerOnLadder() == false){
			
				this.setState(2);
			}
			
			if(Gdx.input.isKeyPressed(Keys.A)){
			
				this.setState(4);
			}
			
			if(Gdx.input.isKeyPressed(Keys.S)){
				
				this.setState(5);
				
				if(this.type == 2){
					
					if(this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
						this.getBody().applyLinearImpulse(new Vector2(-6f, 0f), this.getPosition(), true);
					else if(!this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
						this.getBody().applyLinearImpulse(new Vector2(6f, 0f), this.getPosition(), true);
				}
			}
			
			if(Gdx.input.isKeyPressed(Keys.D)){
				
				this.setState(6);
				
				if(this.type == 1){
					
					if(this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
						this.getBody().applyLinearImpulse(new Vector2(0.75f, 0f), this.getPosition(), true);
					else if(!this.getFacing() && gameScreen.contactListener.isPlayerOnGround())
						this.getBody().applyLinearImpulse(new Vector2(-0.75f, 0f), this.getPosition(), true);
				}
					
			}
			
			if(Gdx.input.isKeyPressed(Keys.F)){
				
				this.setState(7);
			}
		}
		
		if(gameScreen.contactListener.isPlayerOnGround() && this.getState() != 6)	
			
			this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().x * 0.9f, this.getBody().getLinearVelocity().y);
	}
	
	public void increaseMaxHealth(float increase){
		
		this.maxHealth += increase;
		this.health += increase;
	}
	
	public void increaseGoldGain(float increase){
		this.goldGain += increase;
	}
	
	public void increaseGoldLeech(float increase){
		this.goldLeech += increase;
	}
	
	public void increaseHealthSteal(float increase){
		this.HealthSteal += increase;
	}
	
	public void increaseHealthLeech(float increase){
		this.HealthLeech += increase;
	}
	
	public void increaseJumps(int increase){
		this.totalJumps += 1;
	}
	
	public void increaseAttackSpeed(float increase){
		this.attackTime -= increase;
		
		Texture texture;
		TextureRegion[] sprites;
		
		switch(this.type){
		case 1:
			texture = GameScreen.textures.getTexture("commando");

			sprites = new TextureRegion[5];
			sprites = TextureRegion.split(texture, 18, 13)[2];
			primaryRight = new Animation<TextureRegion>(attackTime, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4]});
			break;
		case 2:
			
			texture = GameScreen.textures.getTexture("sniper");

			sprites = new TextureRegion[5];
			sprites = TextureRegion.split(texture, 18, 13)[2];
			primaryRight = new Animation<TextureRegion>(attackTime, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4]});
			break;
			
		}
	}
	
	public void increaseMoveSpeed(float increase){
		this.moveSpeed += increase;
	}
	
	public void increaseKnockbackChance(float increase){
		this.knockbackChance += increase;
	}
	
	public void increaseMortarChance(float increase){
		this.mortarChance += increase;
	}
	
	public void increaseAnimTime(float value){animTime += value;}

	private void drawCommando(SpriteBatch spriteBatch, float stateTime){
		
		switch(this.getState()){
		
		case 0:
			
			spriteBatch.draw(climbing.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 3) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 1: 		
			
			if(this.getFacing()){
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 3) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 3) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 2:
			
			if(this.getBody().getLinearVelocity().x >= 0){
			
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 3:
			
			if(this.getFacing())
				spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
			else
				spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
	
			break;
		case 4:
	
			if (prevFrame != primaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 0 || framesRun == 3){
					
					gameScreen.createBullet("bullet:10.00", this.getFacing());
					
					if(Math.random() < this.mortarChance){
						gameScreen.createMortar(20f, this.getFacing());
					}
				}
				framesRun++;
				prevFrame = primaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <= 5){
	
				if(this.getFacing())
					spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - (PLAYER_WIDTH/2 * GameScreen.SCALE), this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 18, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + (PLAYER_WIDTH/2 * GameScreen.SCALE), this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 18, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		case 5:
			
			if (prevFrame != secondaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 1){
					
					gameScreen.createBullet("ray:10.00", this.getFacing());
				}
				framesRun++;
				prevFrame = secondaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <= 5){
				
				if(this.getFacing())
					spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 33, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 33, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);				
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			
			break;
		case 6:
					
			if (prevFrame != tertiaryRight.getKeyFrame(animTime, true)){
				
				framesRun++;
				prevFrame = tertiaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <=  9){
			
				if(this.getFacing())
					spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - GameScreen.SCALE * PLAYER_WIDTH/1.5f, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 12, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + GameScreen.SCALE * PLAYER_WIDTH/1.5f, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 12, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
	
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		case 7:
			
			if (prevFrame != quaternaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 1 || framesRun == 5 || framesRun == 9){
					
					gameScreen.createBullet("bullet:10.00", this.getFacing());
				}
				else if(framesRun == 3 || framesRun ==7 || framesRun == 11){
					
					gameScreen.createBullet("bullet:10.00", !this.getFacing());
				}
				framesRun++;
				prevFrame = quaternaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <= 15){
				
				if(this.getFacing())
					spriteBatch.draw(quaternaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH * 2.2f * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 40, PLAYER_HEIGHT, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(quaternaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH * 2.2f * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 5) * GameScreen.SCALE, 0, 0, 40, PLAYER_HEIGHT, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		}	
		
		//spriteBatch.end();
	}

	private void drawSniper(SpriteBatch spriteBatch, float stateTime){
		
		switch(this.getState()){
		
		case 0:
			
			spriteBatch.draw(climbing.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH * 1.5f, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
			break;
		case 1: 		
			
			if(this.getFacing()){
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH * 1.5f, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(standingRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH * 1.5f, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 2:
			
			if(this.getBody().getLinearVelocity().x >= 0){
			
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH * 1.5f, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				spriteBatch.draw(jumpRight.getKeyFrame(stateTime, false), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH * 1.5f, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			break;
		case 3:
			
			if(this.getFacing())
				spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH * 1.5f, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
			else
				spriteBatch.draw(runRight.getKeyFrame(stateTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, PLAYER_WIDTH * 1.5f, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);
	
			break;
		case 4:
	
			if (prevFrame != primaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 2){
					
					gameScreen.createBullet("bullet:10.00", this.getFacing());

					if(Math.random() < this.mortarChance){
						gameScreen.createMortar(20f, this.getFacing());
					}
				}
				framesRun++;
				prevFrame = primaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <= 6){
	
				if(this.getFacing())
					spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - (PLAYER_WIDTH/2 * GameScreen.SCALE), this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE * 1.5f, 0, 0, 18, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(primaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + (PLAYER_WIDTH/2 * GameScreen.SCALE), this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE * 1.5f, 0, 0, 18, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		case 5:
			
			if (prevFrame != secondaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 1){
					
					gameScreen.createBullet("ray:10.00", this.getFacing());
				}
				framesRun++;
				prevFrame = secondaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <= 12){
				
				if(this.getFacing())
					spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, 33 * 1.5f, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(secondaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH/2 * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2) * GameScreen.SCALE, 0, 0, 33 * 1.5f, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);				
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			
			break;
		case 6:
					
			if (prevFrame != tertiaryRight.getKeyFrame(animTime, true)){
				
				framesRun++;
				prevFrame = tertiaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <=  6){
			
				if(this.getFacing())
					spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - GameScreen.SCALE * 30, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 1) * GameScreen.SCALE, 0, 0, 87, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(tertiaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + GameScreen.SCALE * 30, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 1) * GameScreen.SCALE, 0, 0, 87, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);
	
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		case 7:
			
			if (prevFrame != quaternaryRight.getKeyFrame(animTime, true)){
				
				if(framesRun == 1 || framesRun == 5 || framesRun == 9){
					
					gameScreen.createBullet("bullet:10.00", this.getFacing());
				}
				else if(framesRun == 3 || framesRun ==7 || framesRun == 11){
					
					gameScreen.createBullet("bullet:10.00", !this.getFacing());
				}
				framesRun++;
				prevFrame = quaternaryRight.getKeyFrame(animTime, true);
			}
	
			if (framesRun <= 15){
				
				if(this.getFacing())
					spriteBatch.draw(quaternaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 - PLAYER_WIDTH * 2.2f * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 1) * GameScreen.SCALE, 0, 0, 40 * 1.5f, PLAYER_HEIGHT * 1.5f, GameScreen.SCALE, GameScreen.SCALE, 0);
				else
					spriteBatch.draw(quaternaryRight.getKeyFrame(animTime, true), this.getBody().getPosition().x * 100 + PLAYER_WIDTH * 2.2f * GameScreen.SCALE, this.getBody().getPosition().y * 100 - (PLAYER_HEIGHT/2 + 1) * GameScreen.SCALE, 0, 0, 40 * 1.5f, PLAYER_HEIGHT * 1.5f, -GameScreen.SCALE, GameScreen.SCALE, 0);
			}
			else{
				
				this.setState(0);
				framesRun = 0;
				animTime = 0;
				prevFrame = null;
			}
			break;
		}	
		
		//spriteBatch.end();
	}

	private void createCommando(){
		
		Texture texture = GameScreen.textures.getTexture("commando");
		TextureRegion[] sprites = new TextureRegion[4];
		
		sprites = TextureRegion.split(texture, 7, 13)[0];
		standingRight = new Animation<TextureRegion>(0.07f, sprites[0]);
		jumpRight = new Animation<TextureRegion>(0.07f, sprites[1]);
	
		sprites = new TextureRegion[2];
		sprites = TextureRegion.split(texture, 7, 13)[0];
		climbing = new Animation<TextureRegion>(0.25f, new TextureRegion[]{sprites[2], sprites[3]});
	
		sprites = new TextureRegion[8];
		sprites = TextureRegion.split(texture, 7, 13)[1];
		runRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7]});
	
		sprites = new TextureRegion[5];
		sprites = TextureRegion.split(texture, 18, 13)[2];
		primaryRight = new Animation<TextureRegion>(attackTime, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4]});
		
		sprites = new TextureRegion[5];
		sprites = TextureRegion.split(texture, 33, 13)[3];
		secondaryRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4]});
		
		sprites = new TextureRegion[9];
		sprites = TextureRegion.split(texture, 12, 13)[4];
		tertiaryRight = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8]});
		
		sprites = new TextureRegion[15];
		sprites = TextureRegion.split(texture, 40, 13)[5];
		quaternaryRight = new Animation<TextureRegion>(0.07f,
				new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8], sprites[9], sprites[10], sprites[11], sprites[12], sprites[13], sprites[14]});
		
	}

	private void createSniper(){
		
		Texture texture = GameScreen.textures.getTexture("sniper");
		TextureRegion[] sprites = new TextureRegion[4];
		
		sprites = TextureRegion.split(texture, 17, 14)[0];
		standingRight = new Animation<TextureRegion>(0.07f, sprites[0]);
		jumpRight = new Animation<TextureRegion>(0.07f, sprites[1]);
	
		sprites = new TextureRegion[5];
		sprites = TextureRegion.split(texture, 12, 12)[0];
		climbing = new Animation<TextureRegion>(0.5f, new TextureRegion[]{sprites[3], sprites[4]});
	
		
		sprites = new TextureRegion[8];
		sprites = TextureRegion.split(texture, 17, 14)[1];
		runRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7]});
	
		sprites = new TextureRegion[6];
		sprites = TextureRegion.split(texture, 18, 13)[2];
		primaryRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5]});
		
		sprites = new TextureRegion[12];
		sprites = TextureRegion.split(texture, 19, 20)[4];
		secondaryRight = new Animation<TextureRegion>(0.07f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8], sprites[9], sprites[10], sprites[11]});
		
		sprites = new TextureRegion[9];
		sprites = TextureRegion.split(texture, 87, 18)[6];
		tertiaryRight = new Animation<TextureRegion>(0.1f, new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5]});
		
		//Not Needed
		sprites = new TextureRegion[15];
		sprites = TextureRegion.split(texture, 40, 13)[5];
		quaternaryRight = new Animation<TextureRegion>(0.07f,
				new TextureRegion[]{sprites[0], sprites[1], sprites[2], sprites[3], sprites[4], sprites[5], sprites[6], sprites[7], sprites[8], sprites[9], sprites[10], sprites[11], sprites[12], sprites[13], sprites[14]});
		
	}

}