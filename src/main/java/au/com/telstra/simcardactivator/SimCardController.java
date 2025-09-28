package au.com.telstra.simcardactivator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@Validated
public class SimCardController {

    private static final Logger log = LoggerFactory.getLogger(SimCardController.class);

    private final SimActivationService activationService;

    public SimCardController(SimActivationService activationService) {
        this.activationService = activationService;
    }

    @PostMapping("/activate")
    public ActivationResponse activateSim(@Valid @RequestBody ActivationRequest request) {
        ActivationResponse response = activationService.activate(request);
        log.info("Activation for iccid {} (customer {}): {}", request.getIccid(), request.getCustomerEmail(), response.isSuccess());
        return response;
    }

    @GetMapping("/activate")
    public ActivationStatusResponse getActivation(@RequestParam("simCardId") Long simCardId) {
        return activationService.findActivation(simCardId)
                .orElseThrow(() -> {
                    log.info("Activation record {} not found", simCardId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "SIM activation not found");
                });
    }
}
