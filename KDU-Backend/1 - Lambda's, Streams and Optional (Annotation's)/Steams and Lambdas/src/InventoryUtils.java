import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

public class InventoryUtils {

    public static Set<String> extractUniqueIds(Inventory inventory) {

        return inventory.getPalletItemIds()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }
}
