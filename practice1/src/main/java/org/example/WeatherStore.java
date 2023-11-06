package org.example;

public interface WeatherStore extends AutoCloseable {

	default void close() {

	}
}
