import com.github.queerzard.jproperties.annotations.Ignore;
import com.github.queerzard.jproperties.annotations.ObjB64;
import com.github.queerzard.jproperties.annotations.Properties;
import com.github.queerzard.jproperties.config.PropertiesBase;
import lombok.Getter;

import java.io.NotSerializableException;
import java.util.HashMap;


@Properties
public class ExampleConfig extends PropertiesBase {

    @Getter
    private String foo = "true";
    @Getter
    @Ignore
    private String bar = "true";
    @Getter
    @ObjB64
    private HashMap<String, String> map;


    public ExampleConfig() throws NotSerializableException {
        postConstructHandover(this);
        System.out.println(map.toString());
    }
}
