USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table SOrderDet add BookPrice decimal(8,2) null;
GO

alter table SOrderDet add ReturnId int null;
GO

alter table SOrderDet add PurReturnId int null;
GO

ALTER TABLE [dbo].[SOrderDet]  WITH CHECK ADD  CONSTRAINT [FK_SOrderDet_Book] FOREIGN KEY([BkNo])
REFERENCES [dbo].[Book] ([BookNo])
GO

ALTER TABLE [dbo].[SOrderDet] CHECK CONSTRAINT [FK_SOrderDet_Book]
GO

