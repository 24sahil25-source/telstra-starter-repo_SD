package au.com.telstra.simcardactivator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        ActivationResponse response = activationService.activate(request.getIccid());
        log.info("Activation for iccid {} (customer {}): {}", request.getIccid(), request.getCustomerEmail(), response.isSuccess());
        return response;
    }
}
