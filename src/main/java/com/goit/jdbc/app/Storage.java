package com.goit.jdbc.app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private String connectionURL = "jdbc:mysql://localhost/offlinetwo";
    private String user = "root";
    private String pass = "root";

    private Connection connection;
    private Statement statement;

    private PreparedStatement deleteDeveloperSt;
    private PreparedStatement selectSt;
    private PreparedStatement updateSt;

    public Storage() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
        }

        try {
            connection = DriverManager.getConnection(connectionURL, user, pass);
            statement = connection.createStatement();
            deleteDeveloperSt = connection.prepareStatement("DELETE FROM Developer WHERE ID=?");
            updateSt = connection.prepareStatement("update developer set firstName = ? , lastName = ?, specialty = ? where id = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initDataBase();

    }

    public void deleteDeveloper (long developerId){
        try {
            deleteDeveloperSt.setLong(1, developerId);
            deleteDeveloperSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDeveloper (Developer developer){
        try {
            updateSt.setString(1, developer.getFirstName());
            updateSt.setString(2, developer.getLastName());
            updateSt.setString(3, developer.getSpecialty());
            updateSt.setLong(4,developer.getId());
            updateSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void  createDeveloper (Developer developer){
        String sql = "INSERT INTO Developer (firstName, lastName, specialty) VALUES ('"+ developer.getFirstName() + "', '" + developer.getLastName() +
        "', '"+ developer.getSpecialty()+"')";
        System.out.println(sql);
        try {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Developer> getAllDevelopers () {
        List <Developer> result = new ArrayList<Developer>();
        String sql = "SELECT * FROM Developer";

        ResultSet rs = null;
        try {
            rs  = statement.executeQuery(sql);
            while (rs.next()){
                long id = rs.getLong("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String specialty = rs.getString("specialty");

                Developer developer = new Developer();
                developer.setId(id);
                developer.setFirstName(firstName);
                developer.setLastName(lastName);
                developer.setSpecialty(specialty);
                result.add(developer);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void initDataBase (){
        String sql  = "CREATE TABLE IF NOT EXISTS Developer (id int auto_increment PRIMARY KEY, firstName varchar(50), lastName varchar(50), specialty varchar (50))";
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        Storage storage = new Storage();
        Developer developer = new Developer();
        developer.setFirstName("Ann");
        developer.setLastName("Annova");
        developer.setSpecialty("Java");
        developer.setId(3);
        //storage.deleteDeveloper(4);
        storage.updateDeveloper(developer);
        /*storage.createDeveloper(developer);
        List<Developer> developers = storage.getAllDevelopers();
        for (Developer d : developers){
            System.out.println(d);
        }*/
    }
}

