USE [MathaDist]
GO

/****** Object:  Table [dbo].[Supplier]    Script Date: 25-Aug-18 12:13:19 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Supplier](
	[PublisherId] [int] NOT NULL,
	[SName] [nvarchar](120) NULL,
	[Address1] [nvarchar](120) NULL,
	[Address2] [nvarchar](120) NULL,
	[Address3] [nvarchar](120) NULL,
	[PhNo1] [nvarchar](50) NULL,
	[PhNo2] [nvarchar](50) NULL,
	[Remarks] [nvarchar](120) NULL,
	[GSTIN] [varchar](30) NULL,
	[Email] [nvarchar](120) NULL,
	[PIN] [nvarchar](12) NULL,
	PRIMARY KEY CLUSTERED 
	(
		[PublisherId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
	CONSTRAINT [IX_Supplier_1] UNIQUE NONCLUSTERED 
	(
		[SName] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO


