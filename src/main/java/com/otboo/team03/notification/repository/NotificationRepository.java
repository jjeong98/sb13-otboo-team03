package com.otboo.team03.notification.repository;

import com.otboo.team03.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("""
            select n
            from Notification n
            where n.receiver.id = :receiverId
              and (
                    :cursor is null
                    or n.createdAt < :cursor
                    or (
                        n.createdAt = :cursor
                        and :idAfter is not null
                        and n.id < :idAfter
                    )
              )
            order by n.createdAt desc, n.id desc
            """)
    List<Notification> findPageByReceiverId(
            @Param("receiverId") UUID receiverId,
            @Param("cursor") Instant cursor,
            @Param("idAfter") UUID idAfter,
            Pageable pageable
    );

    long countByReceiver_Id(UUID receiverId);

    Optional<Notification> findByIdAndReceiver_Id(UUID notificationId, UUID receiverId);
}
