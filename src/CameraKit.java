public class CameraKit extends Equipment {
    private int lensCount;
    private boolean hasTripod;

    public CameraKit(String id, String name, double baseDailyPrice, int lensCount, boolean hasTripod) {
        super(id, name, baseDailyPrice);
        this.lensCount = lensCount;
        this.hasTripod = hasTripod;
    }

    @Override
    public double calculateDailyPrice() {
        double price = getBaseDailyPrice();
        price += (lensCount * 10.0);
        if (hasTripod) price += 15.0;
        return price;
    }

    @Override
    protected String getDetails() {
        return String.format("CameraKit (%d obiektyw(y), Statyw: %b)", lensCount, hasTripod);
    }
}