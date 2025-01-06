package com.huy.backend;

import com.huy.backend.models.Genre;
import com.huy.backend.repository.GenreRepo;
import com.huy.backend.repository.MovieRepo;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;


@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableScheduling
@EnableCaching
public class Application implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	@Autowired
	private MovieRepo movieRepository;

	@Autowired
	private GenreRepo genreRepository;

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// Create and save genres
//		movieRepository.deleteAll();
//		genreRepository.deleteAll();

//		List<Genre> genres = List.of(
//				new Genre(28L, "Action"),
//				new Genre(12L, "Adventure"),
//				new Genre(16L, "Animation"),
//				new Genre(35L, "Comedy"),
//				new Genre(80L, "Crime"),
//				new Genre(99L, "Documentary"),
//				new Genre(18L, "Drama"),
//				new Genre(10751L, "Family"),
//				new Genre(14L, "Fantasy"),
//				new Genre(36L, "History"),
//				new Genre(27L, "Horror"),
//				new Genre(10402L, "Music"),
//				new Genre(9648L, "Mystery"),
//				new Genre(10749L, "Romance"),
//				new Genre(878L, "Science Fiction"),
//				new Genre(10770L, "TV Movie"),
//				new Genre(53L, "Thriller"),
//				new Genre(10752L, "War"),
//				new Genre(37L, "Western")
//		);
//		genreRepository.saveAll(genres);

//		Genre action = new Genre();
//		action.setGenreId(GenerateRandom.generateRandomLong());
//		action.setName("Action");
//		genreRepository.save(action);
//
//		Genre drama = new Genre();
//		drama.setGenreId(GenerateRandom.generateRandomLong());
//		drama.setName("Drama");
//		genreRepository.save(drama);
//
//		Genre sciFi = new Genre();
//		sciFi.setGenreId(GenerateRandom.generateRandomLong());
//		sciFi.setName("Science Fiction");
//		genreRepository.save(sciFi);
//
//		// Create movies
//		Movie inception = new Movie();
//		inception.setMovieId(GenerateRandom.generateRandomLong());
//		inception.setTitle("Inception");
//		inception.setReleaseDate(LocalDate.of(2010, 7, 16));
//		inception.setRuntime(148);
//		inception.setOverview("A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.");
//		inception.setPosterPath("/inception-poster.jpg");
//		inception.setBackdropPath("/inception-backdrop.jpg");
//		inception.setTmdbId(27205);
//		inception.setCreatedAt(LocalDateTime.now());
//		inception.setGenres(Set.of(action, sciFi));
//		movieRepository.save(inception);
//
//		Movie shawshank = new Movie();
//		shawshank.setMovieId(GenerateRandom.generateRandomLong());
//		shawshank.setTitle("The Shawshank Redemption");
//		shawshank.setReleaseDate(LocalDate.of(1994, 9, 23));
//		shawshank.setRuntime(142);
//		shawshank.setOverview("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
//		shawshank.setPosterPath("/shawshank-poster.jpg");
//		shawshank.setBackdropPath("/shawshank-backdrop.jpg");
//		shawshank.setTmdbId(278);
//		shawshank.setCreatedAt(LocalDateTime.now());
//		shawshank.setGenres(Set.of(drama));
//		movieRepository.save(shawshank);
//
//		Movie darkKnight = new Movie();
//		darkKnight.setMovieId(GenerateRandom.generateRandomLong());
//		darkKnight.setTitle("The Dark Knight");
//		darkKnight.setReleaseDate(LocalDate.of(2008, 7, 18));
//		darkKnight.setRuntime(152);
//		darkKnight.setOverview("When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.");
//		darkKnight.setPosterPath("/dark-knight-poster.jpg");
//		darkKnight.setBackdropPath("/dark-knight-backdrop.jpg");
//		darkKnight.setTmdbId(155);
//		darkKnight.setCreatedAt(LocalDateTime.now());
//		darkKnight.setGenres(Set.of(action, drama));
//		movieRepository.save(darkKnight);


	}

}
