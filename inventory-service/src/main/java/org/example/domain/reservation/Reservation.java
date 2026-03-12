package org.example.domain.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public void confirm(){
        status = ReservationStatus.CONFIRMED;
    }
    public void cancel(){
        status = ReservationStatus.CANCELLED;
    }
    public void expire(){
        status = ReservationStatus.EXPIRED;
    }
}
