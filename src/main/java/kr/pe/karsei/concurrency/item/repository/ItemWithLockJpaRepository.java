package kr.pe.karsei.concurrency.item.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import kr.pe.karsei.concurrency.item.entity.ItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface ItemWithLockJpaRepository extends JpaRepository<ItemJpaEntity, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    Optional<ItemJpaEntity> findBySeqNo(Long seqNo);
}
