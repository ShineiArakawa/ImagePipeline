package ImagePipeline.model.primitives;

import java.util.logging.Logger;

import ImagePipeline.util.Common;

public abstract class ConfigPrimitive {
    protected int _intValue = 0;
    protected double _doubleValue = 0.0;
    protected String _stringValue = "";

    protected ValueType _type = ValueType.Invalid;

    abstract public String getStringValue();

    public ValueType getValueType() {
        return _type;
    }

    public int getValueInt() {
        return _intValue;
    }

    public double getValueDouble() {
        return _doubleValue;
    }

    public String getValueString() {
        return _stringValue;
    }

    public static ConfigPrimitive getValue(String value, ValueType type) {
        ConfigPrimitive config = null;

        if (value != null && type != null) {
            try {
                if (type == ValueType.Double) {
                    double doubleValue = Double.parseDouble(value);
                    config = new DoubleConfig(doubleValue);
                } else if (type == ValueType.Int) {
                    int intValue = Integer.parseInt(value);
                    config = new IntConfig(intValue);
                } else if (type == ValueType.String) {
                    config = new StringConfig(value);
                }
            } catch (NumberFormatException e) {
                Logger logger = Logger.getLogger(ConfigPrimitive.class.getName());
                logger.setLevel(Common.GLOBAL_LOG_LEVEL);
                logger.warning("Type mismatch !");
                config = null;
            }
        }

        return config;
    }
}
