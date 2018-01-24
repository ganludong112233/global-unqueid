import java.util.concurrent.locks.LockSupport;

import com.uniqueid.core.IdGeneratorService;
import com.uniqueid.core.NoIdReturnException;

public class Test {

    public static void main(String[] args) {

        for (int i = 1; i <= 100000000; i++) {
            LockSupport.parkNanos(100000000);
            long time1 = System.currentTimeMillis();
            try {
                IdGeneratorService.getInstance().getId("ORDER_ID");
            } catch (NoIdReturnException e) {
                e.printStackTrace();
            }
            long time2 = System.currentTimeMillis();
            System.out.println(time2 - time1);
        }

        IdGeneratorService.getInstance().shutdown();
    }
}
