package main;

import java.util.ArrayList;
import java.util.List;

public class FlattenDependency {
    String groupId;
    String artifactId;

    String version;
    String scope;

    List<Trace> traces;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<Trace> getTraces() {
        return traces;
    }

    public void setTraces(List<Trace> traces) {
        this.traces = traces;
    }

    public void addTrace(Trace trace){
        if(this.traces == null)
            this.traces = new ArrayList<>();
        this.traces.add(trace);
    }

    public FlattenDependency(String groupId, String artifactId, String version, String scope, List<Trace> traces) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
        this.traces = traces;
    }

    @Override
    public String toString() {
        return "FlattenDependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", scope='" + scope + '\'' +
                ", traces=" + traces +
                '}';
    }



}
