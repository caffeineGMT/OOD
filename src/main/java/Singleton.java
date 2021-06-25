import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Singleton {
    public static class ParkingLot {
        private static ParkingLot _instance = null;
        private List<Integer> levels;
        private ParkingLot() {
            levels = new ArrayList<>();
        }

        // non-thread safe
        public static ParkingLot getInstance() {
            if (_instance == null) {
                _instance = new ParkingLot();
            }
            return _instance;
        }

        // thread safe
//        public static synchronized ParkingLot getInstance() {
//            if (_instance == null) {
//                _instance = new ParkingLot();
//            }
//            return _instance;
//        }
    }
}
