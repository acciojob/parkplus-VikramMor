package com.driver.model;

import javax.persistence.*;

@Entity
@Table
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean PaymentCompleted;

    @Enumerated(value = EnumType.STRING)
    private PaymentMode paymentMode;

    @OneToOne
    @JoinColumn
    private Reservation reservation;

    public Payment() {
    }

    public Payment(boolean paymentCompleted, PaymentMode paymentMode) {
        PaymentCompleted = paymentCompleted;
        this.paymentMode = paymentMode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPaymentCompleted() {
        return PaymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        PaymentCompleted = paymentCompleted;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
