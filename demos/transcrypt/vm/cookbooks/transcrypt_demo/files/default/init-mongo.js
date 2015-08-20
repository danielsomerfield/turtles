
var adminUser = {user:"demo", pwd:"demo", roles:[{ role: "readWrite", db:"demo" }]};
if (db.getUser("demo") == null) {
  db.createUser(adminUser);
}
