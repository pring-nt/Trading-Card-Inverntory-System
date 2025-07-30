import com.System.Controller;
import com.System.EnhancedTCIS;
import com.System.View;

/**
 * Entry point for the Trading Card Inventory System application.
 * <p>
 * Initializes the model, view, and controller, then starts the main loop.
 */
public class Main {

    /**
     * Application launcher.
     * <p>
     * Creates the {@link EnhancedTCIS} model, the {@link View} for I/O,
     * and the {@link Controller} to drive the CLI, then invokes {@code run()}.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        EnhancedTCIS inventorySystem = new EnhancedTCIS();
        View view = new View();
        Controller controller = new Controller(view, inventorySystem);
        controller.run();
    }
}
