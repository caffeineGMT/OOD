import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrategyPattern {
    class ExternalRequest {

    }

    class Elevator {

        public void handleExternalRequest(ExternalRequest request) {
        }
    }

    interface HandleRequestStrategy {
        public void handleRequest(ExternalRequest request, List<Elevator> elevators);
    }

    class RandomHandleRequestStrategy implements HandleRequestStrategy {

        @Override
        public void handleRequest(ExternalRequest request, List<Elevator> elevators) {
            Random rand = new Random();
            int n = rand.nextInt(elevators.size());
            elevators.get(n).handleExternalRequest(request);
        }
    }

    class AlwaysOneElevatorHandleRequestStrategy implements HandleRequestStrategy {

        @Override
        public void handleRequest(ExternalRequest request, List<Elevator> elevators) {
            elevators.get(0).handleExternalRequest(request);
        }
    }

    class ElevatorSystem {
        private HandleRequestStrategy strategy;
        private List<Elevator> elevators;

        public ElevatorSystem() {
            elevators = new ArrayList<>();
            strategy = new AlwaysOneElevatorHandleRequestStrategy(); // default
        }

        public void setStrategy(HandleRequestStrategy strategy) {
            this.strategy = strategy;
        }

        public void handleRequest(ExternalRequest request) {
            strategy.handleRequest(request, elevators);
        }
    }
}
