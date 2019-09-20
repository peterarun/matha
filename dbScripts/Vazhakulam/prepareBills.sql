set nocount on

SELECT [sl].[SerialId]
      ,[sl].[SerialNo]
      ,[sl].[TDate]
      ,[sl].[CustId]
      ,[Despatch]
      ,[Documents]
      ,[GRNo]
      ,[Packages]
      ,[SubTotal]
      ,[DPer]
      ,[DAmt]
      ,[Others]
      ,[sl].[Fy]
      ,[Notes]
      ,[sl].[NetAmt]
	  ,[sld].[BkNo]
	  ,[sld].[Qty]
	  ,[sld].[Rate]
	  ,[sld].[Amount]
	  ,[sld].[BookId]
	  ,[sld].[BkNo]
	  ,[sld].[SlNo]
  FROM [Matha].[dbo].[Sales] sl
  join [Matha].[dbo].[SalesDet] sld on sl.SerialId = sld.SerialId
  where [sl].TDate > '2018-06-05'
  order by sl.SerialId, sld.SlNo;