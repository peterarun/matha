USE [MathaNew]
GO

/****** Object:  Table [dbo].[STrans]    Script Date: 24-01-2018 09:00:08 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[STransactions](
	[SerialId] [int] PRIMARY KEY IDENTITY(1,1),
	[TxnDate] [date] NOT NULL,
	[Amount] [decimal](12,2) NOT NULL,
	[SchoolId] [varchar](15) NULL,
	[Note] [nvarchar](100) NULL,
	[SalesId] [varchar](15) NULL,
	[PaymentId] [int] NULL,
	[ReturnId] [int] NULL,
	[AddTime] [datetime] NULL,
    [ModTime] [datetime] NULL,
    [TxnId] varchar(15),
    [Balance] [decimal](12,2) NOT NULL,
    [PrevTxnId] [int],
    [NextTxnId] [int]
);
GO

CREATE UNIQUE NONCLUSTERED INDEX [Sch_NextTxn_Uniq] ON [dbo].[STransactions]
(
	[SchoolId] ASC,
	[NextTxnId] ASC
) ON [PRIMARY]
GO

CREATE UNIQUE NONCLUSTERED INDEX [Sch_Prev_Uniq] ON [dbo].[STransactions]
(
	[SchoolId] ASC,
	[PrevTxnId] ASC
) ON [PRIMARY]
GO
