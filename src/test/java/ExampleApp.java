import com.github.queerzard.jproperties.JProperties;
import lombok.Getter;
import lombok.SneakyThrows;

public class ExampleApp {

    @Getter
    private static ExampleApp instance;


    @SneakyThrows
    public ExampleApp() {
        JProperties jProperties = new JProperties();
        jProperties.registerConfigurations("testpkg.with.configs");
        jProperties.registerConfig(ExampleConfig.class);
    }

    public static void main(String[] args) {
        instance = new ExampleApp();
    }

}
