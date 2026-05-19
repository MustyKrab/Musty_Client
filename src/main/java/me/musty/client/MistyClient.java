package me.musty.client;

public class MistyClient {
    private static MistyClient INSTANCE;
    public static MistyClient getInstance() {
        if (INSTANCE == null) INSTANCE = new MistyClient();
        return INSTANCE;
    }
}
