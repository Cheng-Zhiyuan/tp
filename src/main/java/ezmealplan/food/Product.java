package ezmealplan.food;

import ezmealplan.exceptions.InvalidPriceException;

import java.util.logging.Logger;

public abstract class Product {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    protected String name;
    protected double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of a product, which must be between 0.00 to 9999999999999.99
     *
     * @throws InvalidPriceException If specified price is out of range.
     */
    public void setPrice(double price) throws InvalidPriceException {
        double zeroDollars = 0.00;
        double maxPrice = 9999999999999.99;
        String name = getName();
        if (price < zeroDollars || price > maxPrice) {
            logger.severe("Trigger InvalidPriceException()!");
            throw new InvalidPriceException(name);
        }
        this.price = price;
    }

    /**
     * Overrides the toString() method by formatting the Product as "name ($price)" where price is in 2d.p.
     */
    public String toString() {
        String price = String.format("%.2f", getPrice());
        return getName() + " ($" + price + ")";
    }
}
