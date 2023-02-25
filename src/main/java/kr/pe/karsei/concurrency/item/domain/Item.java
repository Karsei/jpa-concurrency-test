package kr.pe.karsei.concurrency.item.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Item {
    private Long seqNo;
    private String name;
    private Integer stock;

    public Item decreaseStock() {
        if (this.stock > 0) this.stock = this.stock - 1;
        return this;
    }
}
