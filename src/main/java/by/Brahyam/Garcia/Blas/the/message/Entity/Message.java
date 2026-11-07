package by.Brahyam.Garcia.Blas.the.message.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;
    @CreationTimestamp
    @Column(nullable = false)
    private Instant sendAt;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User users;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRooms chatRoom;

}
