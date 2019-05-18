USE [MathaDist]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

ALTER TABLE [dbo].[SReturn]  DROP CONSTRAINT [FK_SReturn_Customer]
GO

ALTER TABLE [dbo].[SPayment]  DROP CONSTRAINT [FK_SPayment_Customer]
GO

ALTER TABLE [dbo].[SOrder]  DROP CONSTRAINT [FK_SOrder_Customer]
GO

ALTER TABLE [dbo].[Sales]  DROP CONSTRAINT [FK_Sales_Customer]
GO

ALTER TABLE [dbo].[Customer]  DROP CONSTRAINT [IX_Customer]
GO
