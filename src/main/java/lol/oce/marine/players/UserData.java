package lol.oce.marine.players;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lol.oce.marine.Practice;
import lol.oce.marine.database.MongoDB;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
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
        return new User(
            uuid,
            new UserKitStats().deserialize(doc.getString("stats")),
            UserStatus.IN_LOBBY,
            null,
            null
        );
    }

    public User createUser(UUID uuid) {
        User user = new User(uuid, new UserKitStats(), UserStatus.IN_LOBBY, null, null);
        saveUser(user);
        return user;
    }

    public List<User> getUsers(int startIndex, int amount) {
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find().skip(startIndex).limit(amount)) {
            if (Practice.getInstance().getUserManager().getUser(UUID.fromString(doc.getString("player"))) != null) {
                users.add(Practice.getInstance().getUserManager().getUser(UUID.fromString(doc.getString("player"))));
            } else {
                users.add(new User(
                        UUID.fromString(doc.getString("player")),
                        new UserKitStats().deserialize(doc.getString("stats")),
                        UserStatus.OFFLINE,
                        null,
                        null
                ));
            }
        }
        return users;
    }

    public List<User> getUsers(String filter) {
        List<User> users = new ArrayList<>();
        for (Document doc : collection.find(new Document("player", filter))) {
            if (Practice.getInstance().getUserManager().getUser(UUID.fromString(doc.getString("player"))) != null) {
                users.add(Practice.getInstance().getUserManager().getUser(UUID.fromString(doc.getString("player"))));
            } else {
                users.add(new User(
                        UUID.fromString(doc.getString("player")),
                        new UserKitStats().deserialize(doc.getString("stats")),
                        UserStatus.OFFLINE,
                        null,
                        null
                ));
            }
        }
        return users;
    }
}
