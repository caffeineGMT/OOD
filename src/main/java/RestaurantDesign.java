import java.util.*;

public class RestaurantDesign {
    class Date {
        long time;

        public long getTime() {
            return time;
        }
    }
    class NoTableException extends Exception {
        public NoTableException(Party p) {
            super("No table available for party size: " + p.getSize());
        }
    }

    class Meal {
        private float price;

        public Meal(float price) {
            this.price = price;
        }

        public float getPrice() {
            return this.price;
        }
    }

    class Order {
        private List<Meal> meals;
        private Table table;
        private Party p;

        public Order(List<Meal> meals, Table table, Party p) {
            this.meals = meals;
            this.table = table;
            this.p = p;
        }

        public List<Meal> getMeals() {
            return meals;
        }

        public void mergeOrder(Order order) {
            if (order != null) {
                for (Meal meal : order.getMeals()) {
                    meals.add(meal);
                }
            }
        }

        public float getPrice() {
            int bill = 0;
            for (Meal meal : meals) {
                bill += meal.getPrice();
            }
            return bill;
        }
    }

    class Party {
        private int size;

        public Party(int size) {
            this.size = size;
        }

        public int getSize() {
            return this.size;
        }
    }

    class Table implements Comparable<Table> {
        private int id;
        private int capacity;
        private boolean available;
        private Order order;
        List<Date> reservations;

        public Table(int id, int capacity) {
            this.id = id;
            this.capacity = capacity;
            available = true;
            order = null;
            reservations = new ArrayList<>();
        }

        public int getId() {
            return this.id;
        }

        public int getCapacity() {
            return this.capacity;
        }

        public List<Date> getReservation() {
            return reservations;
        }

        public boolean isAvailable() {
            return this.available;
        }

        public void markAvailable() {
            this.available = true;
        }

        public void markUnavailable() {
            this.available = false;
        }

        public Order getCurrentOrder() {
            return this.order;
        }

        public void setOrder(Order o) {
            if (order == null) {
                this.order = o;
            } else {
                if (o != null) {
                    this.order.mergeOrder(o);
                }
            }
        }

        @Override
        public int compareTo(Table compareTable) {
            return this.capacity - compareTable.getCapacity();
        }

        // find first item that is larger than target
        private int findDatePosition(Date target) {
            int len = reservations.size();
            if (len == 0)
                return 0;
            if (target.getTime() > reservations.get(len - 1).getTime()) {
                return len;
            }

            int l = 0;
            int r = len - 1;

            while (l + 1 < r) {
                int mid = (l + r) / 2;
                if (reservations.get(mid).getTime() <= target.getTime()) {
                    l = mid;
                } else {
                    r = mid;
                }
            }

            if (reservations.get(l).getTime() > target.getTime()) {
                return l;
            }
            if (reservations.get(r).getTime() > target.getTime()) {
                return r;
            }

            return len;
        }

        public boolean noFollowReservation(Date d) {
            final int MILLI_TO_HOUR = 1000 * 60 * 60;
            int position = findDatePosition(d);

            if (position < reservations.size()) {
                Date nextReservation = reservations.get(position);
                int diff = (int) ((nextReservation.getTime() - d.getTime()) / MILLI_TO_HOUR);
                if (diff < Restaurant.MAX_DINEHOUR) {
                    return false;
                }
            }
            return true;
        }

        public boolean reserveForDate(Date d) {
            final int MILLI_TO_HOUR = 1000 * 60 * 60;
            int position = findDatePosition(d);
            int before = position - 1;
            int after = position;

            if (before >= 0) {
                Date previousReservation = reservations.get(before);
                int diff = (int) ((d.getTime() - previousReservation.getTime()) / MILLI_TO_HOUR);
                if (diff < Restaurant.MAX_DINEHOUR) {
                    return false;
                }
            }

            if (after < reservations.size()) {
                Date nextReservation = reservations.get(after);
                int diff = (int) ((nextReservation.getTime() - d.getTime()) / MILLI_TO_HOUR);
                if (diff < Restaurant.MAX_DINEHOUR) {
                    return false;
                }
            }

            reservations.add(position, d);
            return true;
        }

        public void removeReservation(Date d) {
            reservations.remove(d);
        }
    }

    class Reservation {
        private Table table;
        private Date date;

        public Reservation(Table table, Date date) {
            this.table = table;
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        public Table getTable() {
            return table;
        }
    }

    public class Restaurant {
        private List<Table> tables;
        private List<Meal> menu;
        public static final int MAX_DINEHOUR = 2;
        public static final long HOUR = 3600 * 1000;

        public Restaurant() {
            tables = new ArrayList<Table>();
            menu = new ArrayList<Meal>();
        }

        public void findTable(Party p) throws NoTableException {
            Date currentDate = new Date();
            for (Table t : tables) {
                if (t.isAvailable() && t.getCapacity() >= p.getSize() && t.noFollowReservation(currentDate)) {
                    t.markUnavailable();
                    return;
                }
            }
            throw new NoTableException(p);
        }

        public void takeOrder(Order o) {
        }

        public float checkOut(Order order) {
            order.table.markAvailable();
            return order.getPrice();
        }

        public List<Meal> getMenu() {
            return menu;
        }

        public void addTable(Table t) {
            tables.add(t);
            Collections.sort(tables);
        }

        public Reservation findTableForReservation(Party p, Date date) {
            for (Table table : tables) {
                if (table.getCapacity() >= p.getSize() && table.reserveForDate(date)) {
                    return new Reservation(table, date);
                }
            }
            return null;
        }

        public void cancelReservation(Reservation r) {
            Date date = r.getDate();
            r.getTable().removeReservation(date);
        }
    }
}
