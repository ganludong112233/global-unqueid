import com.uniqueid.core.IdGeneratorService;

public class Test {

    public static void main(String[] args) {

        for (int i = 1; i <= 100; i++) {
            long id = IdGeneratorService.getInstance().getId("ORDER_ID");
            System.out.println(id);
        }

        IdGeneratorService.getInstance().shutdown();
    }
}
