package ImagePipeline.model.primitives;

public class IntConfig extends ConfigPrimitive {
    public IntConfig(int value) {
        _type = ValueType.Int;
        _intValue = value;
    }

    @Override
    public String getStringValue() {
        return Integer.toString(_intValue);
    }
}