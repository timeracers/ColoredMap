package coloredmap.patches.com.megacrit.cardcrawl.map.MapRoomNode;

import coloredmap.ColoredMap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(cls="com.megacrit.cardcrawl.map.MapRoomNode", method="render")
public class IconOutlineHook {
    @SpireInsertPatch(locator = Locator.class, localvars={"room"})
    public static void Insert(Object meObj, Object sbObj, Object roomObj) {
        ColoredMap.setIconOutlineColor((AbstractRoom)roomObj, (SpriteBatch)sbObj);
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(
                "com.badlogic.gdx.graphics.g2d.SpriteBatch", "draw");
            return new int[] { LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher)[0] };
        }
    }
}
