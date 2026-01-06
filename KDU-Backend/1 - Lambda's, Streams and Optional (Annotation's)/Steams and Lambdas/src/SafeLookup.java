import java.util.List;
import java.util.Optional;

public class SafeLookup {

    public static Object safeFind(String id) {
        return Optional.ofNullable(Inventory.findItem(id))
                .map(inv -> inv.getPalletItemIds())
                .orElseGet(() -> List.of());


    }

}
