package shapranv.shell.utils;

public class MathUtils {
    public static int getPercentage(int value, int total) {
        return (int) (((double) value / total) * 100);
    }
}
