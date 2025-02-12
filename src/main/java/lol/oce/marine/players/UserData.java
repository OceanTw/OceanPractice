package lol.oce.marine.players;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lol.oce.marine.database.MongoDB;
import org.bson.Document;

import java.util.UUID;

public class UserData {
    private final MongoCollection<Document> collection;

    public UserData() {
        MongoDatabase database = MongoDB.getDatabase();
        this.collection = MongoDB.getCollection("users");
    }

    public void saveUser(User user) {
        Document original = collection.find(new Document("player", user.getUuid().toString())).first();
        UserKitStats stats = user.getKitStats();
        if (stats == null) {
            stats = new UserKitStats();
        }
        Document doc = new Document("player", user.getUuid().toString())
            .append("stats", stats.serialize());

        if (original == null) {
            collection.insertOne(doc);
        } else {
            collection.replaceOne(original, doc);
        }
    }

    public User loadUser(UUID uuid) {
        Document doc = collection.find(new Document("player", uuid.toString())).first();
        if (doc == null) {
            return createUser(uuid);
        }
        return User.builder()
                .setMatch(null)
                .setUuid(uuid)
                .setStatus(UserStatus.IN_LOBBY)
                .setKitStats(new UserKitStats().deserialize(doc.getString("stats")))
                .build();
    }

    public User createUser(UUID uuid) {
        User user = User.builder()
                .setUuid(uuid)
                .setMatch(null)
                .setStatus(UserStatus.IN_LOBBY)
                .setKitStats(new UserKitStats())
                .build();
        user.status = UserStatus.IN_LOBBY;
        saveUser(user);
        return user;
    }
}
