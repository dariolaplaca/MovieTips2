package com.gruppo4java11.MovieTips.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.gruppo4java11.MovieTips.entities.Movie;
import com.gruppo4java11.MovieTips.tmdbEntities.Genre;
import com.gruppo4java11.MovieTips.tmdbEntities.MovieTMDB;
import com.jayway.jsonpath.internal.filter.ValueNodes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieTMDbDeserializer extends StdDeserializer<MovieTMDB> {

    public MovieTMDbDeserializer(){
        this(null);
    }
    public MovieTMDbDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public MovieTMDB deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String title = node.get("original_title").asText();
        Integer id = node.get("id").asInt();
        String description = node.get("overview").asText();
        LocalDate releaseDate = LocalDate.parse(node.get("release_date").asText());
        String posterPath = node.get("poster_path").asText();

        List<Genre> genres = new ArrayList<>();
        JsonNode genresNode = node.get("genres");
        if (genresNode != null && genresNode.isArray()) {
            for (JsonNode genreNode : genresNode) {
                Integer genreId = genreNode.get("id").asInt();
                String genreName = genreNode.get("name").asText();
                Genre genre = new Genre(genreId, genreName);
                genres.add(genre);
            }
        }

        // Create an instance of Movie and set the extracted values
        MovieTMDB movie = new MovieTMDB();
        movie.setTitle(title);
        movie.setId(id);
        movie.setDescription(description);
        movie.setReleaseDate(releaseDate);
        movie.setPosterPath(posterPath);
        movie.setGenres(genres);

        return movie;
    }

}
