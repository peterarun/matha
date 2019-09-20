USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

ALTER TABLE [dbo].[Customer] ALTER COLUMN CustId INT NOT NULL;
GO

ALTER TABLE [dbo].[Customer] ADD PRIMARY KEY(CustId);
GO

ALTER TABLE [dbo].[SReturn]  WITH CHECK ADD  CONSTRAINT [FK_SReturn_Customer] FOREIGN KEY([CustId])
REFERENCES [dbo].[Customer] ([CustId])
GO

ALTER TABLE [dbo].[SReturn] CHECK CONSTRAINT [FK_SReturn_Customer]
GO

ALTER TABLE [dbo].[SPayment]  WITH CHECK ADD  CONSTRAINT [FK_SPayment_Customer] FOREIGN KEY([CustId])
REFERENCES [dbo].[Customer] ([CustId])
GO

ALTER TABLE [dbo].[SPayment] CHECK CONSTRAINT [FK_SPayment_Customer]
GO

ALTER TABLE [dbo].[SOrder]  WITH CHECK ADD  CONSTRAINT [FK_SOrder_Customer] FOREIGN KEY([CustId])
REFERENCES [dbo].[Customer] ([CustId])
GO

ALTER TABLE [dbo].[SOrder] CHECK CONSTRAINT [FK_SOrder_Customer]
GO

ALTER TABLE [dbo].[Sales]  WITH CHECK ADD  CONSTRAINT [FK_Sales_Customer] FOREIGN KEY([CustId])
REFERENCES [dbo].[Customer] ([CustId])
GO

ALTER TABLE [dbo].[Sales] CHECK CONSTRAINT [FK_Sales_Customer]
GO
