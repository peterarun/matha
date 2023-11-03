USE [MathaBooks]
GO

/****** Object:  Table [dbo].[SAdjustment]    Script Date: 01/11/2023 20:27:34 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[SAdjustment](
	[SerialId] [int] IDENTITY(1,1) NOT NULL,
	[CustId] [int] NULL,
	[Type] [nvarchar](10) NULL,
	[Dated] [datetime] NULL,
	[TxnId] [int] NULL,
	[Status] [int] NULL,
	[DelAmt] [decimal](12, 2) NULL
PRIMARY KEY CLUSTERED
(
	[SerialId] ASC
)
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[SAdjustment]  WITH CHECK ADD  CONSTRAINT [FK_SAdjustment_Customer] FOREIGN KEY([CustId])
REFERENCES [dbo].[Customer] ([CustId])
GO

ALTER TABLE [dbo].[SAdjustment] CHECK CONSTRAINT [FK_SAdjustment_Customer]
GO
