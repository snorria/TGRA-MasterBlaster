package is.ru.tgra;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.utils.BufferUtils;
import is.ru.tgra.network.GameState;
import is.ru.tgra.network.NetworkThread;
import is.ru.tgra.network.Player;
import is.ru.tgra.server.ClientThread;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

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
    ShipModel player;
    List<Shot> shots = new ArrayList<Shot>();
    Point3D playerPos;
    ParticleEffect particleEffect;
    String shotMode = "double";
    boolean altShot = true;
    private boolean isButtonPressedRight = false;
    Music music;

    Quad background;

    float rotationAngle = 0.0f, playerSpeed = 30.0f;
    float deltaTime = 0.0f;
    Vector3D skyBoxRotation;
    private NetworkThread network;
    private UI ui;
    boolean outOfBounds = false, wasDead = false;
    private float gunCooldown;
    private float GUNCD = 0.3f;
    private float deathCooldown;
    private float DEATHTIMER = 4.0f;


    @Override
    public void create() {
        network = new NetworkThread();
        network.start();

        Gdx.gl11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Set up the projection matrix.
        Gdx.gl11.glMatrixMode(GL11.GL_PROJECTION);
        Gdx.gl11.glLoadIdentity();
        Gdx.glu.gluPerspective(Gdx.gl11, 90, 1.333333f, 0.02f, 1000.0f);

        Gdx.gl11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        cam = new Camera(new Point3D(1.0f, 1.0f, 1.0f), new Point3D(1.0f, 1.0f, 0.0f), new Vector3D(0.0f, 1.0f, 0.0f));

        sphere = new Sphere(50, 24);
        cube = new Cube("assets/textures/wood2.jpeg");
        this.cbox = new CrystalBox();
        this.particleEffect = new ParticleEffect();
        this.background = new Quad();
        this.skyBoxRotation = new Vector3D(0.0f,0.0f,0.0f);
        this.player = new ShipModel();
        this.ui = new UI();
        Gdx.input.setCursorCatched(true);

        music = Gdx.audio.newMusic(Gdx.files.internal("Star Wars Rogue Squadron Soundtrack - Rogue Theme- Reprise.mp3"));
        music.setLooping(true);
        music.play();
    }

    @Override
    public void dispose() {        }

    @Override
    public void pause() { }


    private void update() {
        if(GameState.instance().amIDead()){
            this.deathCooldown = DEATHTIMER;
            wasDead = true;
        }
        else if(wasDead && this.deathCooldown <= 0){
            String message = "alive";
            this.network.sendMessage(message);
            wasDead = false;
        }

        float deltaTime = Gdx.graphics.getDeltaTime();

        this.deltaTime = deltaTime;
        rotationAngle += 10.0f * deltaTime;
        if(!outOfBounds){
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

            /*
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
            */

            float x = Gdx.input.getDeltaX();
            x = Gdx.graphics.getWidth()/2-Gdx.input.getX();
            float y = Gdx.input.getDeltaY();
            y = Gdx.graphics.getHeight()/2-Gdx.input.getY();

            float sensitivity = 1.4f;

            cam.pitch(sensitivity*y * deltaTime);
            skyBoxRotation.x +=sensitivity*-y * deltaTime;

            cam.yaw(sensitivity*-x* deltaTime);
            skyBoxRotation.y +=sensitivity*-x * deltaTime;
        }
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        //System.out.println("("+x+","+y+")");

        this.particleEffect.update(deltaTime);

        cam.slide(0.0f, 0.0f, -playerSpeed * deltaTime);

        playerPos = new Point3D(cam.eye.x,cam.eye.y,cam.eye.z);
        playerPos.add(Vector3D.sum(Vector3D.mult(0.0f, cam.u), Vector3D.sum(Vector3D.mult(-0.5f, cam.v), Vector3D.mult(0.0f, cam.n))));

        if(playerPos.x < -200f || playerPos.x > 200f || playerPos.y < -200f || playerPos.y > 200f || playerPos.z < -200f || playerPos.z > 200f){
            cam.pitch(90f*deltaTime);
            outOfBounds = true;
        }
        else
        {
            outOfBounds = false;
        }
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            if(!isButtonPressedRight)
            {
                if(shotMode == "double"){
                    shotMode = "alt";
                } else if(shotMode == "alt"){
                    shotMode = "double";
                }
                isButtonPressedRight = true;
            }
        } else {
            isButtonPressedRight = false;
        }
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && gunCooldown <= 0.0f && this.deathCooldown <= 0){
            //ShootLazer
            Point3D shotEnd = new Point3D(cam.eye.x,cam.eye.y,cam.eye.z);
            shotEnd.add(Vector3D.sum(Vector3D.mult(0.0f, cam.u), Vector3D.sum(Vector3D.mult(0.0f, cam.v), Vector3D.mult(-20000.0f, cam.n))));


            Point3D leftShot = new Point3D(playerPos.x,playerPos.y,playerPos.z);
            Point3D rightShot = new Point3D(playerPos.x,playerPos.y,playerPos.z);

            leftShot.add(Vector3D.sum(Vector3D.mult(-1.0f, cam.u), Vector3D.sum(Vector3D.mult(0.0f, cam.v), Vector3D.mult( 0f, cam.n))));
            rightShot.add(Vector3D.sum(Vector3D.mult(1.0f, cam.u), Vector3D.sum(Vector3D.mult(0.0f, cam.v), Vector3D.mult( 0f, cam.n))));


            if(shotMode == "double"){
                shots.add(new Shot(leftShot,shotEnd));
                shots.add(new Shot(rightShot,shotEnd));

                String message1 = String.format("fire;%s;%s;%s;%s;%s;%s",
                        Float.toString(leftShot.x),
                        Float.toString(leftShot.y),
                        Float.toString(leftShot.z),
                        Float.toString(shotEnd.x),
                        Float.toString(shotEnd.y),
                        Float.toString(shotEnd.z)
                );
                String message2 = String.format("fire;%s;%s;%s;%s;%s;%s",
                        Float.toString(rightShot.x),
                        Float.toString(rightShot.y),
                        Float.toString(rightShot.z),
                        Float.toString(shotEnd.x),
                        Float.toString(shotEnd.y),
                        Float.toString(shotEnd.z)
                );

                this.network.sendMessage(message1);
                this.network.sendMessage(message2);
                gunCooldown = GUNCD;

                for(Player p : GameState.instance().getPlayers()){
                    if(Point3D.LengthLine(p.pos,leftShot,shotEnd) < 1f || Point3D.LengthLine(p.pos,rightShot,shotEnd) < 1f){
                        System.out.println("I GOT SHOT CAPTAIN, "+p.name);
                        String message = String.format("killed;%s",p.name);
                        this.network.sendMessage(message);
                    }
                }
            } else if(shotMode == "alt"){
                if(altShot){ //vinstri
                    shots.add(new Shot(leftShot,shotEnd));
                    String message1 = String.format("fire;%s;%s;%s;%s;%s;%s",
                            Float.toString(leftShot.x),
                            Float.toString(leftShot.y),
                            Float.toString(leftShot.z),
                            Float.toString(shotEnd.x),
                            Float.toString(shotEnd.y),
                            Float.toString(shotEnd.z)
                    );
                    this.network.sendMessage(message1);
                    for(Player p : GameState.instance().getPlayers()){
                        if(Point3D.LengthLine(p.pos,leftShot,shotEnd) < 1f){
                            System.out.println("I GOT SHOT CAPTAIN, "+p.name);
                            String message = String.format("killed;%s",p.name);
                            this.network.sendMessage(message);
                        }
                    }
                } else //haegri
                {
                    shots.add(new Shot(rightShot,shotEnd));
                    String message1 = String.format("fire;%s;%s;%s;%s;%s;%s",
                            Float.toString(rightShot.x),
                            Float.toString(rightShot.y),
                            Float.toString(rightShot.z),
                            Float.toString(shotEnd.x),
                            Float.toString(shotEnd.y),
                            Float.toString(shotEnd.z)
                    );
                    this.network.sendMessage(message1);
                    for(Player p : GameState.instance().getPlayers()){
                        if(Point3D.LengthLine(p.pos,rightShot,shotEnd) < 1f){
                            System.out.println("I GOT SHOT CAPTAIN, "+p.name);
                            String message = String.format("killed;%s",p.name);
                            this.network.sendMessage(message);
                        }
                    }
                }
                gunCooldown = GUNCD/2.0f;
                altShot = !altShot;
            }
        }
        if(deathCooldown < 0){
            for(Player p : GameState.instance().getPlayers()){
                if(!p.isDead){
                    if(Vector3D.difference(p.pos,playerPos).length()<1.5f){
                        String message = String.format("killed;%s",p.name);
                        this.network.sendMessage(message);
                        String message2 = String.format("killed;%s",GameState.instance().clientNickName);
                        this.network.sendMessage(message2);
                        GameState.instance().setDead(p.name);
                        GameState.instance().setDead();
                    }
                }
            }
        }
        gunCooldown-=deltaTime;
        deathCooldown-=deltaTime;
        this.ui.update(deathCooldown);
        cam.slide(0.0f, 0.0f, -playerSpeed * deltaTime);

        String message = String.format("move;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                Float.toString(playerPos.x),
                Float.toString(playerPos.y),
                Float.toString(playerPos.z),
                Float.toString(cam.n.x),
                Float.toString(cam.n.y),
                Float.toString(cam.n.z),
                Float.toString(cam.u.x),
                Float.toString(cam.u.y),
                Float.toString(cam.u.z)
                );
        this.network.sendMessage(message);

        //shots
        Shot tempShot;
        for(int i = 0;i<shots.size();i++){
            tempShot = shots.get(i);
            if(tempShot.update(deltaTime))
            {
                shots.remove(i);
                i--;
            }
        }
        for(Shot s : GameState.instance().getShots())
        {
            shots.add(s);
        }
    }


    private void display()
    {
        Gdx.gl11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        Gdx.gl11.glDisable(GL11.GL_LIGHTING);


        // Set the ModelView matrix with respect to the camera.
        cam.setModelViewMatrix();

        Gdx.gl11.glPushMatrix();
        Gdx.gl11.glTranslatef(0f, 0f, 0f);

        Gdx.gl11.glScalef(750, 750, 750);
        this.background.draw();

        Gdx.gl11.glPopMatrix();

        Gdx.gl11.glEnable(GL11.GL_LIGHTING);
        Gdx.gl11.glEnable(GL11.GL_LIGHT0);
        Gdx.gl11.glEnable(GL11.GL_LIGHT1);
        Gdx.gl11.glEnable(GL11.GL_DEPTH_TEST);






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

        for(is.ru.tgra.network.Player p : GameState.instance().getPlayers()) {
            Gdx.gl11.glPushMatrix();

            //p.forward.
            Gdx.gl11.glTranslatef(p.pos.x,p.pos.y,p.pos.z);
            //Gdx.gl11.glRotatef((float) Vector3D.angle(new Vector3D(-1f,0f,0f),p.left),0f,1f,0f);
            Float d1;
            if(Vector3D.cross(new Vector3D(-1f,0f,0f),p.left).y<0)
                d1 = (float) -Vector3D.angle(new Vector3D(-1f,0f,0f),p.left);
            else
                d1 = (float) Vector3D.angle(new Vector3D(-1f,0f,0f),p.left);
            Gdx.gl11.glRotatef(d1,0f,1f,0f);


            //System.out.println("d1: "+d1);

            Float d2;
            if(Vector3D.cross(new Vector3D(0f,0f,-1f),p.forward).x<0)
                d2 = (float) -Vector3D.angle(new Vector3D(0f,0f,-1f),p.forward);
            else
                d2 = (float) Vector3D.angle(new Vector3D(0f,0f,-1f),p.forward);
            //System.out.println("d2: "+d2);

            //Gdx.gl11.glRotatef(d2,1f,0f,0f);

            Float d3;

            if(Vector3D.cross(new Vector3D(-1f,0f,0f),p.left).z<0)
                d3 = (float) -Vector3D.angle(new Vector3D(-1f,0f,0f),p.left);
            else
                d3 = (float) Vector3D.angle(new Vector3D(-1f,0f,0f),p.left);

            //System.out.println("d3: "+d3);
            //Gdx.gl11.glRotatef(d3,0f,0f,1f);



            //System.out.println(Math.atan2(p.left.y,p.left.x+1)*180/Math.PI);


            this.player.draw();
            Gdx.gl11.glPopMatrix();
        }
        //shots
        for(Shot s: shots)
        {
            s.draw();
        }

        //UI
        Gdx.gl11.glDisable(GL11.GL_LIGHTING);
        Gdx.gl11.glDisable(GL11.GL_DEPTH_TEST);
        Gdx.gl11.glMatrixMode(GL11.GL_PROJECTION);
        Gdx.gl11.glPushMatrix();
        Gdx.gl11.glLoadIdentity();
        Gdx.gl11.glOrthof(-1, 2, 0, 0, -1, 0);


        Gdx.gl11.glMatrixMode(GL11.GL_MODELVIEW);
        Gdx.gl11.glPushMatrix();
        Gdx.gl11.glLoadIdentity();


        Gdx.gl11.glDepthMask(false);
        this.ui.draw();
        Gdx.gl11.glDepthMask(true);

        // pop model view matrix
        Gdx.gl11.glPopMatrix();

        Gdx.gl11.glMatrixMode(GL11.GL_PROJECTION);

        // pop the projection matrix.
        Gdx.gl11.glPopMatrix();

        Gdx.gl11.glMatrixMode(GL11.GL_MODELVIEW);
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
