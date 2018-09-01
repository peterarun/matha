USE [Matha]
GO

/****** Object:  Table [dbo].[Journal]    Script Date: 14-01-2018 15:04:04 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[CashBook](
	[Id] [int] IDENTITY PRIMARY KEY,
	[TDate] [datetime] NULL,
	[Description] [nvarchar](100) NULL,
	[Amount] [decimal](12,2) NOT NULL,
	[Type] [nvarchar](100) NULL,
	[Mode] [nvarchar](100) NULL,
 CONSTRAINT [IX_CashBook] UNIQUE NONCLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO


