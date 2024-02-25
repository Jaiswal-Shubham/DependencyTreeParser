package main;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MavenDependencyParser {

    public static void main(String[] args) throws IOException {

        String currentPath = Paths.get("").toAbsolutePath().toString();
        String inputFolder = currentPath.concat("/").concat("src/resource");
        File folder = new File(inputFolder);

        File[] filesArr = folder.listFiles();

        List<File> files = Arrays.stream(filesArr).toList();

        files.forEach(inputFile -> {
            List<Dependency> roots = parseMavenDependencyTree(inputFile.getAbsolutePath());
            String appName = inputFile.getName().substring(0,inputFile.getName().length()-4);
            FlattenDependencyParserUtil.flatten(roots,appName);

        });

        FlattenDependencyParserUtil.exportToCSV();

    }

    private static List<Dependency> parseMavenDependencyTree(String inputFile) {
        List<Dependency> roots = new ArrayList<>();
        Dependency root = null;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            Dependency currentParent = null;
            int currentIndentLevel = 0;
            while ((line = br.readLine()) != null) {
                line = line.replace("[INFO] ", "");

                String[] parts = line.split(":");
                if (parts.length >= 4) {
                    String groupId = removeSpecialCharacters(parts[0].trim());
                    String artifactId = parts[1].trim();
                    String version = parts[3].trim();
                    String scope = parts[4].trim();

                    int indentLevel = calculateIndentLevel(line);
                    Dependency dependency = new Dependency(groupId, artifactId, version, scope);

                    if (indentLevel == 0) {
                        if(root != null)
                            roots.add(root);
                        root = dependency;
                    } else if (indentLevel > currentIndentLevel) {
                        if (currentParent != null) {
                            currentParent.children.add(dependency);
                        }
                    } else {
                        Dependency parent = findParent(root, indentLevel);
                        if (parent != null) {
                            parent.children.add(dependency);
                        }
                    }

                    currentParent = dependency;
                    currentIndentLevel = indentLevel;
                }
            }
            roots.add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return roots;
    }


    private static int calculateIndentLevel(String line) {
        int indentLevel = 0;
        while (line.charAt(indentLevel) == ' ' || line.charAt(indentLevel) == '|') {
            indentLevel++;
        }
        return indentLevel / 2;
    }

    private static Dependency findParent(Dependency root, int indentLevel) {
        if (indentLevel == 0) {
            return null;
        }
        Dependency parent = root;
        for (int i = 1; i < indentLevel-1; i++) {
            if (parent.children.isEmpty()) {
                return null;
            }
            parent = parent.children.get(parent.children.size() - 1);
        }
        return parent;
    }

    private static void printDependencyTree(Dependency root, int level) {
        // Print the current dependency with appropriate indentation
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        sb.append(root.groupId).append(":").append(root.artifactId).append(":").append(root.version);
        System.out.println(sb.toString());

        for (Dependency child : root.children) {
            printDependencyTree(child, level + 1);
        }
    }

    public static String removeSpecialCharacters(String str) {
        String regex = "^[^a-zA-Z0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.replaceFirst("");
    }
}
