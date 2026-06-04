public class LaptopSet extends Equipment {
    private int ramGb;
    private boolean hasDockingStation;

    public LaptopSet(String id, String name, double baseDailyPrice, int ramGb, boolean hasDockingStation) {
        super(id, name, baseDailyPrice);
        this.ramGb = ramGb;
        this.hasDockingStation = hasDockingStation;
    }

    @Override
    public double calculateDailyPrice() {
        double price = getBaseDailyPrice();
        if (hasDockingStation) price += 15.0;
        if (ramGb >= 32) price += 25.0;
        return price;
    }

    @Override
    protected String getDetails() {
        return String.format("LaptopSet (%d GB RAM, Dok: %b)", ramGb, hasDockingStation);
    }
}