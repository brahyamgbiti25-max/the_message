package by.Brahyam.Garcia.Blas.the.message.Entity;

import by.Brahyam.Garcia.Blas.the.message.Enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//well now I understood why I must use setter and getter in entities
//is because you can created a infinite loop with data that brings the hascode(I don't know what it is yet)
@Getter
@Setter
@Table(name = "Users")
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = true)
    private String profilePictureUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role=Role.USER;

    @Column(nullable = false)
    private boolean active=true;
    @Column
    private boolean emailVerified=false;
    @Column(unique = true)
    private String verificationToken;
    @Column
    private Instant verificationTokenExpiry;
    @Column(unique = true)
    private String passwordResetToken;
    @Column(unique = true)
    private Instant passwordResetTokenExpiry;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    //I was understanding but already I know that when you put a list in when you have many obeject that belongd
    //just to one owner.
    @OneToMany(mappedBy = "users")
    private List<Message> message;
    @ManyToMany
    @JoinTable(
            name = "user_chat_rooms",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_rooms")
    )
    private Set<ChatRooms> chatRooms=new HashSet<>();
}
