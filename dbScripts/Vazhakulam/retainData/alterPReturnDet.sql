USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table PReturnDet add PReturnDetId INT IDENTITY(1,1);
GO

--ALTER TABLE [dbo].[PReturnDet]  WITH CHECK ADD  CONSTRAINT [FK_PReturnDet_Book] FOREIGN KEY([BkNo])
--REFERENCES [dbo].[Book] ([BookNo])
--GO
--
--ALTER TABLE [dbo].[PReturnDet] CHECK CONSTRAINT [FK_PReturnDet_Book]
--GO


--declare @i int  = 1
--
--update PReturnDet
--set PReturnDetId  = @i , @i = @i + 1
--where PReturnDetId is null;

--GO

ALTER TABLE [dbo].[PReturnDet] ALTER COLUMN [PReturnDetId] INT NOT NULL;
GO

ALTER TABLE [dbo].[PReturnDet] ADD CONSTRAINT PReturnDet_PK PRIMARY KEY([PReturnDetId]);
GO

