package ImagePipeline.model.primitives;

public class DoubleConfig extends ConfigPrimitive {
    public DoubleConfig(double value) {
        _type = ValueType.Double;
        _doubleValue = value;
    }

    @Override
    public String getStringValue() {
        return Double.toString(_doubleValue);
    }
}