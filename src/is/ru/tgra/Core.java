package is.ru.tgra;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL11;
import is.ru.tgra.network.GameState;
import is.ru.tgra.network.NetworkThread;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Snorri
 * Date: 9.11.2013
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public class Core implements ApplicationListener{
    Camera cam;
    Sphere sphere;
    Cube cube;
    CrystalBox cbox;
    Ship player;
    ParticleEffect particleEffect;

    Quad background;

    float rotationAngle = 0.0f;
    float deltaTime = 0.0f;
    Vector3D skyBoxRotation;
    private NetworkThread network;


    @Override
    public void create() {
        network = new NetworkThread();
        network.start();

        Gdx.gl11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Set up the projection matrix.
        Gdx.gl11.glMatrixMode(GL11.GL_PROJECTION);
        Gdx.gl11.glLoadIdentity();
        Gdx.glu.gluPerspective(Gdx.gl11, 90, 1.333333f, 0.02f, 330.0f);

        Gdx.gl11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        cam = new Camera(new Point3D(1.0f, 1.0f, 1.0f), new Point3D(1.0f, 0.0f, 2.0f), new Vector3D(0.0f, -1.0f, 0.0f));

        sphere = new Sphere(50, 24);
        cube = new Cube("assets/textures/wood2.jpeg");
        this.cbox = new CrystalBox();
        this.particleEffect = new ParticleEffect();
        this.background = new Quad();
        this.skyBoxRotation = new Vector3D(0.0f,0.0f,0.0f);
        this.player = new Ship();
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void dispose() {        }

    @Override
    public void pause() { }


    private void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        this.deltaTime = deltaTime;
        rotationAngle += 10.0f * deltaTime;

        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            cam.pitch(-90.0f * deltaTime);
            skyBoxRotation.x +=90.0f * deltaTime;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            cam.pitch(90.0f * deltaTime);
            skyBoxRotation.x +=-90.0f * deltaTime;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            cam.yaw(-90.0f * deltaTime);
            skyBoxRotation.y +=90.0f * deltaTime;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            cam.yaw(90.0f * deltaTime);
            skyBoxRotation.y +=-90.0f * deltaTime;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W))
        {
            cam.slide(0.0f, 0.0f, -10.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            cam.slide(0.0f, 0.0f, 10.0f * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A))
        {
            cam.slide(-10.0f * deltaTime, 0.0f, 0.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            cam.slide(10.0f * deltaTime, 0.0f, 0.0f);
        }


        if(Gdx.input.isKeyPressed(Input.Keys.R))
        {
            cam.slide(0.0f, 10.0f * deltaTime, 0.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.F))
        {
            cam.slide(0.0f, -10.0f * deltaTime, 0.0f);
        }

        float x = Gdx.input.getDeltaX();
        x = Gdx.graphics.getWidth()/2-Gdx.input.getX();
        float y = Gdx.input.getDeltaY();
        y = Gdx.graphics.getHeight()/2-Gdx.input.getY();

        float sensitivity = 1.4f;

        cam.pitch(sensitivity*y * deltaTime);
        skyBoxRotation.x +=sensitivity*-y * deltaTime;

        cam.yaw(sensitivity*-x* deltaTime);
        skyBoxRotation.y +=sensitivity*-x * deltaTime;
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        //System.out.println("("+x+","+y+")");

        this.particleEffect.update(deltaTime);

        String message = String.format("move;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                Float.toString(cam.eye.x),
                Float.toString(cam.eye.y),
                Float.toString(cam.eye.z),
                Float.toString(cam.n.x),
                Float.toString(cam.n.y),
                Float.toString(cam.n.z),
                Float.toString(cam.u.x),
                Float.toString(cam.u.y),
                Float.toString(cam.u.z)
                );
        this.network.sendMessage(message);
    }

    private void drawFloor()
    {
        for(float fx = 0.0f; fx < 30.0f; fx += 1.0)
        {
            for(float fz = 0.0f; fz < 30.0f; fz += 1.0)
            {
                Gdx.gl11.glPushMatrix();
                Gdx.gl11.glTranslatef(fx, 0.0f, fz);
                //Gdx.gl11.glScalef(0.95f, 0.95f, 0.95f);
                cube.draw();
                Gdx.gl11.glPopMatrix();
            }
        }
    }

    private void display()
    {
        Gdx.gl11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        Gdx.gl11.glDisable(GL11.GL_LIGHTING);
        Gdx.gl11.glDisable(GL11.GL_DEPTH_TEST);
        Gdx.gl11.glMatrixMode(GL11.GL_PROJECTION);
        Gdx.gl11.glPushMatrix();
        //Gdx.gl11.glLoadIdentity();
        //Gdx.gl11.glOrthof(-1, 2, 0, 0, -1, 0);


        Gdx.gl11.glMatrixMode(GL11.GL_MODELVIEW);
        Gdx.gl11.glPushMatrix();
        Gdx.gl11.glLoadIdentity();


        Gdx.gl11.glDepthMask(false);
        Gdx.gl11.glRotatef(skyBoxRotation.x, 1.0f, 0.0f, 0.0f);
        System.out.println("("+cam.v.x+","+cam.v.y+","+cam.v.z+")");
        Gdx.gl11.glRotatef(-skyBoxRotation.y, 0.0f, 1.0f, 0.0f); //Hægri vinstri
        this.background.draw();

        Gdx.gl11.glDepthMask(true);

        // pop model view matrix
        Gdx.gl11.glPopMatrix();

        Gdx.gl11.glMatrixMode(GL11.GL_PROJECTION);

        // pop the projection matrix.
        Gdx.gl11.glPopMatrix();

        Gdx.gl11.glMatrixMode(GL11.GL_MODELVIEW);



        // Set the ModelView matrix with respect to the camera.
        cam.setModelViewMatrix();


        Gdx.gl11.glEnable(GL11.GL_LIGHTING);
        Gdx.gl11.glEnable(GL11.GL_LIGHT0);
        Gdx.gl11.glEnable(GL11.GL_LIGHT1);
        Gdx.gl11.glEnable(GL11.GL_DEPTH_TEST);


        Gdx.gl11.glPushMatrix();
        Gdx.gl11.glTranslatef(0f, 0.5f, 0f);
        this.particleEffect.display();

        Gdx.gl11.glPopMatrix();


        float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
        Gdx.gl11.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, lightDiffuse, 0);

        float[] lightPosition = {5.0f, 10.0f, 15.0f, 1.0f};
        Gdx.gl11.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition, 0);

        float[] lightDiffuse1 = {0.5f, 0.5f, 0.5f, 1.0f};
        Gdx.gl11.glLightfv(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, lightDiffuse1, 0);

        float[] lightPosition1 = {-5.0f, -10.0f, -15.0f, 1.0f};
        Gdx.gl11.glLightfv(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPosition1, 0);

        float[] materialDiffuse = {1.0f, 1.0f, 1.0f, 0.6f};
        Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_DIFFUSE, materialDiffuse, 0);

        drawFloor();

        Gdx.gl11.glPushMatrix();
        Gdx.gl11.glTranslatef(2.0f, 2.0f, 2.0f);
        //cube.draw();
        this.cbox.draw();
        Gdx.gl11.glPopMatrix();

        Gdx.gl11.glPushMatrix();
        Gdx.gl11.glTranslatef(5.0f, 7.0f, -10.0f);

        Gdx.gl11.glScalef(4, 4, 4);
        Gdx.gl11.glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f);
        sphere.draw();
        Gdx.gl11.glPopMatrix();


        Gdx.gl11.glPushMatrix();
        Point3D ppos = new Point3D(cam.eye.x,cam.eye.y,cam.eye.z);
        ppos.add(Vector3D.sum(Vector3D.mult(0.0f, cam.u), Vector3D.sum(Vector3D.mult(0.0f, cam.v), Vector3D.mult( -2.0f, cam.n))));
        Gdx.gl11.glTranslatef(ppos.x,ppos.y,ppos.z);
        Gdx.gl11.glRotatef(skyBoxRotation.y,0.0f,1.0f,0.0f);
        //Gdx.gl11.glRotatef(skyBoxRotation.x,0.0f,0.0f,1.0f);
        this.player.draw();
        Gdx.gl11.glPopMatrix();

        for(is.ru.tgra.network.Player p : GameState.instance().getPlayers()) {

            Gdx.gl11.glPushMatrix();
            Gdx.gl11.glTranslatef(p.pos.x,p.pos.y,p.pos.z);
            this.player.draw();
            Gdx.gl11.glPopMatrix();
        }
    }

    @Override
    public void render()
    {
        update();
        display();
    }

    @Override
    public void resize(int arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }
}
