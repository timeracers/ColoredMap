package coloredmap;

import basemod.BaseMod;
import basemod.ModColorDisplay;
import basemod.ModImage;
import basemod.ModPanel;
import basemod.ModSlider;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.rooms.*;

public class ColoredMap implements PostInitializeSubscriber {
    private static final String MODNAME = "Colored Map";
    private static final String AUTHOR = "t-larson";
    private static final String DESCRIPTION = "v1.1.0";
    
    public ColoredMap() {
        BaseMod.subscribeToPostInitialize(this);
    }
    
    public static void initialize() {
        ColoredMap cm = new ColoredMap();
    }
    
    public void receivePostInitialize() {
        Prefs modPrefs = SaveHelper.getPrefs("ColoredMapPrefs");
        
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.state.put("selection", -1);
        
        //ModLabel instructions = 
        ModImage background = new ModImage(451.0f, 456.0f, "img/IconBackground.png");
        settingsPanel.addImage(background);
        
        ModColorDisplay[] icons = new ModColorDisplay[6];
        ModSlider redSlider = new ModSlider("R", 350.0f, 700.0f, 255.0f, "", settingsPanel, (me) -> {
            int selection = settingsPanel.state.get("selection");
            if (selection != -1) {
                icons[selection].r = me.value;
                modPrefs.putFloat("r_icon_" + selection, me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addSlider(redSlider);
        
        ModSlider greenSlider = new ModSlider("G", 350.0f, 650.0f, 255.0f, "", settingsPanel, (me) -> {
            int selection = settingsPanel.state.get("selection");
            if (selection != -1) {
                icons[selection].g = me.value;
                modPrefs.putFloat("g_icon_" + selection, me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addSlider(redSlider);
        
        ModSlider blueSlider = new ModSlider("B", 350.0f, 600.0f, 255.0f, "", settingsPanel, (me) -> {
            int selection = settingsPanel.state.get("selection");
            if (selection != -1) {
                icons[selection].b = me.value;
                modPrefs.putFloat("b_icon_" + selection, me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addSlider(greenSlider);
        
        ModSlider outlineSlider = new ModSlider("Outline", 350.0f, 550.0f, 100.0f, "%", settingsPanel, (me) -> {
            int selection = settingsPanel.state.get("selection");
            if (selection != -1) {
                icons[selection].aOutline = me.value;
                modPrefs.putFloat("a_icon_" + selection, me.value);
                modPrefs.flush();
            } 
        });
        settingsPanel.addSlider(blueSlider);
        
        icons[0] = new ModColorDisplay(800.0f, 625.0f, ImageMaster.MAP_NODE_ENEMY, ImageMaster.MAP_NODE_ENEMY_OUTLINE, (me) -> {
            settingsPanel.state.put("selection", 0);
            redSlider.setValue(modPrefs.getFloat("r_icon_0", 1.0f));
            greenSlider.setValue(modPrefs.getFloat("g_icon_0", 1.0f));
            blueSlider.setValue(modPrefs.getFloat("b_icon_0", 1.0f));
            outlineSlider.setValue(modPrefs.getFloat("a_icon_0", 1.0f));
        });
        settingsPanel.addSlider(outlineSlider);
        
        icons[1] = new ModColorDisplay(625.0f, 625.0f, ImageMaster.MAP_NODE_ELITE, ImageMaster.MAP_NODE_ELITE_OUTLINE, (me) -> {
            settingsPanel.state.put("selection", 1);
            redSlider.setValue(modPrefs.getFloat("r_icon_1", 1.0f));
            greenSlider.setValue(modPrefs.getFloat("g_icon_1", 1.0f));
            blueSlider.setValue(modPrefs.getFloat("b_icon_1", 1.0f));
            outlineSlider.setValue(modPrefs.getFloat("a_icon_1", 1.0f));
        });
        
        icons[2] = new ModColorDisplay(450.0f, 625.0f, ImageMaster.MAP_NODE_MERCHANT, ImageMaster.MAP_NODE_MERCHANT_OUTLINE, (me) -> {
            settingsPanel.state.put("selection", 2);
            redSlider.setValue(modPrefs.getFloat("r_icon_2", 1.0f));
            greenSlider.setValue(modPrefs.getFloat("g_icon_2", 1.0f));
            blueSlider.setValue(modPrefs.getFloat("b_icon_2", 1.0f));
            outlineSlider.setValue(modPrefs.getFloat("a_icon_2", 1.0f));
        });
        
        icons[3] = new ModColorDisplay(800.0f, 450.0f, ImageMaster.MAP_NODE_REST, ImageMaster.MAP_NODE_REST_OUTLINE, (me) -> {
            settingsPanel.state.put("selection", 3);
            redSlider.setValue(modPrefs.getFloat("r_icon_3", 1.0f));
            greenSlider.setValue(modPrefs.getFloat("g_icon_3", 1.0f));
            blueSlider.setValue(modPrefs.getFloat("b_icon_3", 1.0f));
            outlineSlider.setValue(modPrefs.getFloat("a_icon_3", 1.0f));
        });
        
        icons[4] = new ModColorDisplay(625.0f, 450.0f, ImageMaster.MAP_NODE_TREASURE, ImageMaster.MAP_NODE_TREASURE_OUTLINE, (me) -> {
            settingsPanel.state.put("selection", 4);
            redSlider.setValue(modPrefs.getFloat("r_icon_4", 1.0f));
            greenSlider.setValue(modPrefs.getFloat("g_icon_4", 1.0f));
            blueSlider.setValue(modPrefs.getFloat("b_icon_4", 1.0f));
            outlineSlider.setValue(modPrefs.getFloat("a_icon_4", 1.0f));
        });
        
        icons[5] = new ModColorDisplay(450.0f, 450.0f, ImageMaster.MAP_NODE_EVENT, ImageMaster.MAP_NODE_EVENT_OUTLINE, (me) -> {
            settingsPanel.state.put("selection", 5);
            redSlider.setValue(modPrefs.getFloat("r_icon_5", 1.0f));
            greenSlider.setValue(modPrefs.getFloat("g_icon_5", 1.0f));
            blueSlider.setValue(modPrefs.getFloat("b_icon_5", 1.0f));
            outlineSlider.setValue(modPrefs.getFloat("a_icon_5", 1.0f));
        });

        for (int i = 0; i < icons.length; i++) {
            icons[i].r = modPrefs.getFloat("r_icon_" + i, 1.0f);
            icons[i].g = modPrefs.getFloat("g_icon_" + i, 1.0f);
            icons[i].b = modPrefs.getFloat("b_icon_" + i, 1.0f);
            icons[i].aOutline = modPrefs.getFloat("a_icon_" + i, 1.0f);
            settingsPanel.addColorDisplay(icons[i]);
        }
        
        Texture badgeTexture = new Texture(Gdx.files.internal("img/ColoredMapBadge.png"));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
    }
    
    public static void setIconOutlineColor(AbstractRoom room, SpriteBatch sb) {
        Prefs modPrefs = SaveHelper.getPrefs("ColoredMapPrefs");
        float a = 0.0f;
        
        if (room instanceof MonsterRoomElite) {
            a = modPrefs.getFloat("a_icon_1", 1.0f);
        } else if (room instanceof MonsterRoom) {
            a = modPrefs.getFloat("a_icon_0", 1.0f);
        } else if (room instanceof ShopRoom) {
            a = modPrefs.getFloat("a_icon_2", 1.0f);
        } else if (room instanceof RestRoom) {
            a = modPrefs.getFloat("a_icon_3", 1.0f);
        } else if (room instanceof TreasureRoom) {
            a = modPrefs.getFloat("a_icon_4", 1.0f);
        } else if (room instanceof EventRoom) {
            a = modPrefs.getFloat("a_icon_5", 1.0f);
        } 
        
        sb.setColor(new Color(0.0f, 0.0f, 0.0f, a));
    }
    
    public static void setIconColor(AbstractRoom room, SpriteBatch sb) {
        Prefs modPrefs = SaveHelper.getPrefs("ColoredMapPrefs");
        float r = 0.0f;
        float g = 0.0f;
        float b = 0.0f;
        
        if (room instanceof MonsterRoomElite) {
            r = modPrefs.getFloat("r_icon_1", 1.0f);
            g = modPrefs.getFloat("g_icon_1", 1.0f);
            b = modPrefs.getFloat("b_icon_1", 1.0f);
        } else if (room instanceof MonsterRoom) {
            r = modPrefs.getFloat("r_icon_0", 1.0f);
            g = modPrefs.getFloat("g_icon_0", 1.0f);
            b = modPrefs.getFloat("b_icon_0", 1.0f);
        } else if (room instanceof ShopRoom) {
            r = modPrefs.getFloat("r_icon_2", 1.0f);
            g = modPrefs.getFloat("g_icon_2", 1.0f);
            b = modPrefs.getFloat("b_icon_2", 1.0f);
        } else if (room instanceof RestRoom) {
            r = modPrefs.getFloat("r_icon_3", 1.0f);
            g = modPrefs.getFloat("g_icon_3", 1.0f);
            b = modPrefs.getFloat("b_icon_3", 1.0f);
        } else if (room instanceof TreasureRoom) {
            r = modPrefs.getFloat("r_icon_4", 1.0f);
            g = modPrefs.getFloat("g_icon_4", 1.0f);
            b = modPrefs.getFloat("b_icon_4", 1.0f);
        } else if (room instanceof EventRoom) {
            r = modPrefs.getFloat("r_icon_5", 1.0f);
            g = modPrefs.getFloat("g_icon_5", 1.0f);
            b = modPrefs.getFloat("b_icon_5", 1.0f);
        }
        
        sb.setColor(new Color(r, g, b, 1.0f));
    }
}