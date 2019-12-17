package shapranv.shell.utils;

import java.io.File;
import java.net.URL;

public class SystemUtils {
    public static String getSystemProperty(String name) {
        return System.getProperties().getProperty(name, System.getenv(name));
    }

    public static URL getResourceUrl(String name) throws Exception {
        URL url = SystemUtils.class.getClassLoader().getResource(name);

        if (url == null) {
            File file = new File(name);
            return file.exists() ? file.toURI().toURL() : null;
        } else {
            return url;
        }
    }
}
