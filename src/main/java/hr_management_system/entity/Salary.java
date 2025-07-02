package hr_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double monthlyAmount;

    private Integer monthNumber;

    private boolean isPaid;

    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private Timestamp paidTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
