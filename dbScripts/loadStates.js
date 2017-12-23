var conn = new Mongo();
var db = conn.getDB("mydatabase");

db.state.insertOne({name: "Kerala", districts: ["Ernakulam", "Idukki"]});
db.state.insertOne({name: "Tamil Nadu", districts: ["Chennai", "Madhurai", "Trichi"]});

