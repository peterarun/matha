USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

update [dbo].[STransactions]
set NextTxnId = null
  where NextTxnId not in (select SerialId from [dbo].[STransactions])

GO
  