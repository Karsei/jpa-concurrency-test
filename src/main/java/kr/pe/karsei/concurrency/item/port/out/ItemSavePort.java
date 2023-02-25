package kr.pe.karsei.concurrency.item.port.out;

import kr.pe.karsei.concurrency.item.domain.Item;
import kr.pe.karsei.concurrency.item.entity.ItemJpaEntity;

public interface ItemSavePort {
    ItemJpaEntity save(Item item);
}
