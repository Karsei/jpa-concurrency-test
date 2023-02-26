package kr.pe.karsei.concurrency.item.domain;

import kr.pe.karsei.concurrency.config.DistributedLock;
import kr.pe.karsei.concurrency.config.DistributedLockCategory;
import kr.pe.karsei.concurrency.item.port.in.ItemUpdateUseCase;
import kr.pe.karsei.concurrency.item.port.out.ItemLoadPort;
import kr.pe.karsei.concurrency.item.port.out.ItemSavePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemUpdateUseCase {
    private final ItemLoadPort itemLoadPort;
    private final ItemSavePort itemSavePort;

    @DistributedLock(key = "#seqNo")
    @Override
    public Item updateWithRedisLock(Long seqNo) {
        Item item = itemLoadPort.findById(seqNo);
        return decreaseStockAndSave(item);
    }

    @Transactional
    @Override
    public Item updateWithPessimisticLock(Long seqNo) {
        Item item = itemLoadPort.findByIdWithLock(seqNo);
        return decreaseStockAndSave(item);
    }

    @DistributedLock(key = "#seqNo", category = DistributedLockCategory.ZOOKEEPER)
    @Override
    public Item updateWithZookeeperLock(Long seqNo) {
        Item item = itemLoadPort.findById(seqNo);
        return decreaseStockAndSave(item);
    }

    private Item decreaseStockAndSave(Item item) {
        item.decreaseStock();
        return itemSavePort.save(item);
    }
}
