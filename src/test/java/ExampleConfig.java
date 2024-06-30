import com.github.queerzard.jproperties.annotations.Ignore;
import com.github.queerzard.jproperties.annotations.ObjB64;
import com.github.queerzard.jproperties.annotations.Observe;
import com.github.queerzard.jproperties.annotations.Properties;
import com.github.queerzard.jproperties.config.PropertiesBase;
import lombok.Getter;

import java.io.NotSerializableException;
import java.util.HashMap;


@Properties
@Observe
public class ExampleConfig extends PropertiesBase {

    @Getter
    private final boolean foo = true;
    @Getter
    @Ignore
    private final String bar = "true";
    @Getter
    @ObjB64
    private HashMap<String, String> map;


    public ExampleConfig() {
/*        this.map = new HashMap<>();
        this.map.put("test", "value");
        System.out.println(map);*/

    }


    @Override
    public void postConstruct() throws NotSerializableException {

    }
}
