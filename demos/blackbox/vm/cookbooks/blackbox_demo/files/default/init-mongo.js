
var adminUser = {user:"blackbox-demo", pwd:"blackbox-demo", roles:[{ role: "readWrite", db:"blackbox-demo" }]};
if (db.getUser("blackbox-demo") == null) {
  db.createUser(adminUser);
}
