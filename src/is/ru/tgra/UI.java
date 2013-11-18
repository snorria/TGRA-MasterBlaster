package is.ru.tgra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Snorri
 * Date: 16.11.2013
 * Time: 22:58
 * To change this template use File | Settings | File Templates.
 */
public class UI {
    FloatBuffer vertexBuffer;
    FloatBuffer texCoordBuffer;
    Texture tex;
    float DEATHTIMER = 4.0f;
    float t;

    public UI(){
		this.vertexBuffer = BufferUtils.newFloatBuffer(24);
		this.vertexBuffer.put(new float[] {
	            -0.08f, -0.08f, 0.0f,  // 0. left-bottom
                0.08f, -0.08f, 0.0f,  // 1. right-bottom
	            -0.08f, 0.08f, 0.0f,  // 2. left-top
                0.08f, 0.08f, 0.0f,   // 3. right-top
                -1.0f, -1.0f, 0.0f,  // 0. left-bottom
                1.0f, -1.0f, 0.0f,  // 1. right-bottom
                -1.0f, 1.0f, 0.0f,  // 2. left-top
                1.0f, 1.0f, 0.0f   // 3. right-top
		});
		this.vertexBuffer.rewind();

		texCoordBuffer = BufferUtils.newFloatBuffer(8);
		texCoordBuffer.put(new float[] {0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f});
		texCoordBuffer.rewind();
		this.tex = new Texture(Gdx.files.internal("assets/textures/circle-01.png"));
    }
    public void update(float deltaTime){
        t-=deltaTime;
    }
    public void draw(){

        Gdx.gl11.glShadeModel(GL11.GL_SMOOTH);
        Gdx.gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);

        Gdx.gl11.glEnable(GL11.GL_TEXTURE_2D);
        Gdx.gl11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        Gdx.gl11.glEnable(GL11.GL_ALPHA_TEST);
        Gdx.gl11.glAlphaFunc(GL11.GL_GREATER, 0.15f);

        Gdx.gl11.glEnable(GL11.GL_BLEND);
        Gdx.gl11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);

        this.tex.bind();  //Gdx.gl11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        Gdx.gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, texCoordBuffer);

        Gdx.gl11.glNormal3f(0.0f, 0.0f, -1.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        if(t>0.0f){
            System.out.println("I'M DEAD YO");
            Gdx.gl11.glDisable(GL11.GL_TEXTURE_2D);
            Gdx.gl11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            Gdx.gl11.glColor4f(t/4.0f,0.0f,0.0f,1.0f);
            Gdx.gl11.glNormal3f(0.0f, 0.0f, -1.0f);
            Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 4, 4);
            Gdx.gl11.glColor4f(1.0f,1.0f,1.0f,1.0f);
        }
        Gdx.gl11.glDisable(GL11.GL_BLEND);
        Gdx.gl11.glDisable(GL11.GL_ALPHA_TEST);

    }

    public void playerDied(){
        t=DEATHTIMER;
    }
}
