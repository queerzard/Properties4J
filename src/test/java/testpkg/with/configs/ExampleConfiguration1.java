package testpkg.with.configs;

import com.github.queerzard.jproperties.annotations.Observe;
import com.github.queerzard.jproperties.annotations.Properties;
import com.github.queerzard.jproperties.annotations.Register;
import com.github.queerzard.jproperties.config.PropertiesBase;
import lombok.Getter;
import testpkg.CustomPropertiesObserver;

import java.io.NotSerializableException;

@Properties
@Observe(observer = CustomPropertiesObserver.class)
@Register
public class ExampleConfiguration1 extends PropertiesBase {

    @Getter
    private final String dasIst = "ein Test";

    public ExampleConfiguration1() throws NotSerializableException {
    }

    @Override
    public void postConstruct() throws NotSerializableException {
        save();
    }
}
