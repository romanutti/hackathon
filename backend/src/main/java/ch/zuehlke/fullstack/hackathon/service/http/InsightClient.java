package ch.zuehlke.fullstack.hackathon.service.http;

import ch.zuehlke.fullstack.hackathon.model.Skill;
import ch.zuehlke.fullstack.hackathon.model.TrainingResponse;
import ch.zuehlke.fullstack.hackathon.model.UserInfo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class InsightClient {
    private final String EMPLOYEES = "/api/v1/employees";
    private final String SEARCH = "/search";
    private final String SKILLS = "/{userId}/skills";
    private final String TRAININGS = "/api/v1/training/employees/{userId}";
    private final String PICTURES = "/api/v1/pictures/{pictureId}?width=400&height=400";
    private final RestTemplate restTemplate;

    public InsightClient(RestTemplateFactory factory) {
        this.restTemplate = factory.getInsightRestTemplate();
    }

    public List<UserInfo> getEmployees(String searchTerm) {
        String uriPath = UriComponentsBuilder.fromPath(EMPLOYEES)
                .path(SEARCH)
                .queryParam("term", URLEncoder.encode(searchTerm, StandardCharsets.UTF_8))
                .toUriString();
        ResponseEntity<List<UserInfo>> response
                = restTemplate.exchange(uriPath, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        assert (response.getStatusCode()).is2xxSuccessful();
        return response.getBody();
    }

    public List<Skill> getSkills(String userId) {
        String uriPath = UriComponentsBuilder.fromPath(EMPLOYEES)
                .path(SKILLS)
                .buildAndExpand(URLEncoder.encode(userId, StandardCharsets.UTF_8))
                .toUriString();
        ResponseEntity<List<Skill>> response
                = restTemplate.exchange(uriPath, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        assert (response.getStatusCode()).is2xxSuccessful();
        return response.getBody();
    }
    
    public List<TrainingResponse> getTrainings(String userId) {
        String uriPath = UriComponentsBuilder.fromPath(TRAININGS)
                .buildAndExpand(URLEncoder.encode(userId, StandardCharsets.UTF_8))
                .toUriString();
        ResponseEntity<List<TrainingResponse>> response
                = restTemplate.exchange(uriPath, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        assert (response.getStatusCode()).is2xxSuccessful();
        return response.getBody();
    }

    public byte[] getPicture(String pictureId) {
        String uriPath = UriComponentsBuilder.fromPath(PICTURES)
                .buildAndExpand(URLEncoder.encode(pictureId, StandardCharsets.UTF_8))
                .toUriString();

        List<HttpMessageConverter<?>> converters = this.restTemplate.getMessageConverters();
        converters.add(new ByteArrayHttpMessageConverter());
        this.restTemplate.setMessageConverters(converters);

        ResponseEntity<byte[]> response
                = this.restTemplate.exchange(uriPath, HttpMethod.GET, null, byte[].class);
        return response.getBody();
    }
}
