USE [MathaDist]
GO

/****** Object:  Table [dbo].[Addresses]    Script Date: 24-Aug-18 6:56:50 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Addresses](
	[Name] [nvarchar](120) NOT NULL,
	[address1] [nvarchar](120) NULL,
	[address2] [nvarchar](120) NULL,
	[address3] [nvarchar](120) NULL,
	[phone1] [nvarchar](50) NULL,
	[phone2] [nvarchar](50) NULL,
	[Email] [nvarchar](120) NULL,
	[PIN] [nvarchar](12) NULL,
PRIMARY KEY CLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO


