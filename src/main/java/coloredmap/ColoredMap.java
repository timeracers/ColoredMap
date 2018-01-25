package coloredmap;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ColoredMap implements PostInitializeSubscriber {
    private static final String MODNAME = "Colored Map";
    private static final String AUTHOR = "t-larson";
    private static final String DESCRIPTION = "v1.0.0";
    
    public ColoredMap() {
        BaseMod.subscribeToPostInitialize(this);
    }
    
    public static void initialize() {
        ColoredMap cm = new ColoredMap();
    }
    
    public void receivePostInitialize() {
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addLabel("This mod does not have any options", 400.0f, 700.0f, (me) -> {});
        
        Texture badgeTexture = new Texture(Gdx.files.internal("img/ColoredMapBadge.png"));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
    }
}