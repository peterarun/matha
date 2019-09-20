USE [MathaDist]
GO

/****** Object:  Table [dbo].[SReturn]    Script Date: 25-Aug-18 12:00:51 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SReturn](
	[SerialId] [nvarchar](10) NOT NULL,
	[TDate] [datetime] NULL,
	[CustId] [nvarchar](10) NULL,
	[SubTotal] [decimal](12, 2) NULL,
	[DAmt] [decimal](12, 2) NULL,
	[discType] [bit] NULL,
	[Others] [decimal](12, 2) NULL,
	[NetAmt] [decimal](12, 2) NULL,
	[CreditNoteNum] [nvarchar](10) NULL,
	[Fy] [nvarchar](5) NULL,
	[Notes] [nvarchar](120) NULL,
	[TxnId] [int] NULL,
	[Status] [int] NULL,
	CONSTRAINT [SReturn_PK] PRIMARY KEY CLUSTERED 
	(
		[SerialId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
	CONSTRAINT [IX_SReturn_1] UNIQUE NONCLUSTERED 
	(
		[CreditNoteNum], [Fy] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[SReturn]  WITH CHECK ADD  CONSTRAINT [FK_SReturn_Customer] FOREIGN KEY([CustId])
REFERENCES [dbo].[Customer] ([CustId])
GO

ALTER TABLE [dbo].[SReturn] CHECK CONSTRAINT [FK_SReturn_Customer]
GO
