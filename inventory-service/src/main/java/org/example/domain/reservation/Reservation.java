package org.example.domain.reservation;

import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
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
