package kr.pe.karsei.concurrency.item.mapper;

import kr.pe.karsei.concurrency.item.domain.Item;
import kr.pe.karsei.concurrency.item.entity.ItemJpaEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ItemMapper {
    public static Item mapItemPessimisticJpaToPessimistic(ItemJpaEntity entity) {
        return new Item(entity.getSeqNo(), entity.getName(), entity.getStock());
    }

    public static ItemJpaEntity mapPessimisticToItemPessimisticJpa(Item item) {
        return new ItemJpaEntity(item.getSeqNo(), item.getName(), item.getStock());
    }
}
