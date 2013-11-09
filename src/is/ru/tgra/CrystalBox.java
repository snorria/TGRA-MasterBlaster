package is.ru.tgra;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;

public class CrystalBox {
	private Cube cube;
	
	public CrystalBox() {
		this.cube = new Cube("assets/textures/star01.bmp");
	}
	
	public void draw()
	{
		Gdx.gl11.glEnable(GL11.GL_ALPHA_TEST);
		Gdx.gl11.glAlphaFunc(GL11.GL_GREATER, 0.03f);
		
		Gdx.gl11.glEnable(GL11.GL_BLEND);
		Gdx.gl11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);
		
		this.cube.draw();
		
		Gdx.gl11.glDisable(GL11.GL_BLEND);
		Gdx.gl11.glDisable(GL11.GL_ALPHA_TEST);
		
		
		
	}
}
