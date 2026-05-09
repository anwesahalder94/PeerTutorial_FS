package com.tutoring.tutorservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PayoutRequest {

    @NotNull
    private Long tutorId;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String paymentMethod;

    private String notes;

    public Long getTutorId() { return tutorId; }
    public void setTutorId(Long tutorId) { this.tutorId = tutorId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
