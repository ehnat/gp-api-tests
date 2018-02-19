package com.williamhill.developer.api.dto.outcome;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class Odds {

    private LivePrice livePrice;
}
