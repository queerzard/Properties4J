package testpkg;

import com.github.queerzard.jproperties.config.PropertiesBase;
import com.github.queerzard.jproperties.observer.AsyncPropertiesObserver;

import java.io.NotSerializableException;
import java.lang.reflect.Field;

public class CustomPropertiesObserver extends AsyncPropertiesObserver {
    @Override
    protected void onPropertyUnchanged(PropertiesBase propertiesBase, String key, String currentValue) {
        System.out.println("property unchanged");
    }

    @Override
    public <T extends PropertiesBase> boolean onChange(T propertiesObject, Field field, Object oldValue, Object newValue) throws NotSerializableException {
        propertiesObject.save();
        return true;
    }
}
