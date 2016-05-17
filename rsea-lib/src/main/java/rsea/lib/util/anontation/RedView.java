package rsea.lib.util.anontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2015-10-01.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RedView {
    int value();
    String type() default "";
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @SuppressWarnings(value = "unused")
    public static @interface Click {
        int[] value();
    }
}