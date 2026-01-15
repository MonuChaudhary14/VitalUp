package org.vitalup.vitalup.entities.pushNotification;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vitalup.vitalup.entities.Auth.Users;

import java.util.UUID;

@Entity
@Table(
        name = "device_tokens",
        uniqueConstraints = @UniqueConstraint(columnNames = "token")
)
@Data
@NoArgsConstructor
public class DeviceToken {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false, length = 512, unique = true)
    private String token;

    @Column(nullable = false)
    private String platform;
}