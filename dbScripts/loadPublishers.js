load("connectDb.js");

var pubArr = [{_id: 45, name: "PUBLISHER"},
{_id: 48, name: "MAYUR PUBLICATIONS"},
{_id: 55, name: "DEEPU PRAKASHAN"},
{_id: 56, name: "RAINBOW PUBLISHERS"},
{_id: 57, name: "P.C.M"},
{_id: 60, name: "MATHA"}];

db.publisher.insertMany(pubArr);
