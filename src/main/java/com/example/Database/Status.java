package com.example.Database;

public enum Status {
    K("Kredit"),
    C("Cash");

    public final String label;

    Status(String label) {
        this.label = label;
    }

    public static Status get(String label) {
        switch (label) {
            case "K": return K;
            default: return C;
        }

    }
}
