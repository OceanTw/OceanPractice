package rip.venus.star.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import rip.venus.star.Practice;
import rip.venus.star.utils.ConsoleUtils;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;

public class MongoDB {
    private static final String CONNECTION_STRING = Practice.getDatabaseConfig().getConfiguration().getString( "connection-string");
    private static final String DATABASE_NAME = "star";

    private static MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
    @Getter
    private static MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

    public static MongoCollection<Document> getCollection(String name) {


        if (!database.listCollectionNames().into(new ArrayList<>()).contains(name)) {
            database.createCollection(name);
        }
        return database.getCollection(name);
    }

    public static void test() {
        MongoDatabase database = MongoDB.getDatabase();
        MongoCollection<Document> collection = database.getCollection("users");

        // Insert a test document
        Document testUser = new Document("name", "testUser")
                .append("uuid", "1234-5678-9101-1121")
                .append("email", "testUser@example.com");
        collection.insertOne(testUser);

        // Verify the document is inserted
        Document foundUser = collection.find(new Document("name", "testUser")).first();
        if (foundUser != null) {
            ConsoleUtils.info("Test user found: " + foundUser.toJson());
        } else {
            ConsoleUtils.info("Test user not found");
        }
    }


}