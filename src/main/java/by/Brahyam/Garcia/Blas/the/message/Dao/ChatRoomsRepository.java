package by.Brahyam.Garcia.Blas.the.message.Dao;

import by.Brahyam.Garcia.Blas.the.message.Entity.ChatRooms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomsRepository extends JpaRepository<ChatRooms,Long>{
    Page<ChatRooms> findByUsers_Email(String email, Pageable pageable);
}
