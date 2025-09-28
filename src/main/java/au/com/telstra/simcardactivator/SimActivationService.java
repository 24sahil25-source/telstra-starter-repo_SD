package au.com.telstra.simcardactivator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

@Service
public class SimActivationService {

    private static final Logger log = LoggerFactory.getLogger(SimActivationService.class);
    private static final URI ACTUATOR_URI = URI.create("http://localhost:8444/actuate");

    private final RestTemplate restTemplate;

    public SimActivationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ActivationResponse activate(String iccid) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(Collections.singletonMap("iccid", iccid));
        try {
            ResponseEntity<ActivationResponse> response = restTemplate.postForEntity(
                    ACTUATOR_URI,
                    requestEntity,
                    ActivationResponse.class
            );

            ActivationResponse body = response.getBody();
            if (body == null) {
                log.warn("Actuator response had no body for iccid {}", iccid);
                return new ActivationResponse(false);
            }
            return body;
        } catch (Exception ex) {
            log.error("Failed to call actuator for iccid {}", iccid, ex);
            return new ActivationResponse(false);
        }
    }
}
