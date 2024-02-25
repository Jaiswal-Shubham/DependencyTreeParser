package main;

import java.util.ArrayList;
import java.util.List;

class Dependency {
    String groupId;
    String artifactId;
    String version;

    String scope;
    List<Dependency> children;

    public Dependency(String groupId, String artifactId, String version, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
        this.children = new ArrayList<>();
    }
}