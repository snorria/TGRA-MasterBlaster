package is.ru.tgra.network;

import is.ru.tgra.Point3D;
import is.ru.tgra.Vector3D;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Toti
 * Date: 16.11.2013
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class Player {
    public String name;
    public Point3D pos;
    public Vector3D forward, left;
    public boolean isDead = false;

    public Player(Point3D pos, Vector3D forward, Vector3D left, String name)
    {
        this.pos = pos;
        this.forward = forward;
        this.left = left;
    }

    public Player(String name)
    {
        Random random = new Random();
        random.setSeed( System.currentTimeMillis());
        this.pos = new Point3D(random.nextFloat(),random.nextFloat(),random.nextFloat());
        this.forward = new Vector3D(1f,0f,0f);
        this.left = new Vector3D(0f,0f,-1f);
        this.name = name;
    }

    public void update(Point3D pos, Vector3D forward, Vector3D left)
    {
        this.pos = pos;
        this.forward = forward;
        this.left = left;
    }
}
