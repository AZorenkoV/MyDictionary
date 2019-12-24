import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;


public class myDictionary {
    private static Connection CONNECTION = null;
    private static Properties dataToConn = new Properties();
    static {
        try (FileReader fr = new FileReader("config.properties")){
            dataToConn.load(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String URL = "jdbc:mysql://"+ dataToConn.getProperty("server_ip") +":3306/my_db";

    public static void main(String[] args){
        Scanner enter = new Scanner(System.in);

        try (Statement statement = getConnectStat()) {
            int choice = 0;
            do{
                System.out.println("What do you do?");
                System.out.println("1 - Insert new word");
                System.out.println("2 - list all words");
                System.out.println("0 - exit");
                System.out.print("Enter: ");
                choice = Integer.valueOf(enter.nextLine());
                if (choice == 1) {
                    System.out.print("Word: ");
                    String word = enter.nextLine();
                    System.out.print("Translete: ");
                    String trans = enter.nextLine();
                    insertRecord(word, trans, statement);
                } else if (choice == 2) {
                    showAllRecords(statement);
                } else if (choice != 0){
                    System.out.println("Wrong choose");
                }
            } while (choice != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Statement getConnectStat() throws SQLException {
        if (CONNECTION == null) CONNECTION = DriverManager.getConnection(URL,dataToConn);
        return CONNECTION.createStatement();
    }

    public static void insertRecord(String word, String trans, Statement statement) throws SQLException {
        String query = "INSERT INTO words (word, trans) VALUES (\'"+ word + "\' , \'" + trans + "\')";
        statement.execute(query);
    }

    public static void showAllRecords(Statement statement) throws SQLException {
        String query1 = "SELECT * FROM words";
        ResultSet resultSet = statement.executeQuery(query1);

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id") + "\t" +
                    resultSet.getString("word") + "\t" +
                    resultSet.getString("trans"));
        }
    }
}


