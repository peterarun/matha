CREATE TABLE [dbo].[DeletedSTransactions](
	[SerialId] [int] IDENTITY(1,1) NOT NULL,
	[TxnDate] [date] NOT NULL,
	[Amount] [decimal](12, 2) NOT NULL,
	[SchoolId] [varchar](15) NULL,
	[Note] [nvarchar](100) NULL,
	[SalesId] [varchar](15) NULL,
	[PaymentId] [int] NULL,
	[ReturnId] [nvarchar](10) NULL,
	[AddTime] [datetime] NULL,
	[ModTime] [datetime] NULL,
	[Balance] [decimal](12, 2) NOT NULL,
	[PrevTxnId] [int] NULL,
	[NextTxnId] [int] NULL,
PRIMARY KEY CLUSTERED
(
	[SerialId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
