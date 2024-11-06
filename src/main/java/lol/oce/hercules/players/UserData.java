package lol.oce.hercules.players;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lol.oce.hercules.database.MongoDB;
import lol.oce.hercules.utils.ConsoleUtils;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.UUID;

public class UserData {
    private MongoCollection<Document> collection;

    public UserData() {
        MongoDatabase database = MongoDB.getDatabase();
        this.collection = MongoDB.getCollection("users");
    }

    public void saveUser(User user) {
        Document original = collection.find(new Document("player", user.getUuid().toString())).first();
        Document doc = new Document("player", user.getUuid().toString());

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
//                .setKitStats(new UserKitStats().deserialize(doc.getString("kitStats")))
                .build();
    }

    public User createUser(UUID uuid) {
        User user = User.builder()
                .setUuid(uuid)
                .setMatch(null)
                .setStatus(UserStatus.IN_LOBBY)
//                .setKitStats(new UserKitStats())
                .build();
//        user.kitStats = new UserKitStats();
        user.status = UserStatus.IN_LOBBY;
        saveUser(user);
        return user;
    }
}