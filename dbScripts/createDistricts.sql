USE [MathaNew]
GO

/****** Object:  Table [dbo].[Districts]    Script Date: 06-02-2018 07:58:01 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Districts](
	[name] [varchar](50) NOT NULL,
	[state] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Districts] PRIMARY KEY CLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

INSERT INTO [dbo].[Districts]
           ([name]
           ,[state])
     VALUES
           ('Ernakulam','Kerala'),
           ('Idukki','Kerala');
GO
