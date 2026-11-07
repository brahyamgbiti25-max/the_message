package by.Brahyam.Garcia.Blas.the.message.Entity;

import by.Brahyam.Garcia.Blas.the.message.Enums.TypeMessage;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Data
@Getter
@Setter
@Table(name = "ChatRooms")
@Entity
public class ChatRooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMessage typeMessage;
    @Column
    private String roomName;
    @Column(nullable = true)
    private String displayImageUrl;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Instant createdAt;
    @ManyToMany(mappedBy = "chatRooms")
    private Set<User> users=new HashSet<>();
    @OneToMany(mappedBy = "chatRoom")
    private List<Message>messages=new ArrayList<>();
}
