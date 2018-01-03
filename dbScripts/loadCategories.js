load("connectDb.js");

var catArr = [{_id: 43, name: "CATEGORY"},
{_id: 49, name: "WRITING"},
{_id: 51, name: "COURSE BOOK"},
{_id: 52, name: "DRAWING"},
{_id: 53, name: "MORAL SCIENCE"},
{_id: 54, name: "GENERAL KNOWLEDGE"},
{_id: 59, name: "COMPUTER"},
{_id: 62, name: "HINDI"}];

db.bookCategory.insertMany(catArr);
