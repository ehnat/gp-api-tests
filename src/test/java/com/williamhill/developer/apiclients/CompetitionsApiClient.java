package com.williamhill.developer.apiclients;

import com.williamhill.developer.apiobjects.event.Event;
import com.williamhill.developer.apiobjects.outcome.Outcome;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;

public class CompetitionsApiClient {
    private static final String EVENTS_FOR_CATEGORY_ENDPOINT = "/competitions/categories/{categoryId}/classes/types/events";
    private static final String CATEGORIES_ENDPOINT = "/competitions/categories";
    private static final String OUTCOMES_FOR_EVENT_ENDPOINT = "/competitions/events/{eventId}/markets/outcomes";
    private static final String SPORTS_BOOK_JSON_CONTENT = "application/vnd.who.Sportsbook+json;v=1;charset=utf-8";

    private ApiContext apiContext;

    public CompetitionsApiClient(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    public Outcome getAnyOutcome() {
        Event event = getAnyNotInPlayEvent()
                .orElseThrow(() -> new RuntimeException("Couldn't find any not in play event"));

        JsonPath response =
                given().
                        spec(getRequestSpec()).
                when().
                        pathParam("eventId", event.getId()).
                        get(OUTCOMES_FOR_EVENT_ENDPOINT).
                then().
                        extract().jsonPath();
        return response.getList("whoCompetitions.event.market[0].outcome", Outcome.class).iterator().next();
    }

    private RequestSpecification getRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("accept", SPORTS_BOOK_JSON_CONTENT)
                .addHeader("who-apiKey", apiContext.getApiKey());
        return builder.build();
    }

    private Optional<Event> getAnyNotInPlayEvent() {
        return getAnyEvents().stream()
                .filter(event -> event.getAvailability().getInPlay().equals("N"))
                .findFirst();
    }

    private List<Event> getAnyEvents() {
        JsonPath response =
                given().
                        spec(getRequestSpec()).
                when().
                        pathParam("categoryId", getFirstCategoryId()).
                        get(EVENTS_FOR_CATEGORY_ENDPOINT).
                then().
                        extract().jsonPath();
        return response.getList("whoCompetitions.category.class.type[0].event", Event.class);
    }

    private String getFirstCategoryId() {
        return given().
                        spec(getRequestSpec()).
                when().
                        get(CATEGORIES_ENDPOINT).
                then().
                        extract().
                        path("whoCompetitions.category[0].id");
    }
}