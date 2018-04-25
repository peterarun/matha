USE [MathaNew]
GO

/****** Object:  Table [dbo].[STrans]    Script Date: 24-01-2018 09:00:08 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
  
alter TABLE [dbo].[STransactions]
add	[AddTime] [datetime] NULL,
	[ModTime] [datetime] NULL,
	[TxnId] varchar(15),
	[Balance] [decimal](12,2) NOT NULL,
	[PrevTxnId] [int],
	[NextTxnId] [int];
GO

CREATE UNIQUE NONCLUSTERED INDEX [Sch_NextTxn_Uniq] ON [dbo].[STransactions]
(
	[SchoolId] ASC,
	[NextTxnId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

CREATE UNIQUE NONCLUSTERED INDEX [Sch_Prev_Uniq] ON [dbo].[STransactions]
(
	[SchoolId] ASC,
	[PrevTxnId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

