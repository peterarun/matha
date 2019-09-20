USE [MathaDist]
GO

/****** Object:  Table [dbo].[Customer]    Script Date: 25-Aug-18 9:40:42 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Customer](
	[CustId] [nvarchar](10) NULL,
	[CName] [nvarchar](120) NULL,
	[Address1] [nvarchar](120) NULL,
	[Address2] [nvarchar](120) NULL,
	[Address3] [nvarchar](120) NULL,
	[Add4] [nvarchar](120) NULL,
	[Add5] [nvarchar](120) NULL,
	[PhNo1] [nvarchar](50) NULL,
	[PhNo2] [nvarchar](50) NULL,
	[Remarks] [nvarchar](120) NULL,
	[email] [nvarchar](120) NULL,
	[principal] [nvarchar](120) NULL,
	[district] [varchar](50) NULL,
	[state] [varchar](50) NULL,
	CONSTRAINT [IX_Customer_1] UNIQUE CLUSTERED 
	(
		[CName] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY],
	CONSTRAINT [IX_Customer] UNIQUE NONCLUSTERED 
	(
		[CustId] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO


