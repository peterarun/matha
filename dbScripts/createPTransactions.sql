USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[PTransactions](
	[SerialId] [int] PRIMARY KEY IDENTITY(1,1),
	[AddTime] [datetime] NOT NULL,
	[ModTime] [datetime] NOT NULL,
	[TxnDate] [date] NOT NULL,
	[Amount] [decimal](12,2) NOT NULL,
	[PublisherId] [varchar](15) NULL,
	[Note] [nvarchar](100) NULL,
	[PurchaseId] [varchar](15) NULL,
	[PaymentId] [int] NULL,
	[ReturnId] [nvarchar](15) NULL,
	[Balance] [decimal](12,2) NOT NULL,
	[PrevTxnId] [int] UNIQUE,
	[NextTxnId] [int] UNIQUE
) ON [PRIMARY]
GO

CREATE UNIQUE NONCLUSTERED INDEX [Pub_NextTxn_Uniq] ON [dbo].[PTransactions]
(
	[PublisherId] ASC,
	[NextTxnId] ASC
) ON [PRIMARY]
GO

CREATE UNIQUE NONCLUSTERED INDEX [Pub_Prev_Uniq] ON [dbo].[PTransactions]
(
	[PublisherId] ASC,
	[PrevTxnId] ASC
) ON [PRIMARY]
GO



