package tn.example.sst.config;

public interface Constants {
    String SPRING_PROFILE_TEST = "test";
    String SPRING_PROFILE_DEVELOPMENT = "dev";
    String SPRING_PROFILE_PRODUCTION = "prod";
    String SPRING_PROFILE_API_DOCS = "api-docs";
    String SYSTEM = "system";

    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    public static final Integer BATCH_SIZE = 20;
}
