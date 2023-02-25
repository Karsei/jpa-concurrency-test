package kr.pe.karsei.concurrency.item.port.out;

import kr.pe.karsei.concurrency.item.domain.Item;

public interface ItemLoadPort {
    Item findById(Long seqNo);
    Item findByIdWithLock(Long seqNo);
}
