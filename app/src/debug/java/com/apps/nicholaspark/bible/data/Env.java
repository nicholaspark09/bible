package com.apps.nicholaspark.bible.data;

public enum Env {
  MOCK_MODE("Mock Mode", "http://localhost/mock/"),
  PRODUCTION_MODE("Production Mode", "");

  public final String name;
  public final String url;

  Env(String name, String url) {
    this.name = name;
    this.url = url;
  }

  public static Env from(String endpoint) {
    for (Env value : values()) {
      if (value.url.equals(endpoint)) {
        return value;
      }
    }
    throw new IllegalArgumentException(String.format("No corresponding env for endpoint: %s", endpoint));
  }

  public boolean isMockMode() {
    return this == MOCK_MODE;
  }

  @Override public String toString() {
    return name;
  }
}
