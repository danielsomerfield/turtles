
var adminUser = {user:"vault-demo", pwd:"vault-demo", roles:[{ role: "readWrite", db:"vault-demo" }]};
if (db.getUser("vault-demo") == null) {
  db.createUser(adminUser);
}
