package au.com.telstra.simcardactivator;

public class ActivationStatusResponse {

    private final String iccid;
    private final String customerEmail;
    private final boolean active;

    public ActivationStatusResponse(String iccid, String customerEmail, boolean active) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
        this.active = active;
    }

    public String getIccid() {
        return iccid;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public boolean isActive() {
        return active;
    }
}
