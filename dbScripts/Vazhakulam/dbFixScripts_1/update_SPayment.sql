USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

update [Matha].[dbo].[SPayment]
  set [SPayment].[CustId] = (select sTrans.[SchoolId] from [STransactions] sTrans
  where sTrans.SerialId = TxnId);

  GO
  