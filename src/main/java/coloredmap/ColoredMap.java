package coloredmap;

import basemod.*;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.rooms.*;
import org.scannotation.AnnotationDB;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpireInitializer
public class ColoredMap implements PostInitializeSubscriber {
    private static final String MODNAME = "Colored Map";
    private static final String AUTHOR = "timeracers, t-larson";
    private static final String DESCRIPTION = "v2.3";
    private static Prefs modPrefs;
    private static boolean hasSelection = false;
    private static ModColorDisplay selectedIcon;
    private static String selectedMapSymbol;
    private static List<Runnable> onSelectionChanged = new ArrayList<>();
    private static Field abstractRoomMapSymbolField;
    private static Field abstractRoomMapImgField;
    private static Field abstractRoomMapImgOutlineField;

    public ColoredMap() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new ColoredMap();
    }

    @Override
    public void receivePostInitialize() {
        modPrefs = SaveHelper.getPrefs("ColoredMapPrefs");

        System.out.println("Changing accessibility of AbstractRoom fields start");
        try {
            abstractRoomMapSymbolField = AbstractRoom.class.getDeclaredField("mapSymbol");
            abstractRoomMapSymbolField.setAccessible(true);
            abstractRoomMapImgField = AbstractRoom.class.getDeclaredField("mapImg");
            abstractRoomMapImgField.setAccessible(true);
            abstractRoomMapImgOutlineField = AbstractRoom.class.getDeclaredField("mapImgOutline");
            abstractRoomMapImgOutlineField.setAccessible(true);
        } catch (Exception ex) {
            System.out.println("Changing accessibility failed!");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        System.out.println("Changing accessibility of AbstractRoom fields end");

        List<ModColorDisplay> icons = new ArrayList<>();
        icons.add(createIcon(new MonsterRoom()));
        icons.add(createIcon(new MonsterRoomElite()));
        icons.add(createIcon(new RestRoom()));
        icons.add(createIcon(new ShopRoom()));
        icons.add(createIcon(new TreasureRoom()));
        icons.add(createIcon(new EventRoom()));

        System.out.println("Reflection to get ColoredRooms start");
        try {
            for (AnnotationDB db : Patcher.annotationDBMap.values()) {
                Set<String> roomNames = db.getAnnotationIndex().get(ColoredRoom.class.getName());
                if (roomNames != null) {
                    for (String roomName : roomNames) {
                        try {
                            AbstractRoom room = (AbstractRoom) Class.forName(roomName).getConstructor().newInstance();
                            if(isValidColoredRoom(room))
                                icons.add(createIcon(room));
                        } catch (NoSuchMethodException innerEx) {
                            System.out.println(roomName + " has no default constructor!");
                        } catch (ClassCastException innerEx) {
                            System.out.println(roomName + " does not extend AbstractRoom!");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Reflection Failed!");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        System.out.println("Reflection to get ColoredRooms End");

        ModPanel settingsPanel = new ModPanel();
        ModImage background = new ModImage(451.0f, 456.0f, "img/IconBackground.png");
        settingsPanel.addUIElement(background);
        addSliders(settingsPanel);
        Pagination pager = new Pagination(
            new ImageButton("img/tinyRightArrow.png", 915, 550, 100, 100, (b) -> {}),
            new ImageButton("img/tinyLeftArrow.png", 350, 550, 100, 100, (b) -> {}),
            2,3, 175, 175, icons);
        settingsPanel.addUIElement(pager);

        Texture badgeTexture = new Texture(Gdx.files.internal("img/ColoredMapBadge.png"));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
    }

    private static boolean isValidColoredRoom(AbstractRoom room) {
        boolean valid = true;
        if(findMapSymbol(room) == null)
        {
            System.out.println(room.getClass().getName() + " has no map symbol!");
            valid = false;
        }
        if(findMapImg(room) == null)
        {
            System.out.println(room.getClass().getName() + " has no map image!");
            valid = false;
        }
        if(findMapImgOutline(room) == null)
        {
            System.out.println(room.getClass().getName() + " has no map outline!");
            valid = false;
        }
        return valid;
    }

    private static ModColorDisplay createIcon(AbstractRoom room) {
        String symbol = findMapSymbol(room);
        ModColorDisplay icon = new ModColorDisplay(450.0f, 625.0f, findMapImg(room), findMapImgOutline(room),
            (me) -> changeSelection(me, symbol));
        icon.r = modPrefs.getFloat(symbol + "_red_icon", 0.0f);
        icon.g = modPrefs.getFloat(symbol + "_green_icon", 0.0f);
        icon.b = modPrefs.getFloat(symbol + "_blue_icon", 0.0f);
        icon.aOutline = modPrefs.getFloat(symbol + "_alpha_outline", 0.0f);
        icon.rOutline = modPrefs.getFloat(symbol + "_red_outline", 0.0f);
        icon.gOutline = modPrefs.getFloat(symbol + "_green_outline", 0.0f);
        icon.bOutline = modPrefs.getFloat(symbol + "_blue_outline", 0.0f);
        return icon;
    }

    private static String findMapSymbol(AbstractRoom room) {
        try {
            return room.getMapSymbol();
        } catch (Exception ex) {
            System.out.println("Warning: Failed to properly get map symbol of " + room.getClass().getName() + "!");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        try {
            return (String) abstractRoomMapSymbolField.get(room);
        } catch (IllegalAccessException ex) {
            System.out.println("Error: Failed to get map symbol of " + room.getClass().getName() + "!");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        //NullPointerException will occur if initializing
        return null;
    }

    private static Texture findMapImg(AbstractRoom room) {
        try {
            return room.getMapImg();
        } catch (Exception ex) {
            System.out.println("Warning: Failed to properly get map image of " + room.getClass().getName() + "!");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        try {
            return (Texture) abstractRoomMapImgField.get(room);
        } catch (IllegalAccessException ex) {
            System.out.println("Error: Failed to get map symbol of " + room.getClass().getName() + "!");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        //NullPointerException will occur if initializing
        return null;
    }

    private static Texture findMapImgOutline(AbstractRoom room) {
        try {
            return room.getMapImgOutline();
        } catch (Exception ex) {
            System.out.println("Warning: Failed to properly get map image outline of " + room.getClass().getName() + "!");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        try {
            return (Texture) abstractRoomMapImgOutlineField.get(room);
        } catch (IllegalAccessException ex) {
            System.out.println("Error: Failed to get map symbol of " + room.getClass().getName() + "!");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
        }
        //NullPointerException will occur if initializing
        return null;
    }

    private static void changeSelection(ModColorDisplay icon, String mapSymbol) {
        hasSelection = true;
        selectedIcon = icon;
        selectedMapSymbol = mapSymbol;
        for (Runnable action : onSelectionChanged)
            action.run();
    }

    private static void addSliders(ModPanel settingsPanel) {
        ModSlider redSlider = new ModSlider("Red", 1125.0f, 675.0f, 255.0f, "", settingsPanel, (me) -> {
            if(hasSelection) {
                selectedIcon.r = me.value;
                modPrefs.putFloat(selectedMapSymbol + "_red_icon", me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addUIElement(redSlider);

        ModSlider greenSlider = new ModSlider("Green", 1125.0f, 625.0f, 255.0f, "", settingsPanel, (me) -> {
            if(hasSelection) {
                selectedIcon.g = me.value;
                modPrefs.putFloat(selectedMapSymbol + "_green_icon", me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addUIElement(greenSlider);

        ModSlider blueSlider = new ModSlider("Blue", 1125.0f, 575.0f, 255.0f, "", settingsPanel, (me) -> {
            if(hasSelection) {
                selectedIcon.b = me.value;
                modPrefs.putFloat(selectedMapSymbol + "_blue_icon", me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addUIElement(blueSlider);

        ModSlider outlineAlphaSlider = new ModSlider("Outline Alpha", 1125.0f, 525.0f, 100.0f, "%", settingsPanel, (me) -> {
            if(hasSelection) {
                selectedIcon.aOutline = me.value;
                modPrefs.putFloat(selectedMapSymbol + "_alpha_outline", me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addUIElement(outlineAlphaSlider);

        ModSlider outlineRedSlider = new ModSlider("Outline Red", 1125.0f, 475.0f, 255.0f, "", settingsPanel, (me) -> {
            if(hasSelection) {
                selectedIcon.rOutline = me.value;
                modPrefs.putFloat(selectedMapSymbol + "_red_outline", me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addUIElement(outlineRedSlider);

        ModSlider outlineGreenSlider = new ModSlider("Outline Green", 1125.0f, 425.0f, 255.0f, "", settingsPanel, (me) -> {
            if(hasSelection) {
                selectedIcon.gOutline = me.value;
                modPrefs.putFloat(selectedMapSymbol + "_green_outline", me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addUIElement(outlineGreenSlider);

        ModSlider outlineBlueSlider = new ModSlider("Outline Blue", 1125.0f, 375.0f, 255.0f, "", settingsPanel, (me) -> {
            if(hasSelection) {
                selectedIcon.bOutline = me.value;
                modPrefs.putFloat(selectedMapSymbol + "_blue_outline", me.value);
                modPrefs.flush();
            }
        });
        settingsPanel.addUIElement(outlineBlueSlider);

        onSelectionChanged.add(() -> {
            redSlider.setValue(modPrefs.getFloat(selectedMapSymbol + "_red_icon", 0.0f));
            greenSlider.setValue(modPrefs.getFloat(selectedMapSymbol + "_green_icon", 0.0f));
            blueSlider.setValue(modPrefs.getFloat(selectedMapSymbol + "_blue_icon", 0.0f));
            outlineAlphaSlider.setValue(modPrefs.getFloat(selectedMapSymbol + "_alpha_outline", 0.0f));
            outlineRedSlider.setValue(modPrefs.getFloat(selectedMapSymbol + "_red_outline", 0.0f));
            outlineGreenSlider.setValue(modPrefs.getFloat(selectedMapSymbol + "_green_outline", 0.0f));
            outlineBlueSlider.setValue(modPrefs.getFloat(selectedMapSymbol + "_blue_outline", 0.0f));
        });
    }

    public static void setIconOutlineColor(AbstractRoom room, SpriteBatch sb) {
        String symbol = findMapSymbol(room);
        if(symbol != null)
            sb.setColor(new Color(
                modPrefs.getFloat(symbol + "_red_outline", 0.0f),
                modPrefs.getFloat(symbol + "_green_outline", 0.0f),
                modPrefs.getFloat(symbol + "_blue_outline", 0.0f),
                modPrefs.getFloat(symbol + "_alpha_outline", 0.0f)));
    }

    public static void setIconColor(AbstractRoom room, SpriteBatch sb) {
        String symbol = findMapSymbol(room);
        if(symbol != null)
            sb.setColor(new Color(
                modPrefs.getFloat(symbol + "_red_icon", 0.0f),
                modPrefs.getFloat(symbol + "_green_icon", 0.0f),
                modPrefs.getFloat(symbol + "_blue_icon", 0.0f), 1.0f));
    }
}
