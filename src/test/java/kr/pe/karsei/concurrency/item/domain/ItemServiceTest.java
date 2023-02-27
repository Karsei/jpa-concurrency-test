package kr.pe.karsei.concurrency.item.domain;

import kr.pe.karsei.concurrency.item.entity.ItemJpaEntity;
import kr.pe.karsei.concurrency.item.repository.ItemJpaRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ItemServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemJpaRepository repository;

    @Test
    void testIfPessimisticLockCanBeSuccess() throws InterruptedException {
        // given
        final int PEOPLE_COUNT = 100;
        final int THREAD_COUNT = 10;
        final long SEQNO = 1L;

        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(PEOPLE_COUNT);
        ItemJpaEntity beforeItem = repository.findById(SEQNO)
                .orElseThrow(() -> new IllegalStateException("테스트 오류"));

        final int BEFORE_STOCK_COUNT = beforeItem.getStock();

        // when
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < PEOPLE_COUNT; i++) {
            service.execute(() -> {
                try {
                    itemService.updateWithPessimisticLock(SEQNO);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                latch.countDown();
            });
        }
        latch.await();

        long estimated = System.currentTimeMillis() - startTime;
        logger.info("estimated: {}ms", estimated);
        Optional<ItemJpaEntity> afterItem = repository.findById(SEQNO);

        // then
        assertThat(afterItem).isPresent();
        assertThat(afterItem.get().getStock()).isEqualTo(BEFORE_STOCK_COUNT - PEOPLE_COUNT);
        logger.info("Before: {}, After: {}", BEFORE_STOCK_COUNT, afterItem.get().getStock());
    }

    @Test
    void testIfRedisLockCanBeSuccess() throws InterruptedException {
        // given
        final int PEOPLE_COUNT = 100;
        final int THREAD_COUNT = 10;
        final long SEQNO = 1L;

        CountDownLatch latch = new CountDownLatch(PEOPLE_COUNT);
        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        ItemJpaEntity beforeItem = repository.findById(SEQNO)
                .orElseThrow(() -> new IllegalStateException("테스트 오류"));

        final int BEFORE_STOCK_COUNT = beforeItem.getStock();

        // when
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < PEOPLE_COUNT; i++) {
            service.execute(() -> {
                try {
                    itemService.updateWithRedisLock(SEQNO);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                latch.countDown();
            });
        }
        latch.await();

        long estimated = System.currentTimeMillis() - startTime;
        logger.info("estimated: {}ms", estimated);
        Optional<ItemJpaEntity> afterItem = repository.findById(SEQNO);

        // then
        assertThat(afterItem).isPresent();
        assertThat(afterItem.get().getStock()).isEqualTo(BEFORE_STOCK_COUNT - PEOPLE_COUNT);
        logger.info("Before: {}, After: {}", BEFORE_STOCK_COUNT, afterItem.get().getStock());
    }

    @Test
    void testIfZookeeperLockCanBeSuccess() throws InterruptedException {
        // given
        final int PEOPLE_COUNT = 100;
        final int THREAD_COUNT = 10;
        final long SEQNO = 1L;

        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(PEOPLE_COUNT);
        ItemJpaEntity beforeItem = repository.findById(SEQNO)
                .orElseThrow(() -> new IllegalStateException("테스트 오류"));

        final int BEFORE_STOCK_COUNT = beforeItem.getStock();

        // when
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < PEOPLE_COUNT; i++) {
            service.execute(() -> {
                try {
                    itemService.updateWithZookeeperLock(SEQNO);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                latch.countDown();
            });
        }
        latch.await();

        long estimated = System.currentTimeMillis() - startTime;
        logger.info("estimated: {}ms", estimated);
        Optional<ItemJpaEntity> afterItem = repository.findById(SEQNO);

        // then
        assertThat(afterItem).isPresent();
        assertThat(afterItem.get().getStock()).isEqualTo(BEFORE_STOCK_COUNT - PEOPLE_COUNT);
        logger.info("Before: {}, After: {}", BEFORE_STOCK_COUNT, afterItem.get().getStock());
    }
}