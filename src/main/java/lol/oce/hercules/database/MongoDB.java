package lol.oce.hercules.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lol.oce.hercules.Practice;
import lombok.Getter;

public class MongoDB {
    private static final String CONNECTION_STRING = Practice.getDatabaseConfig().getConfiguration().getString( "connection-string");
    private static final String DATABASE_NAME = "vpractice";

    private static MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
    @Getter
    private static MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

}