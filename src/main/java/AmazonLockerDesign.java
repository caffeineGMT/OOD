import java.util.Date;
import java.util.Stack;

//Users should be able to use a code to open a locker and pick up a package
//Delivery guy should be able to find an "optimal" locker for a package

// ref: https://github.com/danielmiaomtm/Algorithm-OOD-java/blob/master/Amazon%20OOD%20locker.java
public class AmazonLockerDesign {
    public enum Size {
        SMALL, MIDDLE, LARGE
    }

    public class Package {
        public long packageId;
        private Size packageSize;
        public Package (Long packageId, Size packageSize) {
            this.packageId = packageId;
            this.packageSize = packageSize;
        }
        public Size getSize() {
            return packageSize;
        }
    }

    public class Locker {
        private int lockerId;
        private Size lockerSize;
        private boolean availability;
        public Locker (int lockerId, Size lockerSize) {
            this.lockerId = lockerId;
            this.lockerSize = lockerSize;
        }
        public void takeLocker() {
            markUnavailable();
        }

        public void clearLocker() {
            markAvailable();
        }

        private boolean getAvailability() {
            return availability;
        }

        private void markAvailable() {
            this.availability = true;
        }

        public void markUnavailable() {
            this.availability = false;
        }

        public Size getSize() {
            return lockerSize;
        }

        public boolean isFitIn (Package p) {
            if (lockerSize == Size.LARGE) {
                return true;
            } else if (lockerSize == Size.MIDDLE) {
                return p.getSize() == Size.MIDDLE || p.getSize() == Size.SMALL;
            } else {
                return p.getSize() == Size.SMALL;
            }
        }
    }

    class Receipt{
        private Package p;
        private Locker locker;
        private float startTime;

        public Receipt(Package p, Locker locker, long startTime) {
            this.p = p;
            this.locker = locker;
            this.startTime = startTime;
        }
    }

    public class LockerSystem {
        private float hourlyRate;
        Stack<Locker> availableSmall = new Stack<>();
        Stack<Locker> availableMiddle = new Stack<>();
        Stack<Locker> availableLarge = new Stack<>();

        public LockerSystem(int s, int m, int l, int hourlyRate) {
            int id = 0;
            for (int i = 0; i < s; i++) {
                availableSmall.push(new Locker(id++, Size.SMALL));
            }
            for (int i = 0; i < m; i++) {
                availableMiddle.push(new Locker(id++, Size.MIDDLE));
            }
            for (int i = 0; i < l; i++) {
                availableLarge.push(new Locker(id++, Size.LARGE));
            }
        }

        public boolean checkAvailability(Package p) {
            Size size = p.getSize();
            if (size == Size.LARGE) {
                return availableLarge.size() != 0;
            } else if (size == Size.MIDDLE) {
                return availableMiddle.size() != 0;
            } else {
                return availableSmall.size() != 0;
            }
        }

        public void removePackage(Receipt receipt) {
            receipt.locker.markAvailable();
            if (receipt.locker.getSize() == Size.LARGE) {
                availableLarge.push(new Locker(receipt.locker.lockerId, receipt.locker.lockerSize));
            } else if (receipt.locker.getSize() == Size.MIDDLE){
                availableMiddle.push(new Locker(receipt.locker.lockerId, receipt.locker.lockerSize));
            } else {
                availableSmall.push(new Locker(receipt.locker.lockerId, receipt.locker.lockerSize));
            }
        }

        public Receipt addPackage(Package p) throws LockerFullException{
            if (!checkAvailability(p)) {
                throw new LockerFullException("locker is full");
            }
            Locker locker = null;
            if (p.getSize() == Size.SMALL) {
                locker = availableSmall.pop();
            } else if (p.getSize() == Size.MIDDLE) {
                locker = availableMiddle.pop();
            } else {
                locker = availableLarge.pop();
            }
            Date date = new Date();
            Receipt receipt= new Receipt(p, locker, date.getTime());
            return receipt;
        }

        private float calcPrice(Receipt t) {
            Date curTime = new Date();
            return (curTime.getTime() - t.startTime) / (60 * 60 * 1000) * hourlyRate;
        }
    }

    class LockerFullException extends Exception {
        public LockerFullException(String s) {
            super(s);
        }
    }
}
