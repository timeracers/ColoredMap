package coloredmap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows this room type's colors to be modified in the config menu.
 * The class must have a default constructor, and must extend from AbstractRoom.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColoredRoom {
}
