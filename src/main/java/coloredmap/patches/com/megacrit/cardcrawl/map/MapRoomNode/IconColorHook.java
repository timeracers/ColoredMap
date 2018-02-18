package coloredmap.patches.com.megacrit.cardcrawl.map.MapRoomNode;

import coloredmap.ColoredMap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

@SpirePatch(cls="com.megacrit.cardcrawl.map.MapRoomNode", method="render")
public class IconColorHook {
    @SuppressWarnings("unused")
    @SpireInsertPatch(loc=418, localvars={"room"})
    public static void Insert(Object meObj, Object sbObj, Object roomObj) {
        ColoredMap.setIconColor((AbstractRoom)roomObj, (SpriteBatch)sbObj);
    }
}
