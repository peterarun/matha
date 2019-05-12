USE [MathaDist]
GO

/****** Object:  Table [dbo].[PReturn]    Script Date: 25-Aug-18 9:46:25 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[PReturn]
(
	[SerialId] [nvarchar](15) NOT NULL,
	[TDate] [datetime] NULL,
	[SubTotal] [decimal](12, 2) NULL,
	[Discount] [decimal](12, 2) NULL,
	[Fy] [nvarchar](4) NULL,
	[TxnId] [int] NULL,
	[CreditNoteNum] [nvarchar](30) NULL,
	[PublisherId] [nvarchar](10) NULL,
	[discType] [bit] NULL,
	[Status] [int] NULL,
	CONSTRAINT [PReturn_PK] PRIMARY KEY CLUSTERED 
	(
		[SerialId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
	CONSTRAINT [IX_PReturn_1] UNIQUE NONCLUSTERED 
	(
		[CreditNoteNum],[Fy] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO


