package kr.pe.karsei.concurrency.item.port.out;

import kr.pe.karsei.concurrency.item.domain.Item;

public interface ItemSavePort {
    Item save(Item item);
}
