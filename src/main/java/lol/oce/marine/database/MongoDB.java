package lol.oce.marine.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lol.oce.marine.Practice;
import lol.oce.marine.configs.impl.SettingsLocale;
import lol.oce.marine.utils.ConsoleUtils;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;

public class MongoDB {
    private static final String CONNECTION_STRING = SettingsLocale.MONGO.getString();
    private static final String DATABASE_NAME = "oceanpractice";

    private static MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
    @Getter
    private static MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

    public static MongoCollection<Document> getCollection(String name) {

        if (!database.listCollectionNames().into(new ArrayList<>()).contains(name)) {
            database.createCollection(name);
        }
        return database.getCollection(name);
    }

}