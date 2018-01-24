# Distributed Global Id generator
##The desgin class diagram

![Desgin UML Class Diagram](https://raw.githubusercontent.com/ganludong112233/global-unqueid/master/uml/uml.png)

## embeded jar, example code
~~~
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
~~~

## the definition of ID Generator
create the file named with id_generator.yaml and put it under the classpath directory. Below configuration file define two id generators (db and local), if there are more than one id generator be defined , the client will round-robin style to get global id.
~~~
id.generator:
       - 
         type: db
         username: develop
         driverClass: com.mysql.jdbc.Driver
         password: Aa123456
         url: jdbc:mysql://localhost:3308/test
       -
         type: local
~~~

