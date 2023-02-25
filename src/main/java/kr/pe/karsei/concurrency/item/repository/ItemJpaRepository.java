package kr.pe.karsei.concurrency.item.repository;

import kr.pe.karsei.concurrency.item.entity.ItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemJpaRepository extends JpaRepository<ItemJpaEntity, Long> {
}
