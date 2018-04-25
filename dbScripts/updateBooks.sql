USE [MathaNew]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

update [MathaNew].[dbo].[Book] 
  set PubId = 55
  where PubId = 45;
  
GO
