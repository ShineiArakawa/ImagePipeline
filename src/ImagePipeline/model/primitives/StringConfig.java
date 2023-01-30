package ImagePipeline.model.primitives;

public class StringConfig extends ConfigPrimitive {
    public StringConfig(String value) {
        _type = ValueType.String;
        _stringValue = value;
    }

    @Override
    public String getStringValue() {
        return _stringValue;
    }

}
