package is.ru.tgra;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Snorri
 * Date: 16.11.2013
 * Time: 23:58
 * To change this template use File | Settings | File Templates.
 */
public class Shot {
    Point3D startPos;
    Point3D endPos;
    float timeElapsed;
    float LIFETIME = 0.5f;
    FloatBuffer vertexBuffer;
    ShotDelegate delegate;

    public Shot(Point3D startPos, Point3D endPos,ShotDelegate delegate){
        this.delegate = delegate;
        this.startPos = startPos;
        this.endPos = endPos;
        timeElapsed = 0.0f;
        this.vertexBuffer = BufferUtils.newFloatBuffer(6);
        this.vertexBuffer.put(new float[] {
                0.0f,0.0f,0.0f,
                this.endPos.x, this.endPos.y, this.endPos.z
        });
        this.vertexBuffer.rewind();
    }
    public void update(float deltaTime)
    {
        timeElapsed+=deltaTime;
        if(timeElapsed>LIFETIME)
        {
            delegate.shotDead(this);
        }
    }
    public void draw()
    {
        Gdx.gl11.glShadeModel(GL11.GL_SMOOTH);
        Gdx.gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);
        if(timeElapsed<=LIFETIME){
            Gdx.gl11.glPushMatrix();
            Gdx.gl11.glTranslatef(this.startPos.x,this.startPos.y,this.startPos.z);
            Gdx.gl11.glDisable(GL11.GL_LIGHTING);
            Gdx.gl11.glColor4f(0.0f,0.0f,1.0f,1.0f);
            if(timeElapsed<0.2)
                Gdx.gl11.glLineWidth(3.5f);
            else if(timeElapsed<0.3)
                Gdx.gl11.glLineWidth(2.0f);
            else if(timeElapsed<0.4)
                Gdx.gl11.glLineWidth(1.5f);
            else
                Gdx.gl11.glLineWidth(1.0f);
            Gdx.gl11.glDrawArrays(GL11.GL_LINES, 0, 2);
            Gdx.gl11.glLineWidth(1.0f);
            Gdx.gl11.glColor4f(1.0f,1.0f,1.0f,1.0f);
            Gdx.gl11.glEnable(GL11.GL_LIGHTING);
            Gdx.gl11.glPopMatrix();
        }
    }
}
