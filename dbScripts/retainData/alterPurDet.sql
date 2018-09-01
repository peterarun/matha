USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table [dbo].[PurDet] add PurDetId INT IDENTITY(1,1);
GO

alter table [dbo].[PurDet] add OrderItemId int null;
GO

ALTER TABLE [dbo].[PurDet] ALTER COLUMN [BkNo] nvarchar(30);
GO

ALTER TABLE [dbo].[PurDet]  WITH CHECK ADD  CONSTRAINT [FK_PurDet_Book] FOREIGN KEY([BkNo])
REFERENCES [dbo].[Book] ([BookNo])
GO

ALTER TABLE [dbo].[PurDet] CHECK CONSTRAINT [FK_PurDet_Book]
GO


--declare @i int  = 1
--
--update PurDet
--set PurDetId  = @i , @i = @i + 1
--where PurDetId is null;

GO

ALTER TABLE [dbo].[PurDet] ALTER COLUMN [PurDetId] INT NOT NULL;
GO

ALTER TABLE [dbo].[PurDet] ADD CONSTRAINT PurDetId_PK PRIMARY KEY([PurDetId]);
GO
