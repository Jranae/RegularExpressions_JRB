import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class processes a novel file and counts the occurrences of specific word patterns.
 * It reads a list of regex patterns from a file, searches for each pattern in the novel file,
 * and writes the pattern and its count into an output file.
 *
 * @author Jaleah Beason
 * @version 1.0
 * Assignment 4
 * CS322 - Compiler Construction
 * Spring 2024
 */
public class NovelProcessor{ 

    /**
     * Processes the novel file and counts occurrences of specific word patterns.
     * Writes the pattern and its count into an output file.
     *
     * @param novelFileName   The name of the novel file to process.
     * @param patternFileName The name of the file containing regex patterns.
     */
    public static void processNovel(String novelFileName, String patternFileName) {
        try (BufferedReader novelReader = new BufferedReader(new FileReader(novelFileName));
             BufferedReader patternReader = new BufferedReader(new FileReader(patternFileName))) {

            StringBuilder novelText = new StringBuilder();
            String line;
            while ((line = novelReader.readLine()) != null) {
                novelText.append(line).append("\n");
            }

            HashMap<String, Integer> patternCounts = new HashMap<>();
            String patternLine;
            while ((patternLine = patternReader.readLine()) != null) {
                String pattern = patternLine.trim();
                int count = countPatternOccurrences(novelText.toString(), pattern);
                patternCounts.put(pattern, count);
            }

            String outputFileName = novelFileName.replace(".txt", "_wc.txt");
            try (FileWriter writer = new FileWriter(outputFileName)) {
                for (String pattern : patternCounts.keySet()) {
                    writer.write(pattern + "|" + patternCounts.get(pattern) + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Counts the occurrences of a pattern in the given text.
     *
     * @param text    The text to search for occurrences.
     * @param pattern The regex pattern to search for.
     * @return The count of occurrences of the pattern in the text.
     */
    private static int countPatternOccurrences(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    /**
     * Main method to run the program.
     *
     * @param args Command-line arguments: novel file name and pattern file name.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java NovelProcessor <novel_file_name> <pattern_file_name>");
            return;
        }

        String novelFileName = args[0];
        String patternFileName = args[1];
        processNovel(novelFileName, patternFileName);
    }
}