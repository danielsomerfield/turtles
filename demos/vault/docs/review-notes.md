# Hashicorp Vault

## Summary
Vault is a fundamentally a tool for controlling access to resources via policies. It does not (cannot) solve the bootstrapping problem insofar as it still requires authentication (of course). But it does provide a means of decoupling control of access to resource (authz) from the identity of the requestor (authc) via a clean CLI and API for a number of use cases. I think the real power of this tool will come when it's more mature, has a lot more backends and a stable API so you can build your own. Although the methods of access are quite straightforward, the product does not provide any abstraction (like a virtual filesystem) so applications could access resources in a very familiar way--whether this is a strength or weakness.

The key feature to Vault is leases. It is assumed for Vault deployments that a secret is only valid for so long. Obviously this doesn't handle a case where you want to send out a secret and "recall it" (think outlook "recall message" and how well that works) but rather it's for things like keys and credentials that have a natural lifespan, after which they can be changed. If credentials are always fetched from your vault server based on lease time, you can be guaranteed that those credentials are always the latest and greatest and the app (or user) never has to worry about what those credentials actually are. This is very similar to how AWS handles granting access to AWS resources via AIM roles. Admins grant roles, and AWS handles the mediation of credentials and the like.

## Ratings (poor / fair / good)
- Ease of setup:
- Easy of use:
- Cloud readiness: good
- Data center readiness: good
- Automation / pipeline readiness: fair
- Product maturity: fair
- Developer friendliness: fair

## Features
- Cluster / multi-node support: yes
- Horizontal scaling support: yes (via additional backends)
- Secret expiration: yes

- Access Methods:
  - REST API
  - CLI
- Integrations:
  - Secret:
    - AWS
    - Consul
    - PostgreSQL
    - MySQL
  - Auth:
    - github
    - LDAP
  - Audit:
    - syslog
  - Stats:
    - [Statsite](https://github.com/armon/statsite)

## How does it attempt to solve the bootstrapping problem?
For machines to authentication, Vault offers the [App Id Secret Backend](https://www.vaultproject.io/docs/auth/app-id.html). According to the docs:

  The App ID auth backend is a mechanism for machines to authenticate with Vault. It works by requiring two hard-to-guess unique pieces of information: a unique app ID, and a unique user ID.

These two bits of information are mapped, so that when the request comes in with both, identity is considered established. This obviously involves a little bit of manual intervention but it does serve to extract the process of establishing identity from configuration and policy management.

## Notes
- Vault has a master key. It supports multiple master keys that are required to "unseal" the vault. Until the vault is unsealed, secrets cannot be decrypted. This is done via Shamir secret sharing that shards the key.
- All secrets in Vault are required to have a lease
- AWS Secret Backend
- From the Vault website:
  - "Vault doesn't replace an HSM" -- better for dynamic secrets and less operationally cumbersome, but HSM would be an appropriate backend for Vault.
  - Vault and KMS differ in the scope of problems they are trying to solve. The KMS service is focused on securely storing encryption keys and supporting cryptographic operations (encrypt and decrypt) using those keys. It supports access controls and auditing as well.
