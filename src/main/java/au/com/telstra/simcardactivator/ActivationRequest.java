package au.com.telstra.simcardactivator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ActivationRequest {

    @NotBlank
    private String iccid;

    @NotBlank
    @Email
    private String customerEmail;

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}

