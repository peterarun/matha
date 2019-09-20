USE [MathaDist]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

update [MathaDist].[dbo].[Customer]
  set [state] = 'TAMIL NADU'
  where [state] is null;

  GO
  