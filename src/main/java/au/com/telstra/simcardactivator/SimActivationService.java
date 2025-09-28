package au.com.telstra.simcardactivator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@Service
public class SimActivationService {

    private static final Logger log = LoggerFactory.getLogger(SimActivationService.class);
    private static final URI ACTUATOR_URI = URI.create("http://localhost:8444/actuate");

    private final RestTemplate restTemplate;
    private final SimActivationRepository repository;

    public SimActivationService(RestTemplate restTemplate, SimActivationRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    public ActivationResponse activate(ActivationRequest request) {
        String iccid = request.getIccid();
        ActivationResponse response = callActuator(iccid);
        boolean success = response.isSuccess();

        SimActivationRecord record = new SimActivationRecord(
                iccid,
                request.getCustomerEmail(),
                success
        );

        SimActivationRecord savedRecord = repository.save(record);
        log.info("Persisted activation record id={} for iccid {}", savedRecord.getId(), iccid);
        return response;
    }

    public Optional<ActivationStatusResponse> findActivation(Long id) {
        return repository.findById(id)
                .map(record -> new ActivationStatusResponse(
                        record.getIccid(),
                        record.getCustomerEmail(),
                        record.isActive()
                ));
    }

    private ActivationResponse callActuator(String iccid) {
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
