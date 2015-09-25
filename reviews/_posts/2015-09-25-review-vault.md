---
layout: review
title: Vault
category: review
---
## Review tl;dr
Vault is the runaway product in the secret-service open-source world. Vault isn't entirely mature as a product but it's definitely worth a look if you have sophisticated security needs and want to treat your secrets as ephemeral.

## Product Summary
Vault is a fundamentally a tool for controlling access to resources via policies. It provides a means of decoupling control of access to resource (authz) from the identity of the requestor (authc) via a clean CLI and API for a number of use cases.

The key feature to Vault is leases. It is assumed for Vault deployments that a secret is only valid for so long. Obviously this doesn't handle a case where you want to send out a secret and "recall it" (think outlook "recall message" and how well that works) but rather it's for things like keys and credentials that have a natural lifespan, after which they can be changed. If credentials are always fetched from your vault server based on lease time, you can be guaranteed (unless there is an explicit expiration) that those credentials are always the latest and greatest and the app (or user) never has to worry about what those credentials actually are.

The interactions with HTTP API are pretty involved and have a pretty fair number of interactions. The logic you would need to build into your app to utilize the secrets would require a fair amount of code and the first thing you are going to want to do is develop a library in your native language to handle that interaction as transparently as possible.

## Evaluation
### Strengths
- REST API is pretty easy to use
- Supports a number of secret and authentication backends
- Good built-in help
- Secret lease model encourages a strong key rotation strategy
- Compelling model for ephemeral credentials that can integrate with AWS roles
- Several very interesting useful, including AWS, mysql, PostgreSQL and Consul
- Auditing support at the application level
- Community is responsive, including the devs

### Weaknesses
- Comparatively high cost of entry compared to other tools and strategies.
- API docs are incomplete and at times, contradictory
- Inconsistent REST return code semantics (e.g. returning 400s for things that really should be 503s)
- Relative product immaturity means that API is not yet solidified, so building your own backends would be a tricky endeavor and you will probably end up re-writing
- Not available in standard repos. Extract is deploy.

### Ratings
- Ease of setup: fair
- Easy of use: fair
- Cloud readiness: good
- Datacenter readiness: good
- Automation / pipeline readiness: fair
- Product maturity: fair
- Developer friendliness: fair
- Documentation: fair
- Stability: fair
- Auditability: good
