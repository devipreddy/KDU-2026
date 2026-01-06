
public class SystemConfig {

    @RangeCheck(min = 1, max = 16)
    private int maxThreads = 8;

    @RangeCheck(min = 100, max = 5000)
    private int timeoutSeconds = 2500;

    public SystemConfig(int maxThreads, int timeoutSeconds) {
        this.maxThreads = maxThreads;
        this.timeoutSeconds = timeoutSeconds;
    }

    public static void logSuccess(String message) {
        System.out.println("SUCCESS: " + message);
    }
}
