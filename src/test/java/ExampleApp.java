import lombok.Getter;
import lombok.SneakyThrows;

public class ExampleApp {

    @Getter
    private static ExampleApp instance;


    @SneakyThrows
    public ExampleApp() {
        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.save();
    }

    public static void main(String[] args) {
        instance = new ExampleApp();
    }

}
