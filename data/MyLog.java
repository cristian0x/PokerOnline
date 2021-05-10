package data;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.*;

public class MyLog {

    private final static Logger logger = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    private static void setupLogger(String fileName, String msg1) {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        try { // Default file output is in users's home directory
            FileHandler fileHandler = new FileHandler(fileName);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            // don't stop my program but log out to console.
            logger.log(Level.SEVERE, "File logger not working.", e);
        }
        System.out.println("----------LOG----------");
        logger.info(msg1);
    }

    public static void writeToFile(String username, String password, String fileName) {
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file, true);
            BufferedWriter myWriter = new BufferedWriter(new OutputStreamWriter(fos));
            if ((username != null && !username.isEmpty()) && (password != null && !password.isEmpty())) {
                myWriter.write(username + ":" + password);
                myWriter.newLine();
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } else {
                System.out.println("Invalid data");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeToFile(String username, int money, String fileName) {
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file, true);
            BufferedWriter myWriter = new BufferedWriter(new OutputStreamWriter(fos));
            String moneyString = String.valueOf(money);
            myWriter.write(username + ":" + moneyString);
            myWriter.newLine();
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static ArrayList readFromFile(String fileName) {
        ArrayList<String> arrayList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while (reader.ready()) {
                arrayList.add(reader.readLine());
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return arrayList;
    }

    public static Logger getLogger() {
        if(logger == null){
            new MyLog();
        }
        return logger;
    }

    public static void log(Level level, String msg, String msg1, String fileName){
        setupLogger(fileName, msg1);
        //getLogger().log(level, msg, msg1);
        System.out.println(msg + ": " + msg1);
    }


    public static void main(String[] args) throws java.io.IOException {}
}