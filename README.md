# get-annotation-methods-maven-plugin

To run the maven plugin:
1. Include the plugin in your project.
2. Execute mvn get-annotation-methods:find -DinterfaceNames="Event,XmlElement,Event3" from the cmd line for example.
3. This will return all the methods that contain any of the annotation names above.

Sample Output:

╰❱❱❱❱❱ mvn get-annotation-methods:find -DinterfaceNames="Event,XmlElement,Event3"                                     4.87G   19:25:50 
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building MyPluginTest 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- get-annotation-methods-maven-plugin:1.0-SNAPSHOT:find (default-cli) @ TestMavenPlugin ---

[INFO] The methods implementing the interface Event is :public void gotPackageName.YoYo.send()
[INFO] The fully qualified interface name is : com.src.walmart.Event
[INFO] The methods implementing the interface Event is :public void gotPackageName.innerPackage.YoYo2.send()
[INFO] The fully qualified interface name is : com.src.walmart.Event
[INFO] The methods implementing the interface XmlElement is :public void anotherPackage.nested.YoYo3.anotherMethod()
[INFO] The fully qualified interface name is : javax.xml.bind.annotation.XmlElement
[INFO] The methods implementing the interface Event is :public void anotherPackage.nested.YoYo3.send()
[INFO] The fully qualified interface name is : com.src.walmart.Event
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 0.626 s
[INFO] Finished at: 2018-07-07T19:51:20+05:30
[INFO] Final Memory: 10M/245M
[INFO] ------------------------------------------------------------------------
