USE [MathaDist]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

update [dbo].[PPayment]
set TxnId = null, Status = -2
  where TxnId not in (select SerialId from PTransactions)

update [dbo].[PTransactions]
set PrevTxnId = null
  where PrevTxnId not in (select SerialId from PTransactions)

GO
