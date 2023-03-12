package com.example.Database.Connection;

import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.Document;

public class MongoDBConnection {
    private final MongoClient mongoClient;
    private final String dbName;
    private final String collectionName;

    public MongoDBConnection(String dbName, String collectionName) {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost/");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();

        this.mongoClient = MongoClients.create(settings);

        this.dbName = dbName;
        this.collectionName = collectionName;
    }

    public MongoCollection<Document> getCollection() {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database.getCollection(collectionName);
    }
}
