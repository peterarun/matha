USE [MathaDist]
GO

INSERT INTO [dbo].[State]
           ([name])
SELECT [name]
  FROM [Matha_Old].[dbo].[State]

GO
