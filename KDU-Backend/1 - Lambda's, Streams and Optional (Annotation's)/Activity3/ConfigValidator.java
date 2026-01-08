
import java.lang.reflect.Field;

public class ConfigValidator {

    public static void validate(SystemConfig config) {

        Class<?> clazz = config.getClass();

        for (Field field : clazz.getDeclaredFields()) {

            if (field.isAnnotationPresent(RangeCheck.class)) {

                RangeCheck range = field.getAnnotation(RangeCheck.class);

                field.setAccessible(true);

                try {
                    int value = field.getInt(config);

                    if (value < range.min() || value > range.max()) {
                        throw new ConfigValidationException(
                                String.format(
                                        "Field '%s' failed validation: %d not in [%d, %d]",
                                        field.getName(),
                                        value,
                                        range.min(),
                                        range.max()
                                )
                        );
                    }

                    SystemConfig.logSuccess(
                            String.format(
                                    "%s is within range (%d–%d)",
                                    field.getName(),
                                    range.min(),
                                    range.max()
                            )
                    );

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
