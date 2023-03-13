package com.example.Database.Connection;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.connection.ClusterType;
import org.bson.Document;

public class MongoDBConnection {
    private MongoClient mongoClient;
    private final String dbName;
    private final String collectionName;

    public MongoDBConnection(String dbName, String collectionName) {
        this.dbName = dbName;
        this.collectionName = collectionName;

        try {
            this.mongoClient = MongoClients.create(getClientSettings());
            System.out.println("Connected to MongoDB server");
        } catch (MongoSocketException e) {
            System.err.println("Failed to connect to MongoDB server: " + e.getMessage());
            throw e;
        }
    }

    public MongoCollection<Document> getCollection() throws MongoException {
        this.mongoClient = MongoClients.create(getClientSettings());
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database.getCollection(collectionName);
    }
    public void close() {
        this.mongoClient.close();
    }

    private MongoClientSettings getClientSettings() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost/");
        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
    }
}
