package com.matha.repository;

import com.matha.domain.CashBook;
import com.matha.sales.SalesApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SalesApplication.class)
public class CashBookRepositoryTest
{
    private static final Logger LOGGER = LogManager.getLogger(CashBookRepositoryTest.class);

    @Autowired
    private CashBookRepository cashBookRepository;

    @Test
    public void fetchCashBookRecords()
    {
//        Sort idSort = new Sort("id");
        List<CashBook> cbRecs = cashBookRepository.fetchCashBookRecords(LocalDate.now().minusMonths(2), LocalDate.now(), "T");
        for (CashBook cbRec : cbRecs) {
            LOGGER.info(cbRec);
        }
    }
}