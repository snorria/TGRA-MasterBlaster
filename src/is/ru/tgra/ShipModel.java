package is.ru.tgra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Snorri
 * Date: 9.11.2013
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
public class ShipModel {
    FloatBuffer vertexBuffer;
    FloatBuffer texCoordBuffer;
    Texture tex;
    public ShipModel()
    {
        vertexBuffer = BufferUtils.newFloatBuffer(84);
        //body
        vertexBuffer.put(new float[] {-0.35f, -0.5f, -0.25f, -0.25f, 0.25f, -0.10f, //toppur
                0.35f, -0.5f, -0.25f, 0.25f, 0.25f, -0.10f, //toppur
                0.35f, -0.5f, -0.25f, 0.25f, 0.25f, -0.10f, //aftarihægrihlið
                0.5f, -0.5f, 0.0f, 1.00f, 0.4f, 0.0f, //aftarihægrihlið
                0.35f, -0.5f, -0.25f, 0.5f, -0.5f, 0.0f, //fremrihægrihlið
                0.1f, -1.5f, 0.0f, 0.1f, -1.5f, 0.0f, //fremrihægrihlið
                -0.35f, -0.5f, -0.25f, -0.25f, 0.25f, -0.10f, //aftarivinstrihlið
                -0.5f, -0.5f, 0.0f, -1.00f, 0.4f, 0.0f, //aftarivinstrihlið
                -0.35f, -0.5f, -0.25f, -0.5f, -0.5f, 0.0f, //fremrivinstrihlið
                -0.1f, -1.5f, 0.0f, -0.1f, -1.5f, 0.0f, //fremrivinstrihlið
                -0.25f, 0.25f, -0.10f, 0.25f, 0.25f, -0.10f, //efrabak
                -1.00f, 0.4f, 0.0f, 1.00f, 0.4f, 0.0f, //efrabak
                -0.35f, -0.5f, -0.25f, 0.35f, -0.5f, -0.25f, //fremritoppur
                -0.1f, -1.5f, 0.0f, 0.1f, -1.5f, 0.0f}); //fremritoppur
        vertexBuffer.rewind();

        /*texCoordBuffer = BufferUtils.newFloatBuffer(48);
        texCoordBuffer.put(new float[] {0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f});
        texCoordBuffer.rewind();
*/
        //tex = new Texture(Gdx.files.internal(texture));
    }
    public void draw()
    {
        //TODO: Normals.....
        Gdx.gl11.glShadeModel(GL11.GL_SMOOTH);
        Gdx.gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);
        Gdx.gl11.glNormal3f(0.0f, 0.0f, -1.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        Gdx.gl11.glNormal3f(1.0f, 0.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 4, 4);
        Gdx.gl11.glNormal3f(0.0f, 0.0f, 1.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 8, 4);
        Gdx.gl11.glNormal3f(-1.0f, 0.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 12, 4);
        Gdx.gl11.glNormal3f(0.0f, 1.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 16, 4);
        Gdx.gl11.glNormal3f(0.0f, -1.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 20, 4);
        Gdx.gl11.glNormal3f(0.0f, -1.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 24, 4);
        //neðri hlið
        Gdx.gl11.glPushMatrix();
        Gdx.gl11.glRotatef(180.0f,0.0f,1.0f,0.0f);
        Gdx.gl11.glNormal3f(0.0f, 0.0f, -1.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        Gdx.gl11.glNormal3f(1.0f, 0.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 4, 4);
        Gdx.gl11.glNormal3f(0.0f, 0.0f, 1.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 8, 4);
        Gdx.gl11.glNormal3f(-1.0f, 0.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 12, 4);
        Gdx.gl11.glNormal3f(0.0f, 1.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 16, 4);
        Gdx.gl11.glNormal3f(0.0f, -1.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 20, 4);
        Gdx.gl11.glNormal3f(0.0f, -1.0f, 0.0f);
        Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 24, 4);
        Gdx.gl11.glPopMatrix();
    }
}
