package com.matha.repository;

import java.time.LocalDate;
import java.util.List;

import com.matha.domain.Purchase;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Publisher;
import com.matha.domain.PurchaseReturn;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturn, String>
{
	@Query("select payment from PurchaseReturn payment where payment.salesTxn.publisher = ?1 and (payment.statusInd is null or payment.statusInd <> -2)")
	public List<PurchaseReturn> findAllActiveByPublisher(Publisher school, Sort dateSort);

	@Query(value = "SELECT NEXT VALUE FOR PReturnSeq", nativeQuery = true)
	Long fetchNextSeqVal();

    List<PurchaseReturn> findAllByReturnDateAfter(LocalDate ld, Sort idSort);

	PurchaseReturn findByCreditNoteNumAndFy(String creditNoteNum, Integer fy);
}
