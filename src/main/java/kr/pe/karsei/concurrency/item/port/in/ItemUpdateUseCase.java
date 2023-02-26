package kr.pe.karsei.concurrency.item.port.in;

import kr.pe.karsei.concurrency.item.domain.Item;

public interface ItemUpdateUseCase {
    Item updateWithRedisLock(Long seqNo);
    Item updateWithPessimisticLock(Long seqNo);
    Item updateWithZookeeperLock(Long seqNo);
}
