USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

ALTER TABLE [dbo].[SOrderDet] ADD OrderDetId INT IDENTITY(1,1);
GO

ALTER TABLE [dbo].[SOrderDet] ADD BookPrice decimal(8,2) null;
GO

ALTER TABLE [dbo].[SOrderDet] ALTER COLUMN [OrderDetId] INT NOT NULL;
GO

ALTER TABLE [dbo].[SOrderDet] ADD CONSTRAINT SOrderDet_UK PRIMARY KEY([OrderDetId]);
GO

ALTER TABLE [dbo].[SOrderDet] WITH CHECK ADD  CONSTRAINT [FK_SOrderDet_Book] FOREIGN KEY([BkNo])
REFERENCES [dbo].[Book] ([BookNo])
GO

ALTER TABLE [dbo].[SOrderDet] CHECK CONSTRAINT [FK_SOrderDet_Book]
GO
