package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class FlattenDependencyParserUtil {

    List<FlattenDependency> flattenDependencies = new ArrayList<>();
    static Map<String, FlattenDependency> dependencyMap = new HashMap<>();
    static List<FlattenDependency> dependencyList = new ArrayList<>();

    static String outputFile = "output.csv";

    public static void flatten(List<Dependency> roots, String appName) {
        for (Dependency dependency : roots) {
            flatten(dependency, appName, "");
        }
    }

    public static void flatten(Dependency dependency, String appName, String path) {

        String key = dependency.groupId.concat("-")
                .concat(dependency.artifactId).concat("-")
                .concat(dependency.version).concat("-")
                .concat(dependency.scope);

        if (dependencyMap.containsKey(key)) {
            FlattenDependency flatterDependency = dependencyMap.get(key);
            flatterDependency.addTrace(new Trace(appName, path));
            dependencyMap.replace(key, flatterDependency);
        } else {
            List<Trace> traces = new ArrayList<>();
            traces.add(new Trace(appName, path));
            FlattenDependency flattenDependency = new FlattenDependency(
                    dependency.groupId,
                    dependency.artifactId,
                    dependency.version,
                    dependency.scope,
                    traces);
            dependencyList.add(flattenDependency);
            dependencyMap.put(key, flattenDependency);
        }

        for (Dependency child : dependency.children) {
            flatten(child, appName, dependency.groupId.concat(":")
                    .concat(dependency.artifactId).concat(":")
                    .concat(dependency.version).concat("--->").concat(path));

        }
    }

    public static void exportToCSV() {
        dependencyList.forEach(System.out::println);
        dependencyList.sort(Comparator
                .comparing(FlattenDependency::getGroupId)
                .thenComparing(FlattenDependency::getArtifactId)
                .thenComparing(FlattenDependency::getVersion)
                .thenComparing(FlattenDependency::getScope));

        String currentPath = Paths.get("").toAbsolutePath().toString().concat("/").concat("src/resource");
        String filePath = currentPath.concat("/").concat(outputFile);

        writeDataToCSV(dependencyList, filePath);

    }


    public static void writeDataToCSV(List<FlattenDependency> dependencies, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("GroupId,ArtifactId,Version,Scope,Parent,AppName\n");
            for (FlattenDependency dependency : dependencies) {
                for (Trace trace : dependency.getTraces()) {
                    if(!trace.appName.equals("test") && !trace.appName.equals("test2")) {
                        System.out.println(trace.path);
                    }
                    else
                        System.out.println(trace.appName);

                    writer.write(dependency.getGroupId() + "," +
                            dependency.getArtifactId() + "," +
                            dependency.getVersion() + "," +
                            dependency.getScope() + "," +
                            trace.path + "," +
                            trace.appName + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}