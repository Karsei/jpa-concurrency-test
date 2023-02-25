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

// https://velog.io/@znftm97/%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0-V3-%EB%B6%84%EC%82%B0-DB-%ED%99%98%EA%B2%BD%EC%97%90%EC%84%9C-%EB%B6%84%EC%82%B0-%EB%9D%BDDistributed-Lock-%ED%99%9C%EC%9A%A9
@SpringBootTest
class ItemServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemJpaRepository repository;

    @Test
    void testIfPessimisticLockTestCanBeSuccess() throws InterruptedException {
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
    void testIfRedisLockTestCanBeSuccess() throws InterruptedException {
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
}