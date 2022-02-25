package youyihj.herodotusutils.util;

import static org.reflections.ReflectionUtils.forName;

public class JavaUtil {

    public static boolean isClassExist(String className, ClassLoader classLoader) {
        try {
            forName(className, classLoader);
            return true;
        }
        catch (Throwable ex) {
            return false;
        }
    }

}