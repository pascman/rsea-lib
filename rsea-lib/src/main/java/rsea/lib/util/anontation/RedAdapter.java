package rsea.lib.util.anontation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2015-10-08.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RedAdapter {
    String key();
    int layout();
    Class<? extends Object> holder();
//    Class<? extends Object> headerHolder() default RecyclerViewHolder.class;
//    Class<? extends Object> footerHolder() default RecyclerViewHolder.class;
    int[] headers() default {};
    int[] footer() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface RedAdapterBind {
        String key();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface RedAdapterBindHeader {
        String key();
    }

}
