import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class searches a log file for IP addresses or usernames, stores them in a hashmap,
 * and provides methods to print the contents of the hashmap.
 *
 * @author Jaleah Beason
 * @version 1.0
 * Assignment 4
 * CS322 - Compiler Construction
 * Spring 2024
 */
public class LogFileProcessor {
	 /**
     * Main method to run the program.
     *
     * @param args Command-line arguments: filename and print flag.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java LogFileProcessor <filename> <print_flag>");
            return;
        }

        String fileName = args[0];
        int printFlag = Integer.parseInt(args[1]);

        LogFileProcessor processor = new LogFileProcessor();
        processor.processLogFile(fileName);

        switch (printFlag) {
            case 1:
                processor.printIPAddressMap();
                break;
            case 2:
                processor.printUsernameMap();
                break;
            default:
                System.out.println(processor.getIpAddressMapSize() + " unique IP addresses in the log.");
                System.out.println(processor.getUsernameMapSize() + " unique users in the log.");
                break;
        }
    }
    private HashMap<String, Integer> ipAddressMap;
    private HashMap<String, Integer> usernameMap;

    /**
     * Constructs a LogFileProcessor object and initializes the hashmaps.
     */
    public LogFileProcessor() {
        this.ipAddressMap = new HashMap<>();
        this.usernameMap = new HashMap<>();
    }

    /**
     * Processes the given log file, searching for IP addresses and usernames, and stores them
     * in the respective hashmaps.
     *
     * @param fileName The name of the log file to process.
     */
    public void processLogFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes a single log file line, searching for IP addresses and usernames,
     * and updates the respective hashmaps.
     *
     * @param line The log file line to process.
     */
    private void processLine(String line) {
        HashSet<String> ips = extractIPAddresses(line);
        for (String ip : ips) {
            ipAddressMap.put(ip, ipAddressMap.getOrDefault(ip, 0) + 1);
        }

        HashSet<String> usernames = extractUsernames(line);
        for (String username : usernames) {
            usernameMap.put(username, usernameMap.getOrDefault(username, 0) + 1);
        }
    }

    /**
     * Extracts unique IP addresses from a log file line.
     *
     * @param line The log file line.
     * @return A set of unique IP addresses.
     */
    private HashSet<String> extractIPAddresses(String line) {
        HashSet<String> ips = new HashSet<>();
        String regex = "\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            ips.add(matcher.group());
        }
        return ips;
    }

    /**
     * Extracts unique usernames from a log file line.
     *
     * @param line The log file line.
     * @return A set of unique usernames.
     */
    private HashSet<String> extractUsernames(String line) {
        HashSet<String> usernames = new HashSet<>();
        String regex = "user=([\\w-]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            usernames.add(matcher.group(1));
        }
        return usernames;
    }

    /**
     * Returns the size of the IP address hashmap.
     *
     * @return The number of unique IP addresses.
     */
    public int getIpAddressMapSize() {
        return ipAddressMap.size();
    }

    /**
     * Returns the size of the username hashmap.
     *
     * @return The number of unique usernames.
     */
    public int getUsernameMapSize() {
        return usernameMap.size();
    }

    /**
     * Prints the contents of the IP address hashmap.
     */
    public void printIPAddressMap() {
        for (String ip : ipAddressMap.keySet()) {
            System.out.println(ip + ": " + ipAddressMap.get(ip));
        }
    }

    /**
     * Prints the contents of the username hashmap.
     */
    public void printUsernameMap() {
        for (String username : usernameMap.keySet()) {
            System.out.println(username + ": " + usernameMap.get(username));
        }
    }


    }
