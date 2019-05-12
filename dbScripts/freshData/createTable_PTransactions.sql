USE [MathaDist]
GO

/****** Object:  Table [dbo].[PTransactions]    Script Date: 25-Aug-18 11:03:19 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[PTransactions](
	[SerialId] [int] IDENTITY(1,1) NOT NULL,
	[AddTime] [datetime] NOT NULL,
	[ModTime] [datetime] NOT NULL,
	[TxnDate] [date] NOT NULL,
	[Amount] [decimal](12, 2) NOT NULL,
	[PublisherId] [varchar](15) NULL,
	[Note] [nvarchar](100) NULL,
	[PurchaseId] [varchar](15) NULL,
	[PaymentId] [int] NULL,
	[ReturnId] [nvarchar](15) NULL,
	[Balance] [decimal](12, 2) NOT NULL,
	[PrevTxnId] [int] NULL,
	[NextTxnId] [int] NULL,
	PRIMARY KEY CLUSTERED 
	(
		[SerialId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO


