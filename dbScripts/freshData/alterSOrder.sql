USE [Matha]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

ALTER TABLE [dbo].[SOrder] ADD NumSerialNo AS
CAST(
    LEFT([SerialNo],
        CASE PATINDEX('%[A-Z]%',[SerialNo]) WHEN 0 THEN LEN([SerialNo]) ELSE PATINDEX('%[A-Z]%',[SerialNo]) - 1 END
        ) AS INT
    ) ;

GO
