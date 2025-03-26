package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotBlank
    @Size(min = 4, message = "Payment method must be at least 4 characters")
    private String paymentMethod;

    private String pgPaymentId; // Id de paiement de la passerelle de paiement (RazorPay, Paypal, etc..)

    private String pgName;

    private String pgStatus;

    private String pgResponseMessage;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Order order;

    public Payment(Long paymentId, String pgPaymentId, String pgName, String pgStatus, String pgResponseMessage) {
        this.paymentId = paymentId;
        this.pgPaymentId = pgPaymentId;
        this.pgName = pgName;
        this.pgStatus = pgStatus;
        this.pgResponseMessage = pgResponseMessage;
    }
}
