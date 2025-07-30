import com.System.Controller;
import com.System.EnhancedTCIS;
import com.System.View;

public class Main {
    public static void main(String[] args) {
        EnhancedTCIS inventorySystem = new EnhancedTCIS();
        View view = new View();
        Controller controller = new Controller(view, inventorySystem);
        controller.run();
    }
}
