USE [Matha]
GO

/****** Object:  Table [dbo].[Sales]    Script Date: 25-Aug-18 11:26:20 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Sales](
	[SerialId] [nvarchar](15) NULL,
	[SerialNo] [decimal](18, 0) NULL,
	[Fy] [nvarchar](5) NULL,
	[TDate] [datetime] NULL,
	[CustId] [nvarchar](10) NULL,
	[Despatch] [nvarchar](120) NULL,
	[Documents] [nvarchar](30) NULL,
	[GRNo] [nvarchar](30) NULL,
	[Packages] [nvarchar](10) NULL,
	[SubTotal] [decimal](12, 2) NULL,
	[DAmt] [decimal](12, 2) NULL,
	[Others] [decimal](12, 2) NULL,
	[NetAmt] [decimal](12, 2) NULL,
	[Notes] [nvarchar](120) NULL,
	[TxnId] [int] NULL,
	[discType] [bit] NULL,
	[Status] [int] NULL,
	CONSTRAINT [IX_Sales] UNIQUE NONCLUSTERED 
	(
		[SerialId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY],
	CONSTRAINT [IX_Sales_1] UNIQUE NONCLUSTERED 
	(
		[SerialNo] ASC,
		[Fy] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Sales]  WITH CHECK ADD  CONSTRAINT [FK_Sales_Customer] FOREIGN KEY([CustId])
REFERENCES [dbo].[Customer] ([CustId])
GO

ALTER TABLE [dbo].[Sales] CHECK CONSTRAINT [FK_Sales_Customer]
GO

