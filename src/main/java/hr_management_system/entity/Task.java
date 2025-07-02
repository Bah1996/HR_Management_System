package hr_management_system.entity;

import hr_management_system.entity.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String taskName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Timestamp deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @ManyToOne
    private User fromUser;

    @ManyToOne
    private User toUser;
}
