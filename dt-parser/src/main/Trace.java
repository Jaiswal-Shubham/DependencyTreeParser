package main;

public class Trace {

    public Trace(String appName, String path) {
        this.appName = appName;
        this.path = path;
    }

    String appName;
    String path;


    @Override
    public String toString() {
        return "Trace{" +
                "appName='" + appName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }


}
