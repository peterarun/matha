package com.matha.repository;

import com.matha.domain.Purchase;
import com.matha.domain.Sales;
import com.matha.domain.School;
import com.matha.sales.SalesApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.matha.util.UtilConstants.DELETED_IND;
import static org.junit.Assert.*;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("vazhakulam")
public class SalesRepositoryTest {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Test
    public void fetchNextSeqVal() {
    }

    @Test
    public void fetchNextSerialSeqVal() {
    }

    @Test
    public void findAllByTxnSchool() {
    }

    @Test
    public void findAllBySchoolAndStatusIndNot()
    {
        School school = schoolRepository.getOne(755);
        List<Sales> sales = salesRepository.findAllBySchoolExclStat(school, DELETED_IND);
//        List<Sales> sales = salesRepository.findAllBySchool(school);
        System.out.println(sales);
    }

    @Test
    public void test_findAllByIdLike()
    {
        int page = 0;
        int size = 10;
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "txnDate");
        String searchStr = "%";
        List<Sales> purList = salesRepository.findAllByIdLike(searchStr, pageable).getContent();
        for (int i = 0; i < purList.size(); i++)
        {
            System.out.println(purList.get(i));
        }
    }

    @Test
    public void findAllBySchoolAndFinancialYear() {
    }
}