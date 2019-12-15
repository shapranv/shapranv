package shapranv.shell.utils.reflection;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ReflectionUtils {

    public static Object createInstance(String className) {
        try {
            Class<?> toInstantiate = Class.forName(className);
            return toInstantiate.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot instantiate class " + className, e);
        }
    }
}
