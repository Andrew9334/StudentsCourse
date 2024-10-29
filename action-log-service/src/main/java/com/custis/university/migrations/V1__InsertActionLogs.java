package com.custis.university.migrations;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class V1__InsertActionLogs extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/student";
        String user = "user";
        String password = "password";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            MongoDatabase database = mongoClient.getDatabase("ActionLog");
            MongoCollection<Document> logCollection = database.getCollection("action_logs");

            String student = "SELECT * FROM Student";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(student)) {
                String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
                while (resultSet.next()) {
                    String studentId = resultSet.getString("id");

                    logCollection.insertMany(Arrays.asList(
                            new Document("action", "login")
                                    .append("studentId", studentId)
                                    .append("timestamp", timestamp),
                            new Document("action", "view_course")
                                    .append("studentId", statement)
                                    .append("timestamp", timestamp),
                            new Document("action", "logout")
                                    .append("studentId", studentId)
                                    .append("timestamp", timestamp)
                    ));
                }
            }

        }

    }
}
