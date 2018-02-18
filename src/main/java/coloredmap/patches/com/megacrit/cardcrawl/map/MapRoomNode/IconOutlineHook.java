package coloredmap.patches.com.megacrit.cardcrawl.map.MapRoomNode;

import coloredmap.ColoredMap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

@SpirePatch(cls="com.megacrit.cardcrawl.map.MapRoomNode", method="render")
public class IconOutlineHook {
    @SuppressWarnings("unused")
    @SpireInsertPatch(loc=394, localvars={"room"})
    public static void Insert(Object meObj, Object sbObj, Object roomObj) {
        ColoredMap.setIconOutlineColor((AbstractRoom)roomObj, (SpriteBatch)sbObj);
    }
}
