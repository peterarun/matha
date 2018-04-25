USE [MathaNew]
GO

/****** Object:  Table [dbo].[STrans]    Script Date: 24-01-2018 09:00:08 ******/
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
	[ReturnId] [int] NULL,
	[Balance] [decimal](12,2) NOT NULL,
	[PrevTxnId] [int] UNIQUE,
	[NextTxnId] [int] UNIQUE
) ON [PRIMARY]
GO


