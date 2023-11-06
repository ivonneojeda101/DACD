package org.example;

import com.google.gson.Gson;
import org.example.model.Movie;
import org.example.model.Session;
import org.example.model.Theater;

import java.io.*;
import java.util.Date;

public class SerializationExercises {
	/*
        Should define the class for the concepts Movie, Theater and Session.
        A session is a play of movie in a theater.
        Create 2 instances of each class and relate among them.
        Serialize to Json all objects and save then in different files.
     */
	public static class Exercise1 {
		public static void main(String[] args) throws IOException {
			Movie movie1 = new Movie("The Creator","Gareth Edwards");
			Movie movie2 = new Movie("A Haunting in Venice", "Kenneth Branagh");
			Theater theater1 = new Theater("Yelmo Cines Las Arenas", "Carretera El Rincón S/N, 35010 Las Palmas de Gran Canaria");
			Theater theater2 = new Theater("Artesiete Las Terrazas", "Atovía de Gran Canaria km 5,5 salida \"El Cortijo\" Telde, 35220 Telde");
			Session session1 = new Session(movie1, new Date(), theater1);
			Session session2 = new Session(movie2, new Date(), theater2);

			generateFile("movie1", movie1);
			generateFile("movie2", movie2);
			generateFile("theater1", theater1);
			generateFile("theater2", theater2);
			generateFile("session1", session1);
			generateFile("session2", session2);

		}

		public static void generateFile(String name, Object object) throws IOException {
			File file = new File("./src/output/"+ name + ".json");
			Gson gson = new Gson();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(gson.toJson(object));
			bw.close();
		}
	}

	/*
		Deserialize the objects created in exercise 1.
		Now serialize them using ObjectOutputStream
	 */
	public static class Exercise2 {

		public static void main(String[] args) {

		}
	}

	/*
	   Deserialize the objects from the binary files created in exercise 2.
	*/
	public static class Exercise3 {

		public static void main(String[] args) {

		}
	}
}
