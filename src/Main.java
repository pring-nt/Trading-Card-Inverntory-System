import com.System.Controller;
import com.System.InventorySystem;
import com.System.View;

public class Main {
    public static void main(String[] args) {
        InventorySystem inventorySystem = new InventorySystem();
        View view = new View();
        Controller controller = new Controller(view, inventorySystem);
        controller.run();
        // Start MCO2
    }
}
