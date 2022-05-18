package by.incubator.application.engines;

public abstract class AbstractEngine implements Startable{
    String typeName;
    double typeTaxCoefficient;

    AbstractEngine(String typeName, double typeTaxCoefficient) {
        this.typeName = typeName;
        this.typeTaxCoefficient = typeTaxCoefficient;
    }

    String getTypeName() {
        return typeName;
    }

    void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    double getTypeTaxCoefficient() {
        return typeTaxCoefficient;
    }

    void setTypeTaxCoefficient(double typeTaxCoefficient) {
        this.typeTaxCoefficient = typeTaxCoefficient;
    }

    public String toString() {
        return typeName + "," + "\"" + typeTaxCoefficient + "\"";
    }
}
