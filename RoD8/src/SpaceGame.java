import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The Class SpaceGame.
 */
public class SpaceGame extends Game{
	
	/** The batch. */
	public SpriteBatch batch;
	
	/** The scrolling background. */
	public ScrollingBackground scrollingBackground;
	
	/** The Constant WIDTH. */
	public static int WIDTH = 320;
	
	/** The Constant HEIGHT. */
	public static int HEIGHT = 240;
	
	/** The Constant SCALE. */
	public static final int SCALE = 2;
	
	/** The cam. */
	private OrthographicCamera cam;
//	private StretchViewport viewPort;
	
	/* (non-Javadoc)
 * @see com.badlogic.gdx.ApplicationListener#create()
 */
@Override
	public void create (){
		
		scrollingBackground = new ScrollingBackground();
		batch = new SpriteBatch();
		
		cam = new OrthographicCamera();
		//viewPort = new StretchViewport(WIDTH, HEIGHT, cam);
		//viewPort.apply();
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		
		WIDTH = gd.getDisplayMode().getWidth();
		HEIGHT = gd.getDisplayMode().getHeight();
		
		
		cam.setToOrtho(false, WIDTH, HEIGHT);

		this.setScreen(new MainMenu(this));
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#render()
	 */
	@Override
	public void render (){
	
		batch.setProjectionMatrix(cam.combined);

		super.render();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#resize(int, int)
	 */
	@Override
	public void resize(int width, int height){
		
		//this.scrollingBackground.resize(width, height);
		//viewPort.update(width, height);
		super.resize(width, height);
	}
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
	
}
