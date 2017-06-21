import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

// TODO: Auto-generated Javadoc
/**
 * The Class Item.
 */
public class Item extends B2DSprite{
	
	/** The identifier. */
	public float identifier;

	/** The game screen. */
	GameScreen gameScreen;
	
	/** The type. */
	int type;
	
	/** The item num. */
	int itemNum;
	
	/** The item count. */
	int itemCount = 1;

	/** The image. */
	private Animation<TextureRegion> image;
	
	/**
	 * Instantiates a new item.
	 *
	 * @param body the body
	 * @param gamescreen the gamescreen
	 * @param item the item
	 * @param identity the identity
	 */
	public Item(Body body, GameScreen gamescreen, int item, float identity){
		
		super(body);
		
		gameScreen = gamescreen;
		this.type = item;
		this.identifier = identity;
		
		Texture texture = GameScreen.textures.getTexture("items");
		TextureRegion[] sprites;
		
		this.width = 32f;
		this.height = 32f;
		
		sprites = new TextureRegion[10];
		sprites = TextureRegion.split(texture, 32, 32)[0];
		image = new Animation<TextureRegion>(0.07f, sprites[type - 1]);
	}
	
	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public void getItem(){
		switch(type){
		case 1: //root
			GameScreen.player.increaseMaxHealth(10);
			break;
		case 2: //piggy bank
			GameScreen.player.increaseGoldGain(0.02f);
			break;
		case 3: //smart shopper
			GameScreen.player.increaseGoldLeech(2f);
			break;
		case 4: //infusion
			GameScreen.player.increaseHealthSteal(2f);
			break;
		case 5: //leech
			GameScreen.player.increaseHealthLeech(0.1f);
			break;
		case 6: //double jump
			GameScreen.player.increaseJumps(1);
			break;
		case 7: //attack speed
			GameScreen.player.increaseAttackSpeed(0.01f);
			break;
		case 8: //goat hoof
			GameScreen.player.increaseMoveSpeed(0.2f);
			break;
		case 9: //boxing glove
			GameScreen.player.increaseKnockbackChance(0.1f);
			break;
		case 10: //mortar
			GameScreen.player.increaseMortarChance(0.1f);
			break;
		}
	}
	
	/**
	 * Write item.
	 *
	 * @param spritebatch the spritebatch
	 */
	public void writeItem(SpriteBatch spritebatch){
		
		spritebatch.draw(image.getKeyFrame(gameScreen.stateTime, false), this.itemNum * 32, 80, this.width, this.height);
		
		if(this.itemCount > 1){
			
			gameScreen.scoreFont.draw(spritebatch, new GlyphLayout(gameScreen.scoreFont, "x" + this.itemCount), this.itemNum * 32 + 2 * this.width / 3, 88);
		}
	}
	
	/**
	 * Draw item.
	 *
	 * @param spritebatch the spritebatch
	 */
	public void drawItem(SpriteBatch spritebatch){
		
		spritebatch.draw(image.getKeyFrame(gameScreen.stateTime, false), this.getPosition().x * 100 + (this.width / GameScreen.PPM) / 2, this.getPosition().y * 100 - (this.height / GameScreen.PPM) / 2, 0, 0, this.width, this.height, 1, 1, 0);
	}
}