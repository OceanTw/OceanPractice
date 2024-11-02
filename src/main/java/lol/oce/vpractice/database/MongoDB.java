package lol.oce.vpractice.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

public class MongoDB {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "vpractice";

    private static MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
    @Getter
    private static MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

}