USE [MathaDist]
GO

alter table [dbo].[Book]
add CONSTRAINT [IX_Book_UQ_1] UNIQUE NONCLUSTERED 
	(
		[BookNo], [BName] ASC
	)
	WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY];

GO

