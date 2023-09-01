package house.greysap;

import java.util.Date;


public class Price {
    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                '}';
    }

    /**
     * Identifier in DB
     */
    private long id = 0; // Excluded
    /**
     * Product code
     */
    private String productCode;
    /**
     * Price number
     */
    private int number;
    /**
     * Department number
     */
    private int depart;
    /**
     * Start date
     */
    private java.util.Date begin;
    /**
     * End date
     */
    private java.util.Date end;
    /**
     * Price in the smallest units (i.e. cents)
     */
    private long value;

    public Price(String productCode, int number, int depart, Date begin, Date end, long value) {
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getNumber() {
        return number;
    }

    public int getDepart() {
        return depart;
    }

    public java.util.Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public java.util.Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        if (number != price.number) return false;
        if (depart != price.depart) return false;
        if (value != price.value) return false;
        if (!productCode.equals(price.productCode)) return false;
        if (!begin.equals(price.begin)) return false;
        return end.equals(price.end);
    }

    @Override
    public int hashCode() {
        int result = productCode.hashCode();
        result = 31 * result + number;
        result = 31 * result + depart;
        result = 31 * result + begin.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }
}
