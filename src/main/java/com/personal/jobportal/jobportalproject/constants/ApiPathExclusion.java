package com.personal.jobportal.jobportalproject.constants;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
@Getter
@AllArgsConstructor
public enum ApiPathExclusion {

    SWAGGER_RESOURCES("/swagger-resources/**"),
    SWAGGER_UI_HTML("swagger-ui.html"), WEBJARS("/webjars/**"), SWAGGER_UI("/swagger-ui/**"),
    SWAGGER_API_V3_DOCS("/v3/api-docs/**");

    private final String path;
}
