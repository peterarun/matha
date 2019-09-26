USE [MathaDist]
GO

/****** Object:  Table [dbo].[CashBook]    Script Date: 24-Aug-18 7:02:22 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[CashBook](
	[Id] [int] NOT NULL,
	[TDate] [datetime] NULL,
	[Description] [nvarchar](100) NULL,
	[Amount] [decimal](12, 2) NOT NULL,
	[Type] [nvarchar](100) NULL,
	[Mode] [nvarchar](100) NULL,
	[Representative] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [IX_CashBook] UNIQUE NONCLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

