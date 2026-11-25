import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Obfuscator {

    public static void main(String[] args) throws IOException {
        String filePath = "TestFile.java";
        Path path = Paths.get(filePath);
        String code = Files.readString(path);

        // ПОДЗАДАЧА 2: Удаление комментариев
        code = code.replaceAll("(?s)/\\*.*?\\*/", "");
        code = code.replaceAll("//.*", "");

        // ПОДЗАДАЧА 3: Замена имени класса
        Matcher m = Pattern.compile("\\bclass\\s+(\\w+)").matcher(code);
        String oldName = m.find() ? m.group(1) : "Main";
        String newName = "A";
        code = code.replaceAll("\\b" + oldName + "\\b", newName);

        // ПОДЗАДАЧА 4: Замена идентификаторов
        Set<String> ids = new LinkedHashSet<>();
        Matcher im = Pattern.compile("\\b[a-zA-Z_][a-zA-Z0-9_]*\\b").matcher(code);
        while (im.find()) {
            String id = im.group();
            if (!isKeyword(id) && !id.equals(newName)) ids.add(id);
        }

        Map<String, String> map = new HashMap<>();
        int i = 0;
        boolean twoChars = ids.size() > 52;
        for (String id : ids) {
            map.put(id, generateName(i++, twoChars));
        }

        List<String> keys = new ArrayList<>(map.keySet());
        keys.sort((a, b) -> b.length() - a.length());
        for (String k : keys) {
            code = code.replaceAll("\\b" + k + "\\b", map.get(k));
        }

        // ПОДЗАДАЧА 1: Сжатие кода
        code = code.replaceAll("\\s+", " ");
        code = code.replaceAll("\\{", " {\n").replaceAll(";", ";\n");

        // Сохранение и переименование файла
        Path newFile = path.getParent() != null
                ? path.getParent().resolve(newName + ".java")
                : Paths.get(newName + ".java");
        Files.writeString(newFile, code);
        if (!path.toAbsolutePath().equals(newFile.toAbsolutePath())) Files.delete(path);

        System.out.println("Готово. Файл: " + newFile.getFileName());
    }

    private static String generateName(int idx, boolean twoChars) {
        if (!twoChars) {
            return idx < 26 ? String.valueOf((char)('a' + idx)) : String.valueOf((char)('A' + idx - 26));
        }
        return "" + (char)('a' + idx / 26) + (char)('a' + idx % 26);
    }

    private static boolean isKeyword(String w) {
        return Arrays.asList("abstract","assert","boolean","break","byte","case","catch","char",
                "class","const","continue","default","do","double","else","enum","extends","final",
                "finally","float","for","goto","if","implements","import","instanceof","int",
                "interface","long","native","new","package","private","protected","public","return",
                "short","static","strictfp","super","switch","synchronized","this","throw","throws",
                "transient","try","void","volatile","while","true","false","null","String","System",
                "out","println","print","main","args","Exception","Override","Deprecated").contains(w);
    }
}