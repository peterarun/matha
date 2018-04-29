USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

alter table SReturn add TxnId int null;
GO

ALTER TABLE [dbo].[SReturn] ALTER COLUMN [SerialId] INT NOT NULL;
GO

ALTER TABLE [dbo].[SReturn] ADD CONSTRAINT SReturn_PK PRIMARY KEY([SerialId]);
GO
