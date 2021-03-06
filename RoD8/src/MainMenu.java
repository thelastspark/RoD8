import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

// TODO: Auto-generated Javadoc
/**
 * The Class MainMenu.
 */
public class MainMenu implements Screen{

	/** The game. */
	SpaceGame game;
	
	/** The exit buttton active. */
	Texture exitButttonActive;

	/** The exit button inactive. */
	Texture exitButtonInactive;

	/** The play button active. */
	Texture playButtonActive;

	/** The play button inactive. */
	Texture playButtonInactive;
	
	/** The splash screen. */
	Texture splashScreen;

	/** The exit button width. */
	private static int EXIT_BUTTON_WIDTH = 300;
	
	/** The exit button height. */
	private static int EXIT_BUTTON_HEIGHT = 150;

	/** The play button width. */
	private static int PLAY_BUTTON_WIDTH = 500;
	
	/** The play button height. */
	private static int PLAY_BUTTON_HEIGHT = 200;
	
	/** The Constant PLAY_BUTTON_Y. */
	private static final int PLAY_BUTTON_Y = 300;
	
	/** The Constant EXIT_BUTTON_Y. */
	private static final int EXIT_BUTTON_Y = 125;
	
	/**
	 * Instantiates a new main menu.
	 *
	 * @param game the game
	 */
	public MainMenu(SpaceGame game){
	
		this.game = game;
		
		splashScreen = new Texture("risk of drizzle.png");
		playButtonActive = new Texture("play_button_active.png");
		playButtonInactive = new Texture("play_button_inactive.png");
		exitButttonActive = new Texture("exit_button_active.png");
		exitButtonInactive = new Texture("exit_button_inactive.png");

		game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
	
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();

		game.batch.draw(splashScreen, SpaceGame.WIDTH / 2 - SpaceGame.HEIGHT / 2 + 20, 20, SpaceGame.HEIGHT - 40, SpaceGame.HEIGHT - 40);
		
		int x = SpaceGame.WIDTH/2 - EXIT_BUTTON_WIDTH/2;

		if (Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && SpaceGame.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && SpaceGame.HEIGHT - Gdx.input.getY() >  EXIT_BUTTON_Y){
			
			game.batch.draw(exitButttonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
			
			if(Gdx.input.isTouched()){
				
				Gdx.app.exit();
			}
		}else{
			
			game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
		}
		
		x = SpaceGame.WIDTH/2 - PLAY_BUTTON_WIDTH/2;
		
		if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && SpaceGame.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && SpaceGame.HEIGHT - Gdx.input.getY() >  PLAY_BUTTON_Y){
			
			game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
			
			if(Gdx.input.isTouched()){
				
				game.setScreen(new CharacterScreen(game));
			}
		}else{
			
			game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		}
		
		game.batch.end();
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

}
