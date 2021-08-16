# ReadMe.md

To compile the project, open the terminal to the ```pulsar/skyview/``` folder. Execure the following command:

```sh
mvn clean package
```

The from that same directory, run the project with this command:

```sh
java -cp target/skyview-1.0-SNAPSHOT.jar com.datastax.pulsar.App
```

In the pulsar/skyview/src/main/java/com/datastax/pulsar/ folder you will need to create a Credentials.java file with the following contents:

```java
package com.datastax.pulsar;

public class Credentials {
    // This is the pulsar token
    public static String token="<your pulsar token here>";
}
```
