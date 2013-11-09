package is.ru.tgra;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 * Created with IntelliJ IDEA.
 * User: Snorri
 * Date: 9.11.2013
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
public class DesktopStarter {
    public static void main(String[] args)
    {
        new LwjglApplication(new Core(), "daGame", 800, 600, false);
    }
}
