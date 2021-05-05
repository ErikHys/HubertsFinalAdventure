import BridgeWorld.BridgeRun;
import TreasureWorld.TreasureHunt;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Hubert's final adventure";
        cfg.height = 480;
        cfg.width = 800;
        cfg.resizable = false;
        new LwjglApplication(new BridgeRun(), cfg);
    }
}